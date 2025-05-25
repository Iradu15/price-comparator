package com.price_comparator.price_comparator.Service.Impl;

import com.price_comparator.price_comparator.Controller.CurrentDateController;
import com.price_comparator.price_comparator.Model.ProductPrice;
import com.price_comparator.price_comparator.Repository.ProductPriceRepository;
import com.price_comparator.price_comparator.Service.NextDayUpdateProductPricesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class NextDayUpdateProductPricesServiceImpl implements NextDayUpdateProductPricesService {

    @Autowired
    ProductPriceRepository productPriceRepository;

    @Autowired
    CurrentDateController currentDateController;

    @Override
    public void updateProductPrice() {
        /*
        Apply the new productPrices that start from today:
        - set endDate for the currentMapping to today - 1 day
        */
        LocalDate currentDate = LocalDate.parse(currentDateController.getCurrentDate());
        if (productPriceRepository.findAllCurrentPricesThatNeedToBeReplaced(currentDate).isEmpty()) return;

        List<ProductPrice> pricesThatNeedToBeReplaced = productPriceRepository.findAllCurrentPricesThatNeedToBeReplaced(
                currentDate).get();

        for (ProductPrice productPrice : pricesThatNeedToBeReplaced)
            productPrice.setEndDate(currentDate.minusDays(1));

        productPriceRepository.saveAll(pricesThatNeedToBeReplaced);
        System.out.printf("Updated %d prices (product-store price mappings)", pricesThatNeedToBeReplaced.size());
    }
}
