package com.price_comparator.price_comparator.Service.Impl;

import com.price_comparator.price_comparator.DTO.FinalPrice;
import com.price_comparator.price_comparator.DTO.ShoppingListItemDto;
import com.price_comparator.price_comparator.DTO.ShoppingListResponseDto;
import com.price_comparator.price_comparator.Model.ShoppingList;
import com.price_comparator.price_comparator.Model.ShoppingListItem;
import com.price_comparator.price_comparator.Service.GetFinalPriceService;
import com.price_comparator.price_comparator.Service.ShoppingListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ShoppingListServiceImpl implements ShoppingListService {

    @Autowired
    GetFinalPriceService getFinalPriceService;

    @Override
    public ShoppingListResponseDto processShoppingList(ShoppingList shoppingList) {
        List<ShoppingListItem> items = shoppingList.getShoppingListItems();
        List<ShoppingListItemDto> itemResponses = new ArrayList<>();
        double totalPrice = 0.0;

        for (ShoppingListItem item : items){
            String productID = item.getProduct().getProductId();
            FinalPrice price;

            try {
                price = getFinalPriceService.getFinalPriceForProductAllStores(productID);
            } catch (Exception e){
                System.out.printf("Error retrieving price for %s: %s%n", productID, e.getMessage());
                continue;
            }

            double pricePerUnit = price.getFinalPrice();
            double priceOfAllUnits = pricePerUnit * item.getQuantity();

            totalPrice += priceOfAllUnits;

            ShoppingListItemDto itemResponse = new ShoppingListItemDto(
                    productID,
                    item.getQuantity(),
                    priceOfAllUnits,
                    price.getStoreName()
            );
            itemResponses.add(itemResponse);
        }

        return new ShoppingListResponseDto(itemResponses, totalPrice);
    }
}
