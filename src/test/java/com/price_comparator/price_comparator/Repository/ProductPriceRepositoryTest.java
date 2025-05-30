package com.price_comparator.price_comparator.Repository;

import com.price_comparator.price_comparator.AbstractBaseTest;
import com.price_comparator.price_comparator.Model.Product;
import com.price_comparator.price_comparator.Model.ProductPrice;
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
public class ProductPriceRepositoryTest extends AbstractBaseTest {

    @Test
    void testFindCurrentPricePast() {
        /*
        Test findCurrentPrice for past mapping
        */
        LocalDate currentDate = setUpCurrentDate(LocalDate.now()).getCurrentDay();
        Store store = setUpStoreEntity("lidl");
        Product product = setUpProduct("P001", "lapte", "lactate", "zuzu", 1.0, "l");
        setUpProductPrice(store, product, "ron", 10.0, currentDate.minusDays(1), null);

        ProductPrice currentMapping = productPriceRepository.findCurrentPrice(product, store, currentDate).get();

        assert (currentMapping.getStartDate().equals(currentDate.minusDays(1)));
    }

    @Test
    void testFindCurrentPriceFuture() {
        /*
        Test findCurrentPrice for future mapping.
        */
        LocalDate currentDate = setUpCurrentDate(LocalDate.now()).getCurrentDay();
        Store store = setUpStoreEntity("lidl");
        Product product = setUpProduct("P002", "lapte", "lactate", "zuzu", 2.0, "l");
        setUpProductPrice(store, product, "ron", 18.0, currentDate.plusDays(1), null);

        assert (productPriceRepository.findCurrentPrice(product, store, currentDate).isEmpty());
    }

    @Test
    void testFindAllCurrentPricesThatNeedToBeReplacedCorrectCount() {
        /*
        Test that method selects correct mappings
        */
        LocalDate currentDate = setUpCurrentDate(LocalDate.now()).getCurrentDay();
        Store store = setUpStoreEntity("lidl");
        Product product = setUpProduct("P001", "lapte", "lactate", "zuzu", 1.0, "l");
        setUpProductPrice(store, product, "ron", 10.0, currentDate.minusDays(1), null);
        setUpProductPrice(store, product, "ron", 9.0, currentDate, null);

        Product product2 = setUpProduct("P002", "lapte", "lactate", "zuzu", 2.0, "l");
        setUpProductPrice(store, product2, "ron", 15.0, currentDate.plusDays(1), null);

        List<ProductPrice> mappingsThatNeedToBeReplaced =
                productPriceRepository.findAllCurrentPricesThatNeedToBeReplaced(
                currentDate).get();

        assert (mappingsThatNeedToBeReplaced.size() == 1);
    }

    @Test
    void testFindAllCurrentPricesThatNeedToBeReplaced() {
        /*
        Test FindAllCurrentPricesThatNeedToBeReplaced selects correct mappings
        */
        LocalDate currentDate = setUpCurrentDate(LocalDate.now()).getCurrentDay();
        Store store = setUpStoreEntity("lidl");
        Product product = setUpProduct("P001", "lapte", "lactate", "zuzu", 1.0, "l");
        setUpProductPrice(store, product, "ron", 10.0, currentDate.minusDays(1), null);
        setUpProductPrice(store, product, "ron", 9.0, currentDate, null);

        Product product2 = setUpProduct("P002", "lapte", "lactate", "zuzu", 2.0, "l");
        setUpProductPrice(store, product2, "ron", 15.0, currentDate.plusDays(1), null);

        List<ProductPrice> mappingsThatNeedToBeReplaced =
                productPriceRepository.findAllCurrentPricesThatNeedToBeReplaced(
                currentDate).get();

        assert (mappingsThatNeedToBeReplaced.getFirst().getStartDate().equals(currentDate.minusDays(1)));
    }

    @Test
    void testFindOverlappingMappingsCount() {
        /*
        Test findOverlappingMappings selects correct mappings
        */
        LocalDate currentDate = setUpCurrentDate(LocalDate.now()).getCurrentDay();
        Store store = setUpStoreEntity("lidl");
        Product product = setUpProduct("P001", "lapte", "lactate", "zuzu", 1.0, "l");
        setUpProductPrice(store, product, "ron", 10.0, currentDate, null);

        Product product2 = setUpProduct("P002", "lapte", "lactate", "zuzu", 1.0, "l");
        setUpProductPrice(store, product2, "ron", 15.0, currentDate.plusDays(1), null);


        List<ProductPrice> overlappingMappings = productPriceRepository.findOverlappingMappings(product,
                store,
                currentDate).get();

        assert (overlappingMappings.size() == 1);
    }

