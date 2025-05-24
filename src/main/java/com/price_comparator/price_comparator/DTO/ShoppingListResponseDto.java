package com.price_comparator.price_comparator.DTO;

import java.util.List;

public record ShoppingListResponseDto(
        List<ShoppingListItemDto> items,
        Double totalPrice
) {
    @Override
    public String toString() {
        return "ShoppingListResponseDto{" + "items=" + items + ", totalPrice=" + totalPrice + "}\n";
    }
}
