package com.price_comparator.price_comparator.Controller;

import com.price_comparator.price_comparator.DTO.FinalPrice;
import com.price_comparator.price_comparator.Service.BestPricePerUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BestPricePerUnitController {
    @Autowired
    BestPricePerUnitService bestPricePerUnitService;

    @GetMapping("bestPricePerUnit")
    ResponseEntity<String> getBestPricePerUnit(@RequestParam Integer size) {
        System.out.printf("Retrieving top %d products based on unit / price", size);

        try {
            List<FinalPrice> bestPricePerUnit = bestPricePerUnitService.getBestProductsPerUnit(size);
            return ResponseEntity.ok("Best "
                    + size
                    + " products by unit / price are: \n"
                    + bestPricePerUnit.toString());
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error while retrieving best products by unit / price: " + e.getMessage());
        }
    }
}
