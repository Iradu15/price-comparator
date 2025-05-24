package com.price_comparator.price_comparator.DTO;

public record ShoppingListItemDto(
        String productId,
        int quantity,
        Double price,
        String cheapestStore
) {
    public ShoppingListItemDto(String productId, int quantity) {
        this(productId, quantity, 0.0, "");
    }

    public ShoppingListItemDto {
    }
}
