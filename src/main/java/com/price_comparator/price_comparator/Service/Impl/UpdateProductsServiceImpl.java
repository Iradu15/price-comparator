package com.price_comparator.price_comparator.Service.Impl;

import com.price_comparator.price_comparator.Model.Product;
import com.price_comparator.price_comparator.Model.ProductPrice;
import com.price_comparator.price_comparator.Model.Store;
import com.price_comparator.price_comparator.Service.UpdateProductsService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Service
public class UpdateProductsServiceImpl implements UpdateProductsService {

    @Override
    public void processCsvFile(MultipartFile file, String storeName, LocalDate date) {

    }

    @Override
    public void addNewProducts(List<Product> products, Store store, LocalDate startDate) {

    }

    @Override
    public void updatePrices(List<ProductPrice> priceUpdates, Store store, LocalDate fileDate) {

    }
}
