package com.price_comparator.price_comparator.Service;

import com.price_comparator.price_comparator.DTO.FinalPrice;

import java.util.List;

public interface BestPricePerUnitService {
    List<FinalPrice> getBestProductsPerUnit(int size);
    FinalPrice standardizePrice(FinalPrice price);
}
