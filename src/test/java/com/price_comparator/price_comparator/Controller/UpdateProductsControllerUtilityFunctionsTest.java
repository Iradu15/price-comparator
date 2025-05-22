package com.price_comparator.price_comparator.Controller;

import com.price_comparator.price_comparator.Model.CurrentDate;
import com.price_comparator.price_comparator.Repository.CurrentDateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

@SpringBootTest
@ActiveProfiles("test") // will be configured using application-{test}.properties
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UpdateProductsControllerUtilityFunctionsTest {

    @Autowired
    CurrentDateRepository currentDateRepository;

    @Autowired
    UpdateProductsController updateProductsController;

    @BeforeEach
    void setUp(){
        currentDateRepository.deleteAll();

        CurrentDate currentDate = new CurrentDate();
        currentDate.setCurrentDay(LocalDate.now());
        currentDateRepository.save(currentDate);
    }

    @Test
    void testIsFilePastTrue(){
        LocalDate fileDate = LocalDate.now().minusDays(1);
        assert(updateProductsController.isFileDatePast(fileDate));
    }

    @Test
    void testIsFileDatePastFalse(){
        LocalDate fileDate = LocalDate.now().plusDays(1);
        assert(!updateProductsController.isFileDatePast(fileDate));
    }
}
