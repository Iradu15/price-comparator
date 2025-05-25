package com.price_comparator.price_comparator.Service.Impl;

import com.price_comparator.price_comparator.DTO.AlertResponseDto;
import com.price_comparator.price_comparator.DTO.FinalPrice;
import com.price_comparator.price_comparator.Model.Alert;
import com.price_comparator.price_comparator.Model.Product;
import com.price_comparator.price_comparator.Repository.AlertsRepository;
import com.price_comparator.price_comparator.Service.AlertsService;
import com.price_comparator.price_comparator.Service.GetFinalPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AlertsServiceImpl implements AlertsService {

    @Autowired
    AlertsRepository alertsRepository;

    @Autowired
    GetFinalPriceService getFinalPriceService;

    @Override
    public String checkAlerts() {
        List<Alert> alertList = alertsRepository.findAll();
        List<AlertResponseDto> processedAlerts = new ArrayList<>();

        for (Alert alert : alertList) {
            Product product = alert.getProduct();
            Double targetPrice = alert.getTargetPrice();

            try {
                FinalPrice cheapestPrice =
                        getFinalPriceService.getFinalPriceForProductAllStores(product.getProductId());
                Double price = cheapestPrice.getFinalPrice();
                String storeName = cheapestPrice.getStoreName();

                if (price <= targetPrice) {
                    System.out.printf("Price for %s is finally below %f: %f%n",
                            product.getProductId(),
                            targetPrice,
                            price);
                    AlertResponseDto alertResponseDto = createDtoFromAlert(alert, storeName, price);
                    processedAlerts.add(alertResponseDto);
                    alertsRepository.delete(alert);
                }
            } catch (Exception e) {
                System.out.printf("Error retrieving price for %s: %s%n", product.getProductId(), e.getMessage());
            }
        }

        StringBuilder log = new StringBuilder();
        if (processedAlerts.isEmpty()) {
            log.append("Check finished");
        } else {
            for (AlertResponseDto alertResponseDto : processedAlerts)
                log.append("Price for ").append(alertResponseDto.productName()).append(" is finally below ").append(
                        alertResponseDto.targetPrice()).append(" at: ").append(alertResponseDto.storeName()).append(
                        " - ").append(alertResponseDto.finalPrice()).append("\n");
        }

        return log.toString();
    }

    AlertResponseDto createDtoFromAlert(Alert alert, String storeName, Double finalPrice) {
        return new AlertResponseDto(storeName, finalPrice, alert.getProduct().getName(), alert.getTargetPrice());
    }
}
