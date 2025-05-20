package com.price_comparator.price_comparator.Controller;

import com.price_comparator.price_comparator.AbstractBaseTest;
import com.price_comparator.price_comparator.Model.Product;
import com.price_comparator.price_comparator.Model.ProductPrice;
import com.price_comparator.price_comparator.Model.Store;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;


@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TopDiscountsControllerTest extends AbstractBaseTest {
    void testOnlyAvailableDiscountsAreConsidered(){
        Store store = setUpStoreEntity("Lidl");
        storeRepository.save(store);

        Product product = setUpProduct("P001", "lapte zuzu", "lactate", "Zuzu", 1.0, "l");
        productRepository.save(product);

        ProductPrice mapping = setUpProductPrice(store, product, "Ron", 12.9, LocalDate.now().minusDays(2), null);
        productPriceRepository.save(mapping);

        Product product2 = setUpProduct("P002", "iaurt", "lactate", "Zuzu", 200.0, "ml");
        productRepository.save(product2);

        ProductPrice mapping2 = setUpProductPrice(store, product, "Ron", 6.5, LocalDate.now().minusDays(2), null);
        productPriceRepository.save(mapping2);


    }
}
