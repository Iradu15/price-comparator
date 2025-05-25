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

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GetFinalPriceServiceTest extends AbstractBaseTest {

    @Autowired
    GetFinalPriceService getFinalPriceService;

    @Test
    void testGetFinalPriceForProductWithDiscount() {
        LocalDate currentDate = setUpCurrentDate(LocalDate.now()).getCurrentDay();
        Store store = setUpStoreEntity("lidl");
        Product product = setUpProduct("P105", "suc", "bauturi", "prigat", 3.0, "l");
        setUpProductPrice(store, product, "ron", 100.0, currentDate.minusDays(1), null);
        setUpDiscount(product, store, 5, currentDate.minusDays(5), currentDate.plusDays(1));

        FinalPrice result = getFinalPriceService.getFinalPriceForProduct(product.getProductId(), store.getName());

        FinalPrice expectedResult = new FinalPrice(product.getProductId(),
                product.getName(),
                product.getBrand(),
                product.getPackageQuantity(),
                product.getPackageUnit(),
                product.getCategory(),
                store.getName(),
                95.0,
                -1.0);

        assert (result.equals(expectedResult));
    }

    @Test
    void testGetFinalPriceForProductNoDiscount() {
        LocalDate currentDate = setUpCurrentDate(LocalDate.now()).getCurrentDay();
        Store store = setUpStoreEntity("lidl");
        Product product = setUpProduct("P105", "suc", "bauturi", "prigat", 3.0, "l");
        setUpProductPrice(store, product, "ron", 100.0, currentDate.minusDays(1), null);

        FinalPrice result = getFinalPriceService.getFinalPriceForProduct(product.getProductId(), store.getName());

        FinalPrice expectedResult = new FinalPrice(product.getProductId(),
                product.getName(),
                product.getBrand(),
                product.getPackageQuantity(),
                product.getPackageUnit(),
                product.getCategory(),
                store.getName(),
                100.0,
                -1.0);

        assert (result.equals(expectedResult));
    }

    @Test
    void testGetFinalPriceForProductPastDiscount() {
        LocalDate currentDate = setUpCurrentDate(LocalDate.now()).getCurrentDay();
        Store store = setUpStoreEntity("lidl");
        Product product = setUpProduct("P105", "suc", "bauturi", "prigat", 3.0, "l");
        setUpProductPrice(store, product, "ron", 100.0, currentDate.minusDays(1), null);
        setUpDiscount(product, store, 5, currentDate.minusDays(5), currentDate.minusDays(1));

        FinalPrice result = getFinalPriceService.getFinalPriceForProduct(product.getProductId(), store.getName());

        FinalPrice expectedResult = new FinalPrice(product.getProductId(),
                product.getName(),
                product.getBrand(),
                product.getPackageQuantity(),
                product.getPackageUnit(),
                product.getCategory(),
                store.getName(),
                100.0,
                -1.0);

        assert (result.equals(expectedResult));
    }

    @Test
    void testGetFinalPriceForProductFutureDiscount() {
        LocalDate currentDate = setUpCurrentDate(LocalDate.now()).getCurrentDay();
        Store store = setUpStoreEntity("lidl");
        Product product = setUpProduct("P105", "suc", "bauturi", "prigat", 3.0, "l");
        setUpProductPrice(store, product, "ron", 100.0, currentDate.minusDays(1), null);
        setUpDiscount(product, store, 5, currentDate.plusDays(1), currentDate.plusDays(5));

        FinalPrice result = getFinalPriceService.getFinalPriceForProduct(product.getProductId(), store.getName());

        FinalPrice expectedResult = new FinalPrice(product.getProductId(),
                product.getName(),
                product.getBrand(),
                product.getPackageQuantity(),
                product.getPackageUnit(),
                product.getCategory(),
                store.getName(),
                100.0,
                -1.0);

        assert (result.equals(expectedResult));
    }

    @Test
    void testGetFinalPriceForProductAllStores2StoresNoDiscount() {
        LocalDate currentDate = setUpCurrentDate(LocalDate.now()).getCurrentDay();
        Store store = setUpStoreEntity("lidl");
        Store store2 = setUpStoreEntity("kaufland");
        Product product = setUpProduct("P105", "suc", "bauturi", "prigat", 3.0, "l");
        setUpProductPrice(store, product, "ron", 50.0, currentDate.minusDays(1), null);
        setUpProductPrice(store2, product, "ron", 100.0, currentDate.minusDays(1), null);

        FinalPrice res = getFinalPriceService.getFinalPriceForProductAllStores(product.getProductId());
        assert res.getFinalPrice().equals(50.0);
    }

    @Test
    void testGetFinalPriceForProductOneStoreDiscount() {
        LocalDate currentDate = setUpCurrentDate(LocalDate.now()).getCurrentDay();
        Store store = setUpStoreEntity("lidl");
        Product product = setUpProduct("P105", "suc", "bauturi", "prigat", 3.0, "l");
        setUpProductPrice(store, product, "ron", 100.0, currentDate.minusDays(1), null);
        setUpDiscount(product, store, 15, currentDate.minusDays(1), currentDate.plusDays(5));

        FinalPrice res = getFinalPriceService.getFinalPriceForProductAllStores(product.getProductId());
        assert res.getFinalPrice().equals(85.0);
    }

    @Test
    void testGetFinalPriceForProductAllStores2StoresDiscount() {
        LocalDate currentDate = setUpCurrentDate(LocalDate.now()).getCurrentDay();
        Store store = setUpStoreEntity("lidl");
        Store store2 = setUpStoreEntity("kaufland");
        Product product = setUpProduct("P105", "suc", "bauturi", "prigat", 3.0, "l");
        setUpProductPrice(store, product, "ron", 50.0, currentDate.minusDays(1), null);
        setUpProductPrice(store2, product, "ron", 100.0, currentDate.minusDays(1), null);
        setUpDiscount(product, store2, 70, currentDate.minusDays(1), currentDate.plusDays(5));

        FinalPrice res = getFinalPriceService.getFinalPriceForProductAllStores(product.getProductId());
        assert res.getFinalPrice().equals(30.0);
        assert res.getStoreName().equals(store2.getName());
    }
}
