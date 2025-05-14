package com.price_comparator.price_comparator.DTO;

public record ProductDto(
        String productId,
        String productName,
        String productCategory,
        String brand,
        Double packageQuantity,
        String packageUnit,
        Double price,
        String currency
) { }
