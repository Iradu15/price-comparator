package com.price_comparator.price_comparator;

import com.price_comparator.price_comparator.Model.*;
import com.price_comparator.price_comparator.Repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class AbstractBaseTest {
    @Autowired
    public ProductRepository productRepository;

    @Autowired
    public ProductPriceRepository productPriceRepository;

    @Autowired
    public StoreRepository storeRepository;

    @Autowired
    public CurrentDateRepository currentDateRepository;

    @Autowired
    public DiscountsRepository discountsRepository;

    @Autowired
    public AlertsRepository alertsRepository;

    @Autowired
    public ShoppingListRepository shoppingListRepository;

    @BeforeEach
    void deleteAll() {
        shoppingListRepository.deleteAll();
        alertsRepository.deleteAll();
        discountsRepository.deleteAll();
        currentDateRepository.deleteAll();
        productPriceRepository.deleteAll();
        productRepository.deleteAll();
        storeRepository.deleteAll();
    }

    public Store setUpStoreEntity(String storeName) {
        Store store = new Store(storeName);
        storeRepository.save(store);
        storeRepository.flush();
        return store;
    }

    public Product setUpProduct(String productId, String name, String category, String brand, Double packageQuantity,
                                String packageUnit) {
        Product product = new Product(productId, name, category, brand, packageQuantity, packageUnit);
        productRepository.save(product);
        productRepository.flush();
        return product;
    }

    public ProductPrice setUpProductPrice(Store store, Product product, String currency, Double price,
                                          LocalDate startDate, LocalDate endDate) {
        ProductPrice productPrice = new ProductPrice(product, store, currency, price, startDate, endDate);
        productPriceRepository.save(productPrice);
        productPriceRepository.flush();
        return productPrice;
    }

    public CurrentDate setUpCurrentDate(LocalDate date) {
        CurrentDate currentDate = new CurrentDate();
        currentDate.setCurrentDay(date);
        currentDateRepository.save(currentDate);
        currentDateRepository.flush();
        return currentDate;
    }

    public Discount setUpDiscount(Product product, Store store, int percentageOfDiscount, LocalDate fromDate,
                                  LocalDate toDate) {
        Discount discount = new Discount(product, store, percentageOfDiscount, fromDate, toDate);
        discountsRepository.save(discount);
        discountsRepository.flush();
        return discount;
    }

    public Alert setUpAlert(Product product, Double targetPrice) {
        Alert alert = new Alert(product, targetPrice, LocalDate.now());
        alertsRepository.save(alert);
        alertsRepository.flush();
        return alert;
    }

    public ShoppingList setUpShoppingList(List<Product> products, List<Integer> quantities) {
        List<ShoppingListItem> items = new ArrayList<>();
        ShoppingList shoppingList = new ShoppingList();
        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            int quantity = quantities.get(i);
            items.add(new ShoppingListItem(product, shoppingList, quantity));
        }
        shoppingList.setShoppingListItems(items);
        shoppingListRepository.save(shoppingList);
        shoppingListRepository.flush();

        return shoppingList;
    }
}
