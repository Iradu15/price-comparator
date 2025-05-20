package com.price_comparator.price_comparator.Controller;

import com.price_comparator.price_comparator.Model.Discount;
import com.price_comparator.price_comparator.Repository.DiscountsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@RestController
public class TopDiscountsController {
    @Autowired
    DiscountsRepository discountsRepository;

    @Autowired
    CurrentDateController currentDateController;

    @GetMapping("topDiscounts")
    ResponseEntity<String> getTopDiscounts(@RequestParam Integer size){
        System.out.printf("Retrieving top %d active discounts", size);
        try{
            LocalDate currentDate = LocalDate.parse(currentDateController.getCurrentDate());
            List<Discount> topDiscounts = discountsRepository.findTopActiveDiscounts(currentDate, size).orElse(Collections.emptyList());
            return ResponseEntity.ok().body("Top " + size + " discounts available:\n " + topDiscounts.toString());

        } catch (Exception e){
            return ResponseEntity.internalServerError().body("Error processing file: " + e.getMessage());
        }
    }
}
