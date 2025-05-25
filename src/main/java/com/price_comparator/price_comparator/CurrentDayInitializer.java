package com.price_comparator.price_comparator;

import com.price_comparator.price_comparator.Model.CurrentDate;
import com.price_comparator.price_comparator.Repository.CurrentDateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class CurrentDayInitializer implements CommandLineRunner {

    @Autowired
    CurrentDateRepository currentDateRepository;

    @Override
    public void run(String... args) throws Exception {
        if (!currentDateRepository.existsById(1L)) {
            System.out.println("Current Day initialized with today's date");
            CurrentDate currentDate = new CurrentDate();
            currentDate.setCurrentDay(LocalDate.now());
            currentDateRepository.save(currentDate);
        }
    }
}
