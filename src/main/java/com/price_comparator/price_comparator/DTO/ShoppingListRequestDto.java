package com.price_comparator.price_comparator.DTO;

import java.util.List;

public record ShoppingListRequestDto(
    List<ShoppingListItemDto> items
) { }
