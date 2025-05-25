package com.price_comparator.price_comparator.Service;

import com.price_comparator.price_comparator.DTO.DiscountDto;
import com.price_comparator.price_comparator.Model.Discount;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;

public interface UpdateDiscountsService {
    void processCsvFile(MultipartFile file, String storeName, LocalDate date) throws IOException;

    Discount createDiscountFromDto(DiscountDto discountDto, String storeName);
}