package com.price_comparator.price_comparator;

import com.price_comparator.price_comparator.Model.Product;
import com.price_comparator.price_comparator.Model.ProductPrice;
import com.price_comparator.price_comparator.Model.Store;
import com.price_comparator.price_comparator.Repository.ProductPriceRepository;
import com.price_comparator.price_comparator.Repository.ProductRepository;
import com.price_comparator.price_comparator.Repository.StoreRepository;
import com.price_comparator.price_comparator.Service.UpdateProductsService;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;


@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class AbstractBaseTest {

    @Autowired
    public UpdateProductsService updateProductsService;

    @Autowired
    public ProductRepository productRepository;

    @Autowired
    public ProductPriceRepository productPriceRepository;

    @Autowired
    public StoreRepository storeRepository;


    public Store setUpStoreEntity(String storeName){
        Store store = new Store(storeName);
        storeRepository.save(store);
        storeRepository.flush();
        return store;
    }

    public Product setUpProduct(String productId, String name, String category, String brand, Double packageQuantity, String packageUnit){
        Product product = new Product(productId, name, category, brand, packageQuantity, packageUnit);
        productRepository.save(product);
        productRepository.flush();
        return product;
    }

    public ProductPrice setUpProductPrice(Store store, Product product, String currency, Double price, LocalDate startDate, LocalDate endDate){
        ProductPrice productPrice = new ProductPrice(product, store, "Ron", 12.9, LocalDate.now().minusDays(2), null);
        productPriceRepository.save(productPrice);
        productPriceRepository.flush();
        return productPrice;
    }

}