    @Test
    void testFindOverlappingMappings() {
        /*
        Test findOverlappingMappings selects correct mappings
        */
        LocalDate currentDate = setUpCurrentDate(LocalDate.now()).getCurrentDay();
        Store store = setUpStoreEntity("lidl");
        Product product = setUpProduct("P001", "lapte", "lactate", "zuzu", 1.0, "l");
        setUpProductPrice(store, product, "ron", 10.0, currentDate, null);

        Product product2 = setUpProduct("P002", "lapte", "lactate", "zuzu", 1.0, "l");
        setUpProductPrice(store, product2, "ron", 15.0, currentDate.plusDays(1), null);


        List<ProductPrice> overlappingMappings = productPriceRepository.findOverlappingMappings(product,
                store,
                currentDate).get();

        assert (overlappingMappings.getFirst().getStartDate().equals(currentDate));
    }

    @Test
    void testFindAllCurrentProductPricesIgnoresFinishedItems() {
        LocalDate currentDate = setUpCurrentDate(LocalDate.now()).getCurrentDay();
        Store store = setUpStoreEntity("lidl");
        Product product = setUpProduct("P001", "lapte", "lactate", "zuzu", 1.0, "l");
        setUpProductPrice(store, product, "ron", 10.0, currentDate, null);
        setUpProductPrice(store, product, "ron", 10.0, currentDate.minusDays(3), currentDate.minusDays(2));

        List<ProductPrice> currentProductPrices = productPriceRepository.findAllCurrentProductPrices(currentDate).get();

        assert (currentProductPrices.size() == 1);
        assert (currentProductPrices.getFirst().getStartDate().equals(currentDate));
    }

    @Test
    void testFindAllCurrentProductPricesFutureFinishedItems() {
        LocalDate currentDate = setUpCurrentDate(LocalDate.now()).getCurrentDay();
        Store store = setUpStoreEntity("lidl");
        Product product = setUpProduct("P001", "lapte", "lactate", "zuzu", 1.0, "l");
        setUpProductPrice(store, product, "ron", 10.0, currentDate, null);
        setUpProductPrice(store, product, "ron", 10.0, currentDate.plusDays(3), null);

        List<ProductPrice> currentProductPrices = productPriceRepository.findAllCurrentProductPrices(currentDate).get();

        assert (currentProductPrices.size() == 1);
        assert (currentProductPrices.getFirst().getStartDate().equals(currentDate));
    }

    @Test
    void testFindStoresHavingProductSuccess() {
        LocalDate currentDate = setUpCurrentDate(LocalDate.now()).getCurrentDay();
        Store store = setUpStoreEntity("lidl");
        Store store2 = setUpStoreEntity("kaufland");
        Product product = setUpProduct("P001", "lapte", "lactate", "zuzu", 1.0, "l");
        setUpProductPrice(store, product, "ron", 9.0, currentDate, null);
        setUpProductPrice(store2, product, "ron", 10.0, currentDate.minusDays(1), null);

        List<Store> stores = productPriceRepository.findStoresHavingProduct(product, currentDate).get();

        assert (stores.size() == 2);
        assert (stores.getFirst().getName().equals("lidl"));
        assert (stores.getLast().getName().equals("kaufland"));
    }

    @Test
    void testFindStoresHavingProductIgnoreOutdatedMappings() {
        LocalDate currentDate = setUpCurrentDate(LocalDate.now()).getCurrentDay();
        Store store = setUpStoreEntity("lidl");
        Store store2 = setUpStoreEntity("kaufland");
        Product product = setUpProduct("P001", "lapte", "lactate", "zuzu", 1.0, "l");
        setUpProductPrice(store, product, "ron", 9.0, currentDate, null);
        setUpProductPrice(store2, product, "ron", 10.0, currentDate.minusDays(2), currentDate.minusDays(1));

        List<Store> stores = productPriceRepository.findStoresHavingProduct(product, currentDate).get();

        assert (stores.size() == 1);
        assert (stores.getFirst().getName().equals("lidl"));
    }

    @Test
    void testFindStoresHavingProductIgnoreFutureMappings() {
        LocalDate currentDate = setUpCurrentDate(LocalDate.now()).getCurrentDay();
        Store store = setUpStoreEntity("lidl");
        Store store2 = setUpStoreEntity("kaufland");
        Product product = setUpProduct("P001", "lapte", "lactate", "zuzu", 1.0, "l");
        setUpProductPrice(store, product, "ron", 9.0, currentDate, null);
        setUpProductPrice(store2, product, "ron", 10.0, currentDate.plusDays(1), currentDate.plusDays(2));

        List<Store> stores = productPriceRepository.findStoresHavingProduct(product, currentDate).get();

        assert (stores.size() == 1);
        assert (stores.getFirst().getName().equals("lidl"));
    }
}
