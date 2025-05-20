package com.price_comparator.price_comparator.DTO;

import java.time.LocalDate;

public record DiscountDto(
        String productId,
        String productName,
        String brand,
        Double packageQuantity,
        String packageUnit,
        String productCategory,
        LocalDate fromDate,
        LocalDate toDate,
        Integer percentageOfDiscount
) { }
