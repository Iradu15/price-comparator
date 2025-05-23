package com.price_comparator.price_comparator.DTO;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlertRequest {
    private String productId;
    private Double targetPrice;

    // Getters and Setters
    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Double getTargetPrice() {
        return targetPrice;
    }

    public void setTargetPrice(Double targetPrice) {
        this.targetPrice = targetPrice;
    }

}
