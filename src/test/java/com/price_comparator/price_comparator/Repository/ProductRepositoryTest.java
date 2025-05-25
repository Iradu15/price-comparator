package com.price_comparator.price_comparator.Repository;

import com.price_comparator.price_comparator.AbstractBaseTest;
import com.price_comparator.price_comparator.Model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test") // will be configured using application-{test}.properties
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProductRepositoryTest extends AbstractBaseTest {
    @BeforeEach
    void deleteAll() {
        currentDateRepository.deleteAll();
        productPriceRepository.deleteAll();
        productRepository.deleteAll();
        storeRepository.deleteAll();
    }

    @Test
    void testAddNewProducts() {
        /*
        Test adding new products works as expected
        */

        List<Product> products = List.of(new Product("P001", "lapte zuzu", "lactate", "Zuzu", 1.0, "l"),
                new Product("P002", "iaurt grecesc", "lactate", "Lidl", 0.4, "kg"));

        productRepository.saveAll(products);

        Optional<Product> result = productRepository.findByProductId("P001");
        assertTrue(result.isPresent());

        assertEquals("lapte zuzu", result.get().getName());
    }

    @Test
    void testAddDuplicateProducts() {
        /*
        Test adding duplicate products works as intended
        */

        List<Product> products = List.of(new Product("P001", "lapte zuzu", "lactate", "Zuzu", 1.0, "l"),
                new Product("P001", "lapte zuzu", "lactate", "Zuzu", 1.0, "l"),
                new Product("P002", "iaurt grecesc", "lactate", "Lidl", 0.4, "kg"));

        Exception exception = assertThrows(DataIntegrityViolationException.class, () -> {
            productRepository.saveAll(products);
            productRepository.flush(); // saveAll may not hit the db immediately
        });

        String errMessage = exception.getMessage();
        assertTrue(errMessage.contains("could not execute statement"));
    }
}