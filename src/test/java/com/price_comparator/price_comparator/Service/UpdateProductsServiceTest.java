package com.price_comparator.price_comparator.Service;

import com.price_comparator.price_comparator.AbstractBaseTest;
import com.price_comparator.price_comparator.Controller.CurrentDateController;
import com.price_comparator.price_comparator.DTO.ProductDto;
import com.price_comparator.price_comparator.Model.Product;
import com.price_comparator.price_comparator.Model.ProductPrice;
import com.price_comparator.price_comparator.Model.Store;
import com.price_comparator.price_comparator.Repository.ProductPriceRepository;
import com.price_comparator.price_comparator.Repository.ProductRepository;
import com.price_comparator.price_comparator.Repository.StoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@ActiveProfiles("test") // will be configured using application-{test}.properties
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UpdateProductsServiceTest extends AbstractBaseTest {
    @Autowired
    UpdateProductsService updateProductsService;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductPriceRepository productPriceRepository;

    @Autowired
    StoreRepository storeRepository;

    @Autowired
    CurrentDateController currentDateController;

    @BeforeEach
    void deleteAll(){
        productPriceRepository.deleteAll();
        productRepository.deleteAll();
        storeRepository.deleteAll();
    }

    @Test
    void testAddNewProducts(){
        // Test adding new products works as expected

        List<Product> products = List.of(
                new Product("P001", "lapte zuzu", "lactate", "Zuzu", 1.0, "l"),
                new Product("P002", "iaurt grecesc", "lactate", "Lidl", 0.4, "kg")
        );

        productRepository.saveAll(products);

        Optional<Product> result = productRepository.findByProductId("P001");
        assertTrue(result.isPresent());

        assertEquals("lapte zuzu", result.get().getName());
    }

    @Test
    void testAddDuplicateProducts(){
        // Test adding duplicate products works as intended

        List<Product> products = List.of(
                new Product("P001", "lapte zuzu", "lactate", "Zuzu", 1.0, "l"),
                new Product("P001", "lapte zuzu", "lactate", "Zuzu", 1.0, "l"),
                new Product("P002", "iaurt grecesc", "lactate", "Lidl", 0.4, "kg")
        );

        Exception exception = assertThrows(DataIntegrityViolationException.class, () -> {
            productRepository.saveAll(products);
            productRepository.flush(); // Ensure exception is thrown here, saveAll may not hit the db immediately
        });

        String errMessage = exception.getMessage();
        assertTrue(errMessage.contains("could not execute statement"));
    }

    @Test
    void testUpdateProductPrice(){
        // Test that updating productPrice mapping works as intended and adds new mapping

        Store store = setUpStoreEntity("Lidl");
        Product product = setUpProduct("P001", "lapte zuzu", "lactate", "Zuzu", 1.0, "l");

        ProductPrice oldMapping = setUpProductPrice(store, product, "Ron", 12.9, LocalDate.now().minusDays(2), null);

        ProductDto dto = new ProductDto(
                product.getProductId(),
                product.getName(),
                product.getCategory(),
                product.getBrand(),
                product.getPackageQuantity(),
                product.getPackageUnit(),
                oldMapping.getPrice() + 1.5,
                oldMapping.getCurrency()
        );

        updateProductsService.updatePrices(oldMapping, dto, store.getName(), LocalDate.now());
        assert(productPriceRepository.findByProduct_ProductIdAndStore_Name(oldMapping.getProduct().getProductId(), oldMapping.getStore().getName()).isPresent());


        List<ProductPrice> mappings = productPriceRepository.findAll();
        mappings.sort((o1, o2) -> o1.getStartDate().compareTo(o2.getStartDate()));

        // assert that old mapping's endDate was updated to (today - 1 day)
        oldMapping = mappings.get(0);
        assertEquals(oldMapping.getEndDate(), LocalDate.now().minusDays(1));

        // assert new mapping was inserted
        LocalDate currentDate = LocalDate.parse(currentDateController.getCurrentDate());
        ProductPrice newMapping = productPriceRepository.findCurrentPrice(product, store, currentDate).get();
        assertNull(newMapping.getEndDate());
        assertEquals(newMapping.getStartDate(), LocalDate.now());
    }

}
