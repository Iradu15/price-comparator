package com.price_comparator.price_comparator.DTO;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlertRequest {
    private String productId;
    private Double targetPrice;
}
