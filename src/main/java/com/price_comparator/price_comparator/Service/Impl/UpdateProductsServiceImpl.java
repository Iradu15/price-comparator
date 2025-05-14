package com.price_comparator.price_comparator.Service.Impl;

import com.price_comparator.price_comparator.Controller.CSVController;
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

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class UpdateProductsServiceImpl implements UpdateProductsService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductPriceRepository productPriceRepository;

    @Autowired
    StoreRepository storeRepository;


    @Override
    public void processCsvFile(MultipartFile file, String storeName, LocalDate date) {
        List<ProductDto> retrievedProducts = CSVController.parseProductCSV(file);
        List<ProductPrice> productPricesToBeAdded = new ArrayList<>();
        List<Product> productsToBeAdded = new ArrayList<>();

        for(ProductDto productDto: retrievedProducts){
            /*
            there can be 2 products with the same String product id but different prices (P002)
             */

            System.out.println("product: " + productDto.toString());



            // product does not exist, add it
            if(productRepository.findByProductId(productDto.productId()).isEmpty()){
                Product product = createProductFromDto(productDto);
                productsToBeAdded.add(product);
            }

            // product does not exist in this specific store, add mapping
//            if (productPriceRepository.findByProductIdAndStore_Name(productDto.productId(), storeName).isEmpty()){
//                ProductPrice productPrice = createProductPriceFromDto(productDto, storeName);
//                productPricesToBeAdded.add(productPrice);
//            }
        }
        addNewProducts(productsToBeAdded);
    }

    @Override
    public void addNewProducts(List<Product> products) {
        productRepository.saveAll(products);
        System.out.println("Saved all products to database");
    }

    @Override
    public void addNewProductsPrices(List<ProductPrice> productPrices) {
        productPriceRepository.saveAll(productPrices);
    }

    @Override
    public void updatePrices(List<ProductPrice> priceUpdates, Store store, LocalDate fileDate) {

    }

    public Store createStoreFromDto(ProductDto dtoObject, String storeName) {
        return new Store(storeName);
    }

    public Product createProductFromDto(ProductDto dtoObject){
        return new Product(dtoObject.productId(), dtoObject.productName(), dtoObject.productCategory(), dtoObject.brand(), dtoObject.packageQuantity(), dtoObject.packageUnit());
    }

    public ProductPrice createProductPriceFromDto(ProductDto dtoObject, String storeName){
        Store store = null;
        if (storeRepository.findByNameIgnoreCase(storeName).isEmpty()){
            store = createStoreFromDto(dtoObject, storeName);
        }
        else{
            store = storeRepository.findByNameIgnoreCase(storeName).get();

        }

        Product product= productRepository.findByProductId(dtoObject.productId()).get();

        return new ProductPrice(product, store, dtoObject.currency(), dtoObject.price(), LocalDate.now(), null);
    }

}
