package com.price_comparator.price_comparator.Service;

import com.price_comparator.price_comparator.DTO.FinalPrice;

public interface GetFinalPriceService {
    FinalPrice getFinalPriceForProduct(String productId, String storeName);
}
