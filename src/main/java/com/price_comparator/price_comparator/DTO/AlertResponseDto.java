package com.price_comparator.price_comparator.DTO;

public record AlertResponseDto(
        String storeName,
        Double finalPrice,
        String productName,
        Double targetPrice
) { }
