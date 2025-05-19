package com.price_comparator.price_comparator.Controller;

import com.price_comparator.price_comparator.Model.CurrentDate;
import com.price_comparator.price_comparator.Repository.CurrentDateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
public class CurrentDateController {

    @Autowired
    CurrentDateRepository currentDateRepository;

    @GetMapping("/next-day")
    public void goToNextDay(){
        CurrentDate currentDate = currentDateRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("System state not initialized"));
        currentDate.setCurrentDay(currentDate.getCurrentDay().plusDays(1));
        currentDateRepository.save(currentDate);
    }

    @GetMapping("/get-date")
    public String getCurrentDate(){
        CurrentDate date = currentDateRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("System state not initialized"));

        return date.getCurrentDay().toString();
    }
}
