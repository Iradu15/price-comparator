package com.price_comparator.price_comparator.Controller;

import com.price_comparator.price_comparator.DTO.ProductDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CSVController {
    public static List<ProductDto> parseProductCSV(MultipartFile file) {
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
                    components[7].trim()
                );
                retrievedProductDtos.add(productDto);
            }

        } catch (Exception e){
            System.out.println("Error parsing CSV file " + file.getOriginalFilename() + ": " + e);
        }

        return retrievedProductDtos;
    }
}
