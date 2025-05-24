package com.price_comparator.price_comparator.Controller;

import com.price_comparator.price_comparator.DTO.AlertRequest;
import com.price_comparator.price_comparator.Model.Alert;
import com.price_comparator.price_comparator.Model.Product;
import com.price_comparator.price_comparator.Repository.AlertsRepository;
import com.price_comparator.price_comparator.Repository.ProductRepository;
import com.price_comparator.price_comparator.Service.AlertsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
public class AlertsController {

    @Autowired
    AlertsRepository alertsRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CurrentDateController currentDateController;

    @Autowired
    AlertsService alertsService;

    @PostMapping("createAlert")
    public ResponseEntity<String> createAlert(@RequestBody AlertRequest alertRequest) {
        String productId = alertRequest.getProductId();
        Double targetPrice = alertRequest.getTargetPrice();

        if (productId == null || productId.isBlank()) {
            return ResponseEntity.badRequest().body("Product ID must not be null or empty");
        }
        if (targetPrice == null || targetPrice <= 0) {
            return ResponseEntity.badRequest().body("Target price must be a positive number");
        }

        Optional<Product> optionalProduct = productRepository.findByProductId(productId);
        if (optionalProduct.isEmpty()) {
            return ResponseEntity.badRequest().body("Product with id " + productId + " does not exist");
        }

        Product product = optionalProduct.get();
        Alert newAlert = new Alert(product, targetPrice, LocalDate.parse(currentDateController.getCurrentDate()));
        alertsRepository.save(newAlert);

        return ResponseEntity.ok("Alert created successfully for " + productId + " at " + targetPrice);
    }


    @GetMapping("alerts")
    ResponseEntity<String> alerts(){
        List<Alert> alertList = alertsRepository.findAll();
        return ResponseEntity.ok("Available alerts:\n" + alertList.toString());
    }

    @GetMapping("alert/{id}")
    public ResponseEntity<String> listAlert(@PathVariable String id) {
        long alertId;

        try {
            alertId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Invalid alert ID format: " + id);
        }

        return alertsRepository.findById(alertId)
                .map(alert -> ResponseEntity.ok("Alert with id " + id + ":\n" + alert))
                .orElseGet(() -> ResponseEntity.badRequest().body("Alert with id " + id + " does not exist"));
    }


    @DeleteMapping("deleteAlert/{id}")
    public ResponseEntity<String> deleteAlert(@PathVariable String id) {
        long alertId;

        try {
            alertId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Invalid alert ID format: " + id);
        }

        return alertsRepository.findById(alertId)
                .map(alert -> {
                    alertsRepository.deleteById(alertId);
                    return ResponseEntity.ok("Alert with id " + id + " was deleted successfully");
                })
                .orElseGet(() -> ResponseEntity.badRequest().body("Alert with id " + id + " does not exist"));
    }

    @GetMapping("checkAlerts")
    public ResponseEntity<String> checkAlerts(){
        try{
            System.out.println("Checking alerts status");
            String log = alertsService.checkAlerts();
            return ResponseEntity.ok(log);
        } catch (Exception e){
            return ResponseEntity.internalServerError().body("Error while checking alerts status " + e.getMessage());
        }
    }
}
