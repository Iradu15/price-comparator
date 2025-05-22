package com.price_comparator.price_comparator.Service.Impl;

import com.price_comparator.price_comparator.Controller.CurrentDateController;
import com.price_comparator.price_comparator.Enum.AllMeasureUnits;
import com.price_comparator.price_comparator.Enum.StandardUnits;
import com.price_comparator.price_comparator.DTO.FinalPrice;
import com.price_comparator.price_comparator.Model.ProductPrice;
import com.price_comparator.price_comparator.Repository.ProductPriceRepository;
import com.price_comparator.price_comparator.Service.BestPricePerUnitService;
import com.price_comparator.price_comparator.Service.GetFinalPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class BestPricePerUnitServiceImpl implements BestPricePerUnitService {

    @Autowired
    GetFinalPriceService getFinalPriceService;

    @Autowired
    ProductPriceRepository productPriceRepository;

    @Autowired
    CurrentDateController currentDateController;

    @Override
    public List<FinalPrice> getBestProductsPerUnit(int size) {
        LocalDate currentDate = LocalDate.parse(currentDateController.getCurrentDate());
        List<ProductPrice> productPriceList = productPriceRepository.findAllCurrentProductPrices(currentDate).orElseThrow(() ->
                new RuntimeException("No ProductPrices available for the moment, add some first"));

        List<FinalPrice> bestProductsPerUnit = new ArrayList<>();

        for (ProductPrice productPrice : productPriceList){
            String productId = productPrice.getProduct().getProductId();
            String storeName = productPrice.getStore().getName();

            FinalPrice standardizedPrice = getFinalPriceService.getFinalPriceForProduct(productId, storeName);
            FinalPrice finalPrice = standardizePrice(standardizedPrice);

            bestProductsPerUnit.add(finalPrice);
        }

        bestProductsPerUnit.sort((a, b) -> a.getPricePerUnit().compareTo(b.getPricePerUnit()));
        return bestProductsPerUnit.subList(0, size);
    }

    @Override
    public FinalPrice standardizePrice(FinalPrice price) {
        String unit = price.getPackageUnit();

        if(isStandardUnit(unit)){
            Double pricePerUnit = price.getFinalPrice() / (Double) price.getPackageQuantity();
            price.setPricePerUnit(pricePerUnit);

            return price;
        }

        if(AllMeasureUnits.fromValue(unit).equals(AllMeasureUnits.G) || AllMeasureUnits.fromValue(unit).equals(AllMeasureUnits.ML)){
            price.setPricePerUnit(price.getFinalPrice() * 1000.0 / price.getPackageQuantity());
        }
        else{
            throw new IllegalArgumentException("Unit measure " + unit + " unknown"); 
        }

        return price;
    }

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
