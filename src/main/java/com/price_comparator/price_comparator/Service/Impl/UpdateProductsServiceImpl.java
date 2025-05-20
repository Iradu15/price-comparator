package com.price_comparator.price_comparator.Service.Impl;

import com.price_comparator.price_comparator.Controller.CSVController;
import com.price_comparator.price_comparator.Controller.CurrentDateController;
import com.price_comparator.price_comparator.DTO.ProductDto;
import com.price_comparator.price_comparator.Model.Product;
import com.price_comparator.price_comparator.Model.ProductPrice;
import com.price_comparator.price_comparator.Model.Store;
import com.price_comparator.price_comparator.Repository.ProductPriceRepository;
import com.price_comparator.price_comparator.Repository.ProductRepository;
import com.price_comparator.price_comparator.Repository.StoreRepository;
import com.price_comparator.price_comparator.Service.UpdateProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class UpdateProductsServiceImpl implements UpdateProductsService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductPriceRepository productPriceRepository;

    @Autowired
    StoreRepository storeRepository;

    @Autowired
    CurrentDateController currentDateController;

    @Override
    public void processCsvFile(MultipartFile file, String storeName, LocalDate date) {
        List<ProductDto> retrievedProducts = CSVController.parseProductCSV(file);

        List<ProductPrice> productPricesToBeAdded = new ArrayList<>();
        List<Product> productsToBeAdded = new ArrayList<>();

        createStoreIfNotExistent(storeName);

        // add new products
        for(ProductDto productDto: retrievedProducts)
            if(productRepository.findByProductId(productDto.productId()).isEmpty()){
                Product product = createProductFromDto(productDto);
                productsToBeAdded.add(product);
            }
        productRepository.saveAll(productsToBeAdded);
        System.out.printf("Saved %d new products%n", productsToBeAdded.size());

        for(ProductDto productDto: retrievedProducts) {
            // check was done earlier, it's ok to .get() directly
            Product product = productRepository.findByProductId(productDto.productId()).get();
            Store store = storeRepository.findByNameIgnoreCase(storeName).get();

            if (productPriceRepository.findOverlappingMappings(product, store, date)
                    .filter(list -> !list.isEmpty())
                    .isPresent()) {
                System.out.printf(
                        "Mapping for %s - %s is not valid, there are overlapping mappings for %s%n",
                        product.getProductId(), store.getName(), date.toString()
                );

                continue;
            }

            // update current products-store(productPrice) mapping if date = current day
            LocalDate currentDate = LocalDate.parse(currentDateController.getCurrentDate());
            if (productPriceRepository.findCurrentPrice(product, store, currentDate).isPresent() && currentDate == date){
                ProductPrice existentMapping = productPriceRepository.findCurrentPrice(product, store, currentDate).get();
                updatePrices(existentMapping, productDto, storeName, date);

                System.out.printf("Updated productPrice %s - %s%n", existentMapping.getProduct().getProductId(), existentMapping.getStore().getName());
            }

            // add new products-store(productPrice) mappings
            ProductPrice productPrice = createProductPriceFromDto(productDto, storeName, date);
            productPricesToBeAdded.add(productPrice);
        }

        productPriceRepository.saveAll(productPricesToBeAdded);
        System.out.printf("Saved %d new productPrices%n", productPricesToBeAdded.size());
    }


    @Override
    public void updatePrices(ProductPrice oldMapping, ProductDto productDto, String storeName, LocalDate date) {
        /*
        Update endDate for old mapping from null to date - 1 day
        */
        oldMapping.setEndDate(date.minusDays(1));
        productPriceRepository.save(oldMapping);
    }

    public Store createStoreIfNotExistent(String storeName) {
        if(storeRepository.findByNameIgnoreCase(storeName).isPresent())
            return storeRepository.findByNameIgnoreCase(storeName).get();

        Store store = new Store(storeName);
        storeRepository.save(store);
        System.out.println("Saved store: " + storeName + "\n");
        return store;
    }

    static public Product createProductFromDto(ProductDto dtoObject){
        return new Product(dtoObject.productId(), dtoObject.productName(), dtoObject.productCategory(), dtoObject.brand(), dtoObject.packageQuantity(), dtoObject.packageUnit());
    }

    public ProductPrice createProductPriceFromDto(ProductDto dtoObject, String storeName, LocalDate date){
        Store store = createStoreIfNotExistent(storeName);
        Product product= productRepository.findByProductId(dtoObject.productId()).get();

        return new ProductPrice(product, store, dtoObject.currency(), dtoObject.price(), date, null);
    }

}
