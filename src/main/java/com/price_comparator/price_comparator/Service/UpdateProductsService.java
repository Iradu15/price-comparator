package com.price_comparator.price_comparator.Service;

import com.price_comparator.price_comparator.Model.Product;
import com.price_comparator.price_comparator.Model.ProductPrice;
import com.price_comparator.price_comparator.Model.Store;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface UpdateProductsService {
    void processCsvFile(MultipartFile file, String storeName, LocalDate date) throws IOException;

    /*
    Add new products for a specific store
    */
    void addNewProducts(List<Product> products);

    /*
    Add new productPrices for a specific store
    */
    public void addNewProductsPrices(List<ProductPrice> products);

    /*
    Update existent productPrices
    */
    void updatePrices(List<ProductPrice> priceUpdates, Store store, LocalDate fileDate);
}


