package com.price_comparator.price_comparator.Controller;

import com.price_comparator.price_comparator.Service.UpdateProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@RestController
public class UpdateProductsController {

    @Autowired
    private UpdateProductsService updateService; // Injects the DepartmentService dependency.


    @PostMapping("update")
    ResponseEntity<String> updateProducts(@RequestParam("file") MultipartFile file){
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !originalFilename.contains("_") || StringUtils.countOccurrencesOf(originalFilename, "-") != 2) {
            return ResponseEntity.badRequest().body("Invalid filename format. Expected: store_yyyy-MM-dd.csv");
        }

        String[] parts = file.getOriginalFilename().split("_");

        String storeName = parts[0];
        LocalDate fileDate = LocalDate.parse(parts[1].replace(".csv", ""));

        try {
            updateService.processCsvFile(file, storeName, fileDate);
            return ResponseEntity.ok("Product prices updated");
        } catch (Exception e){
            return ResponseEntity.internalServerError().body("Error processing file: " + e.getMessage());
        }
    }
}
