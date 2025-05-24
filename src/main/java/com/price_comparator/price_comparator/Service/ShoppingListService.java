package com.price_comparator.price_comparator.Service;

import com.price_comparator.price_comparator.DTO.ShoppingListResponseDto;
import com.price_comparator.price_comparator.Model.ShoppingList;
import org.springframework.beans.factory.annotation.Autowired;

public interface ShoppingListService {
    ShoppingListResponseDto processShoppingList(ShoppingList shoppingList);
}
