package com.price_comparator.price_comparator.Controller;

import com.price_comparator.price_comparator.Model.CurrentDate;
import com.price_comparator.price_comparator.Repository.CurrentDateRepository;
import com.price_comparator.price_comparator.Service.AlertsService;
import com.price_comparator.price_comparator.Service.NextDayUpdateProductPricesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NextDayController {
    @Autowired
    CurrentDateRepository currentDateRepository;

    @Autowired
    NextDayUpdateProductPricesService nextDayUpdateProductPricesService;

    @Autowired
    AlertsService alertsService;

    @GetMapping("/next-day")
    public void goToNextDay() {
        CurrentDate currentDate = currentDateRepository.findById(1L).orElseThrow(() -> new RuntimeException(
                "System state not initialized"));
        currentDate.setCurrentDay(currentDate.getCurrentDay().plusDays(1));
        currentDateRepository.save(currentDate);

        try {
            nextDayUpdateProductPricesService.updateProductPrice();
        } catch (Exception e) {
            System.out.printf("Error while updating outdated productPrices: %s", e.getMessage());
        }

        try {
            alertsService.checkAlerts();
        } catch (Exception e) {
            System.out.printf("Error while checking alerts status: %s", e.getMessage());
        }
    }
}
