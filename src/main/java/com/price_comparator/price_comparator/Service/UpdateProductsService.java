package com.price_comparator.price_comparator.Service;

import com.price_comparator.price_comparator.DTO.ProductDto;
import com.price_comparator.price_comparator.Model.ProductPrice;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;

public interface UpdateProductsService {
    void processCsvFile(MultipartFile file, String storeName, LocalDate date) throws IOException;

    //    Update existent productPrices
    void updatePrices(ProductPrice oldProductPrice, ProductDto productDto, String storeName, LocalDate date);
}


