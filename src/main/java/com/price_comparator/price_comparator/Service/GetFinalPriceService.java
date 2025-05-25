package com.price_comparator.price_comparator.Service;

import com.price_comparator.price_comparator.DTO.FinalPrice;
import org.apache.coyote.BadRequestException;

public interface GetFinalPriceService {
    /*
    Get the price of a product for a specified store
    */
    FinalPrice getFinalPriceForProduct(String productId, String storeName);

    /*
    Get the cheapest price from all stores having the specified product in stock
    */
    FinalPrice getFinalPriceForProductAllStores(String productId);
}
