package com.price_comparator.price_comparator.Service;

import com.price_comparator.price_comparator.DTO.FinalPrice;
import com.price_comparator.price_comparator.Enum.StandardUnits;

import java.util.List;

public interface BestPricePerUnitService {
    List<FinalPrice> getBestProductsPerUnit(int size);
    FinalPrice standardizePrice(FinalPrice price);

    static boolean isStandardUnit(String unit) {
        /*
        Check if unit measure is already standard
        */
        for (StandardUnits s : StandardUnits.values()) {
            if (s.name().equals(unit)) {
                return true;
            }
        }

        return false;
    }
}
