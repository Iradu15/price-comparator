package com.price_comparator.price_comparator.Service;

import com.price_comparator.price_comparator.AbstractBaseTest;
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
public class AlertsServiceTest extends AbstractBaseTest {

    @Autowired
    AlertsService alertsService;

    @Test
    void testCheckAlertsSingleStoreNoDiscount() {
        LocalDate currentDate = setUpCurrentDate(LocalDate.now()).getCurrentDay();
        Store store = setUpStoreEntity("lidl");
        Product product = setUpProduct("P001", "lapte", "lactate", "zuzu", 1.0, "l");
        setUpProductPrice(store, product, "ron", 12.5, currentDate.minusDays(1), null);
        setUpAlert(product, 13.0);
        setUpAlert(product, 8.0);

        assert (alertsRepository.findAll().size() == 2);

        alertsService.checkAlerts();

        assert (alertsRepository.findAll().size() == 1);
        assert (alertsRepository.findAll().getFirst().getTargetPrice().equals(8.0));
    }

    @Test
    void testCheckAlertsSingleStoreDiscount() {
        LocalDate currentDate = setUpCurrentDate(LocalDate.now()).getCurrentDay();
        Store store = setUpStoreEntity("lidl");
        Product product = setUpProduct("P001", "lapte", "lactate", "zuzu", 1.0, "l");
        setUpProductPrice(store, product, "ron", 20.0, currentDate.minusDays(1), null);
        setUpDiscount(product, store, 50, currentDate.minusDays(1), currentDate.plusDays(1));
        setUpAlert(product, 13.0);
        setUpAlert(product, 8.0);

        assert (alertsRepository.findAll().size() == 2);

        alertsService.checkAlerts();

        assert (alertsRepository.findAll().size() == 1);
        assert (alertsRepository.findAll().getFirst().getTargetPrice().equals(8.0));
    }

    @Test
    void testCheckAlertsNoStoresHavingProduct() {
        LocalDate currentDate = setUpCurrentDate(LocalDate.now()).getCurrentDay();
        Store store = setUpStoreEntity("lidl");
        Product product = setUpProduct("P001", "lapte", "lactate", "zuzu", 1.0, "l");
        Product product2 = setUpProduct("P002", "lapte", "lactate", "lidl", 2.0, "l");
        setUpProductPrice(store, product, "ron", 20.0, currentDate.minusDays(1), null);
        setUpProductPrice(store, product2, "ron", 40.0, currentDate.minusDays(1), null);
        setUpAlert(product, 19.0);
        setUpAlert(product2, 39.0);

        assert (alertsRepository.findAll().size() == 2);

        alertsService.checkAlerts();

        assert (alertsRepository.findAll().size() == 2);
    }

    @Test
    void testCheckAlertsOneStoreHavingBelowPrice() {
        LocalDate currentDate = setUpCurrentDate(LocalDate.now()).getCurrentDay();
        Store store = setUpStoreEntity("lidl");
        Store store2 = setUpStoreEntity("kaufland");
        Product product = setUpProduct("P001", "lapte", "lactate", "zuzu", 1.0, "l");
        setUpProductPrice(store, product, "ron", 20.0, currentDate.minusDays(1), null);
        setUpProductPrice(store2, product, "ron", 25.0, currentDate.minusDays(1), null);
        setUpAlert(product, 21.0);

        assert (alertsRepository.findAll().size() == 1);

        alertsService.checkAlerts();

        assert (alertsRepository.findAll().isEmpty());
    }
}
