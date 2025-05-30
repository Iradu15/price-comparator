package com.price_comparator.price_comparator.Controller;

import com.price_comparator.price_comparator.Model.CurrentDate;
import com.price_comparator.price_comparator.Repository.CurrentDateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class CurrentDateController {

    @Autowired
    CurrentDateRepository currentDateRepository;

    @GetMapping("/get-date")
    public String getCurrentDate() {
        CurrentDate date = currentDateRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("System state not initialized"));

        return date.getCurrentDay().toString();
    }
}
