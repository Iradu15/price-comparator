package com.price_comparator.price_comparator.Controller;

import com.price_comparator.price_comparator.Model.CurrentDate;
import com.price_comparator.price_comparator.Repository.CurrentDateRepository;
import com.price_comparator.price_comparator.Service.UpdateDiscountsService;
import com.price_comparator.price_comparator.Service.UpdateProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@RestController
public class UpdateProductsController {

    @Autowired
    CurrentDateRepository currentDateRepository;

    @Autowired
    private UpdateProductsService updateService;

    @Autowired
    private UpdateDiscountsService updateDiscountsService;

    @PostMapping("update")
    ResponseEntity<String> updateProducts(@RequestParam("file") MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null
                || StringUtils.countOccurrencesOf(originalFilename, "_") != 1
                || StringUtils.countOccurrencesOf(originalFilename, "-") != 2) {
            return ResponseEntity.badRequest().body("Invalid filename format. Expected: store_yyyy-MM-dd.csv");
        }

        String[] parts = file.getOriginalFilename().split("_");
        String storeName = parts[0];
        LocalDate fileDate = LocalDate.parse(parts[1].replace(".csv", ""));

        if (isFileDatePast(fileDate)) {
            return ResponseEntity.badRequest().body("Past updates cannot be applied");
        }

        try {
            updateService.processCsvFile(file, storeName, fileDate);
            return ResponseEntity.ok("Product prices updated");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error processing file: " + e.getMessage());
        }
    }

    @PostMapping("update/discounts")
    ResponseEntity<String> updateDiscounts(@RequestParam("file") MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null
                || StringUtils.countOccurrencesOf(originalFilename, "_") != 2
                || StringUtils.countOccurrencesOf(originalFilename, "-") != 2) {
            return ResponseEntity.badRequest()
                    .body("Invalid filename format. Expected: store_discounts_yyyy-MM-dd.csv");
        }

        String[] parts = file.getOriginalFilename().split("_");

        if (!parts[1].equals("discounts")) return ResponseEntity.badRequest().body(
                "Invalid filename format. Expected: store_discounts_yyyy-MM-dd.csv");

        String storeName = parts[0];
        LocalDate fileDate = LocalDate.parse(parts[2].replace(".csv", ""));

        if (isFileDatePast(fileDate)) {
            return ResponseEntity.badRequest().body("Past discounts cannot be applied");
        }

        try {
            updateDiscountsService.processCsvFile(file, storeName, fileDate);
            return ResponseEntity.ok("Discounts added successfully");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error processing file: " + e.getMessage());
        }
    }

    public boolean isFileDatePast(LocalDate fileDate) {
        CurrentDate currentDate = currentDateRepository.findById(1L).orElseThrow(() -> new RuntimeException(
                "System state not initialized"));
        return fileDate.isBefore(currentDate.getCurrentDay());
    }
}
