package com.price_comparator.price_comparator.Service;

import com.price_comparator.price_comparator.DTO.DiscountDto;
import com.price_comparator.price_comparator.DTO.ProductDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CsvService {
    List<ProductDto> parseProductCSV(MultipartFile file);

    List<DiscountDto> parseDiscountCSV(MultipartFile file);
}
