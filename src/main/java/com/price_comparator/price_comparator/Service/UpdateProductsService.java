package com.price_comparator.price_comparator.Service;

import com.price_comparator.price_comparator.Model.Product;
import com.price_comparator.price_comparator.Model.ProductPrice;
import com.price_comparator.price_comparator.Model.Store;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

public interface UpdateProductsService {
    void processCsvFile(MultipartFile file, String storeName, LocalDate date);

    /*
    Add new products for a specific store with corresponding productPrices
    */
    void addNewProducts(List<Product> products, Store store, LocalDate startDate);

    /*
    Update existent productPrices
    */
    void updatePrices(List<ProductPrice> priceUpdates, Store store, LocalDate fileDate);

}


