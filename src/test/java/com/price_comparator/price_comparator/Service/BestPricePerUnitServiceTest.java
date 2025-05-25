package com.price_comparator.price_comparator.Service;


import com.price_comparator.price_comparator.AbstractBaseTest;
import com.price_comparator.price_comparator.DTO.FinalPrice;
import com.price_comparator.price_comparator.Model.Product;
import com.price_comparator.price_comparator.Model.Store;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BestPricePerUnitServiceTest extends AbstractBaseTest {

    @Autowired
    BestPricePerUnitService bestPricePerUnitService;

    @Test
    void testGetBestProductsPerUnitWithoutDiscount() {
        LocalDate currentDate = setUpCurrentDate(LocalDate.now()).getCurrentDay();
        Store store = setUpStoreEntity("lidl");
        Product product = setUpProduct("P001", "lapte", "lactate", "zuzu", 1.0, "l");
        setUpProductPrice(store, product, "ron", 10.0, currentDate, null);

        Product product2 = setUpProduct("P002", "lapte", "lactate", "napolact", 2.0, "l");
        setUpProductPrice(store, product2, "ron", 10.0, currentDate, null);

        List<FinalPrice> bestProductsPerUnit = bestPricePerUnitService.getBestProductsPerUnit(2);
        assert (bestProductsPerUnit.size() == 2);

        FinalPrice expectedResultFirst = new FinalPrice(product2.getProductId(),
                product2.getName(),
                product2.getBrand(),
                product2.getPackageQuantity(),
                product2.getPackageUnit(),
                product2.getCategory(),
                store.getName(),
                10.0,
                5.0);

        assert (bestProductsPerUnit.getFirst().equals(expectedResultFirst));
    }

    @Test
    void testGetBestProductsPerUnitWithDiscount() {
        LocalDate currentDate = setUpCurrentDate(LocalDate.now()).getCurrentDay();
        Store store = setUpStoreEntity("lidl");
        Product product = setUpProduct("P001", "lapte", "lactate", "zuzu", 1.0, "l");
        setUpProductPrice(store, product, "ron", 10.0, currentDate, null);
        setUpDiscount(product, store, 90, currentDate.minusDays(1), currentDate.plusDays(1));

        Product product2 = setUpProduct("P002", "lapte", "lactate", "napolact", 2.0, "l");
        setUpProductPrice(store, product2, "ron", 10.0, currentDate, null);

        List<FinalPrice> bestProductsPerUnit = bestPricePerUnitService.getBestProductsPerUnit(2);
        assert (bestProductsPerUnit.size() == 2);

        FinalPrice expectedResultFirst = new FinalPrice(product.getProductId(),
                product.getName(),
                product.getBrand(),
                product.getPackageQuantity(),
                product.getPackageUnit(),
                product.getCategory(),
                store.getName(),
                1.0,
                1.0);

        assert (bestProductsPerUnit.getFirst().equals(expectedResultFirst));
    }

    @Test
    void testGetBestProductsPerUnitWithoutDiscountNewMappingIsBad() {
        /*
        First mapping for product is cheap but no longer available, second is more expensive than second product
        */
        LocalDate currentDate = setUpCurrentDate(LocalDate.now()).getCurrentDay();
        Store store = setUpStoreEntity("lidl");
        Product product = setUpProduct("P001", "lapte", "lactate", "zuzu", 1.0, "l");
        setUpProductPrice(store, product, "ron", 1.0, currentDate.minusDays(2), currentDate.minusDays(1));
        setUpProductPrice(store, product, "ron", 10.0, currentDate, null);

        Product product2 = setUpProduct("P002", "lapte", "lactate", "napolact", 2.0, "l");
        setUpProductPrice(store, product2, "ron", 10.0, currentDate, null);

        List<FinalPrice> bestProductsPerUnit = bestPricePerUnitService.getBestProductsPerUnit(2);
        assert (bestProductsPerUnit.size() == 2);

        FinalPrice expectedResultFirst = new FinalPrice(product2.getProductId(),
                product2.getName(),
                product2.getBrand(),
                product2.getPackageQuantity(),
                product2.getPackageUnit(),
                product2.getCategory(),
                store.getName(),
                10.0,
                5.0);

        assert (bestProductsPerUnit.getFirst().equals(expectedResultFirst));
    }

    @Test
    void testGetBestProductsPerUnitThrowRuntimeException() {
        setUpCurrentDate(LocalDate.now());
        setUpStoreEntity("lidl");
        setUpProduct("P001", "lapte", "lactate", "zuzu", 1.0, "l");
        setUpProduct("P002", "lapte", "lactate", "napolact", 2.0, "l");

        Exception exception = assertThrows(RuntimeException.class, () -> {
            bestPricePerUnitService.getBestProductsPerUnit(2);
        });

        String errMessage = exception.getMessage();
        assert (errMessage.contains("No ProductPrices available for the moment, add some first"));
    }

    @Test
    void testGetBestProductsPerUnitMoreSizeThanMappings() {
        LocalDate currentDate = setUpCurrentDate(LocalDate.now()).getCurrentDay();
        Store store = setUpStoreEntity("lidl");
        Product product = setUpProduct("P001", "lapte", "lactate", "zuzu", 1.0, "l");
        setUpProductPrice(store, product, "ron", 1.0, currentDate.minusDays(2), currentDate.minusDays(1));
        setUpProductPrice(store, product, "ron", 10.0, currentDate, null);

        Product product2 = setUpProduct("P002", "lapte", "lactate", "napolact", 2.0, "l");
        setUpProductPrice(store, product2, "ron", 10.0, currentDate, null);

        List<FinalPrice> bestProductsPerUnit = bestPricePerUnitService.getBestProductsPerUnit(100);
        assert (bestProductsPerUnit.size() == 2);
    }

    @Test
    void testGetBestProductsPerUnitDifferentUnits() {
        LocalDate currentDate = setUpCurrentDate(LocalDate.now()).getCurrentDay();
        Store store = setUpStoreEntity("lidl");
        Product product = setUpProduct("P001", "lapte", "lactate", "zuzu", 1.0, "l");
        setUpProductPrice(store, product, "ron", 10.0, currentDate, null);

        Product product2 = setUpProduct("P002", "lapte", "lactate", "napolact", 200.0, "ml");
        setUpProductPrice(store, product2, "ron", 1.5, currentDate, null);

        List<FinalPrice> bestProductsPerUnit = bestPricePerUnitService.getBestProductsPerUnit(2);
        assert (bestProductsPerUnit.size() == 2);

        FinalPrice expectedResultFirst = new FinalPrice(product2.getProductId(),
                product2.getName(),
                product2.getBrand(),
                product2.getPackageQuantity(),
                product2.getPackageUnit(),
                product2.getCategory(),
                store.getName(),
                1.5,
                7.5);

        assert (bestProductsPerUnit.getFirst().equals(expectedResultFirst));
    }
}
