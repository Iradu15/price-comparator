package com.price_comparator.price_comparator.Service.Impl;

import com.price_comparator.price_comparator.DTO.DiscountDto;
import com.price_comparator.price_comparator.DTO.ProductDto;
import com.price_comparator.price_comparator.Service.CsvService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class CsvServiceImpl implements CsvService {
    public List<ProductDto> parseProductCSV(MultipartFile file) {
        /*
        Parse CSV file with products from a specific store
        */
        List<ProductDto> retrievedProductDtos = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            reader.readLine(); // skip first line

            while ((line = reader.readLine()) != null) {
                String[] components = line.split(";");
                if (components.length < 8) continue; // skip invalid lines

                ProductDto productDto = new ProductDto(components[0].trim(),
                        components[1].trim(),
                        components[2].trim(),
                        components[3].trim(),
                        Double.parseDouble(components[4].trim()),
                        components[5].trim(),
                        Double.parseDouble(components[6].trim()),
                        components[7].trim());
                retrievedProductDtos.add(productDto);
            }

        } catch (Exception e) {
            System.out.println("Error parsing CSV file " + file.getOriginalFilename() + ": " + e);
        }

        return retrievedProductDtos;
    }

    public List<DiscountDto> parseDiscountCSV(MultipartFile file) {
        /*
        Parse CSV file with discounts from a specific store
        */
        List<DiscountDto> retrievedDiscountsDto = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            reader.readLine(); // skip first line

            while ((line = reader.readLine()) != null) {
                String[] components = line.split(";");
                if (components.length < 9) continue; // skip invalid lines

                DiscountDto discountDto = new DiscountDto(components[0].trim(),
                        components[1].trim(),
                        components[2].trim(),
                        Double.parseDouble(components[3].trim()),
                        components[4].trim(),
                        components[5].trim(),
                        LocalDate.parse(components[6].trim()),
                        LocalDate.parse(components[7].trim()),
                        Integer.parseInt(components[8].trim()));
                retrievedDiscountsDto.add(discountDto);
            }

        } catch (Exception e) {
            System.out.println("Error parsing CSV file " + file.getOriginalFilename() + ": " + e);
        }

        return retrievedDiscountsDto;
    }
}
