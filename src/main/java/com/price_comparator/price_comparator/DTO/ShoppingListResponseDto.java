package com.price_comparator.price_comparator.DTO;

import java.util.List;
import java.util.Objects;

public record ShoppingListResponseDto(
        List<ShoppingListItemDto> items,
        Double totalPrice
) {
    @Override
    public String toString() {
        return "ShoppingListResponseDto{" + "items=" + items + ", totalPrice=" + totalPrice + "}\n";
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ShoppingListResponseDto that = (ShoppingListResponseDto) o;
        return Objects.equals(totalPrice, that.totalPrice) && Objects.equals(items, that.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(items, totalPrice);
    }
}
