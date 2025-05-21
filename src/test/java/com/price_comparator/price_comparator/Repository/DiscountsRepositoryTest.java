package com.price_comparator.price_comparator.Repository;

import com.price_comparator.price_comparator.AbstractBaseTest;
import com.price_comparator.price_comparator.Model.Discount;
import com.price_comparator.price_comparator.Model.Product;
import com.price_comparator.price_comparator.Model.Store;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;


@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DiscountsRepositoryTest extends AbstractBaseTest {
    @Test
    void testFindTopDiscountsCount() {
        /*
        Test findTopDiscounts size works as expected
        */
        LocalDate currentDate = setUpCurrentDate(LocalDate.now()).getCurrentDay();
        Store store = setUpStoreEntity("lidl");
        Product product = setUpProduct("P001", "lapte", "lactate", "zuzu", 1.0, "l");
        setUpProductPrice(store, product, "ron", 10.0, currentDate.minusDays(1), null);
        Discount discount = setUpDiscount(product, store, 10, currentDate, currentDate.plusDays(1));
        Discount discount2 = setUpDiscount(product, store, 15, currentDate.plusDays(2), currentDate.plusDays(3));

        Product product2 = setUpProduct("P002", "lapte", "lactate", "zuzu", 2.0, "l");
        setUpProductPrice(store, product2, "ron", 15.0, currentDate.minusDays(1), null);
        Discount discount3 = setUpDiscount(product2, store, 30, currentDate, currentDate.plusDays(1));

        int size = 2;
        List<Discount> topDiscounts = discountsRepository.findTopActiveDiscounts(currentDate, size).get();

        assert(topDiscounts.size() == size);
    }

    @Test
    void testFindTopDiscounts() {
        /*
        Test findTopDiscounts only selects active discounts
        */
        LocalDate currentDate = setUpCurrentDate(LocalDate.now()).getCurrentDay();
        Store store = setUpStoreEntity("lidl");
        Product product = setUpProduct("P001", "lapte", "lactate", "zuzu", 1.0, "l");
        setUpProductPrice(store, product, "ron", 10.0, currentDate.minusDays(1), null);
        Discount discount = setUpDiscount(product, store, 10, currentDate, currentDate.plusDays(1));
        Discount discount2 = setUpDiscount(product, store, 15, currentDate.plusDays(2), currentDate.plusDays(3));

        Product product2 = setUpProduct("P002", "lapte", "lactate", "zuzu", 2.0, "l");
        setUpProductPrice(store, product2, "ron", 15.0, currentDate.minusDays(1), null);
        Discount discount3 = setUpDiscount(product2, store, 30, currentDate, currentDate.plusDays(1));

        int size = 2;
        List<Discount> topDiscounts = discountsRepository.findTopActiveDiscounts(currentDate, size).get();

        /*
        discount2 > discount, but discount2 is not active yey
         */

        assert(topDiscounts.get(0).getPercentageOfDiscount().equals(discount3.getPercentageOfDiscount()));
        assert(topDiscounts.get(1).getPercentageOfDiscount().equals(discount.getPercentageOfDiscount()));
    }

    @Test
    void testFindOverlappingDiscountsLeftOverlap() {
        LocalDate from = LocalDate.of(2025, 5, 10);
        LocalDate to = LocalDate.of(2025, 5, 20);

        Store store = setUpStoreEntity("lidl");
        Product product = setUpProduct("P100", "cafea", "bauturi", "jacobs", 1.0, "kg");

        setUpDiscount(product, store, 10, LocalDate.of(2025, 5, 5), LocalDate.of(2025, 5, 12));

        List<Discount> overlaps = discountsRepository.findOverlappingDiscounts(store, product, from, to).get();
        assert(overlaps.size() == 1);
    }

    @Test
    void testFindOverlappingDiscountsRightOverlap() {
        LocalDate from = LocalDate.of(2025, 5, 10);
        LocalDate to = LocalDate.of(2025, 5, 20);

        Store store = setUpStoreEntity("lidl");
        Product product = setUpProduct("P101", "faina", "panificatie", "buhusi", 1.0, "kg");

        setUpDiscount(product, store, 20, LocalDate.of(2025, 5, 18), LocalDate.of(2025, 5, 25));

        List<Discount> overlaps = discountsRepository.findOverlappingDiscounts(store, product, from, to).get();
        assert(overlaps.size() == 1);
    }

    @Test
    void testFindOverlappingDiscountsNewIntervalContainsExisting() {
        LocalDate from = LocalDate.of(2025, 5, 1);
        LocalDate to = LocalDate.of(2025, 5, 30);

        Store store = setUpStoreEntity("carrefour");
        Product product = setUpProduct("P102", "apa", "bauturi", "borsec", 2.0, "l");

        Discount existing = setUpDiscount(product, store, 5, LocalDate.of(2025, 5, 10), LocalDate.of(2025, 5, 15));

        List<Discount> overlaps = discountsRepository.findOverlappingDiscounts(store, product, from, to).get();
        assert(overlaps.size() == 1);
    }

    @Test
    void testFindOverlappingDiscountsNewIntervalIsContainedByExisting() {
        LocalDate from = LocalDate.of(2025, 5, 5);
        LocalDate to = LocalDate.of(2025, 5, 15);

        Store store = setUpStoreEntity("carrefour");
        Product product = setUpProduct("P102", "apa", "bauturi", "borsec", 2.0, "l");

        Discount existing = setUpDiscount(product, store, 5, LocalDate.of(2025, 5, 1), LocalDate.of(2025, 5, 30));

        List<Discount> overlaps = discountsRepository.findOverlappingDiscounts(store, product, from, to).get();
        assert(overlaps.size() == 1);
    }

    @Test
    void testFindOverlappingDiscountsNoOverlap() {
        LocalDate from = LocalDate.of(2025, 6, 1);
        LocalDate to = LocalDate.of(2025, 6, 10);

        Store store = setUpStoreEntity("lidl");
        Product product = setUpProduct("P103", "zahar", "dulciuri", "cristal", 1.0, "kg");

        Discount existing = setUpDiscount(product, store, 7, LocalDate.of(2025, 5, 1), LocalDate.of(2025, 5, 15));

        List<Discount> overlaps = discountsRepository.findOverlappingDiscounts(store, product, from, to).get();
        assert(overlaps.isEmpty());
    }



}
