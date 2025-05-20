package com.price_comparator.price_comparator.Service.Impl;

import com.price_comparator.price_comparator.Controller.CSVController;
import com.price_comparator.price_comparator.DTO.DiscountDto;
import com.price_comparator.price_comparator.Model.Discount;
import com.price_comparator.price_comparator.Model.Product;
import com.price_comparator.price_comparator.Model.Store;
import com.price_comparator.price_comparator.Repository.DiscountsRepository;
import com.price_comparator.price_comparator.Repository.ProductPriceRepository;
import com.price_comparator.price_comparator.Repository.ProductRepository;
import com.price_comparator.price_comparator.Repository.StoreRepository;
import com.price_comparator.price_comparator.Service.UpdateDiscountsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Service
public class UpdateDiscountsServiceImpl implements UpdateDiscountsService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductPriceRepository productPriceRepository;

    @Autowired
    DiscountsRepository discountsRepository;

    @Autowired
    StoreRepository storeRepository;

    @Override
    public void processCsvFile(MultipartFile file, String storeName, LocalDate date) {
        List<DiscountDto> retrievedDiscounts = CSVController.parseDiscountCSV(file);
        List<Discount> discountsToBeAdded = new ArrayList<>();

         storeRepository.findByNameIgnoreCase(storeName)
                .orElseThrow(() -> new IllegalArgumentException("Store with name " + storeName + " not found"));

        for(DiscountDto discountDto: retrievedDiscounts) {

            productRepository.findByProductId(discountDto.productId()).orElseThrow(() -> new IllegalArgumentException
                    ("Product with ID" + discountDto.productId() + " not found, cannot apply discount\""));

            productPriceRepository.findByProduct_ProductIdAndStore_Name(discountDto.productId(), storeName)
                    .orElseThrow(() -> new IllegalStateException("Mapping between product ID " + discountDto.productId()
                            + " and store " + storeName + " not found"));

            Discount discount = createDiscountFromDto(discountDto, storeName);
            discountsToBeAdded.add(discount);
        }

        discountsRepository.saveAll(discountsToBeAdded);
        System.out.printf("Saved %d new discounts%n", discountsToBeAdded.size());
    }

    public Discount createDiscountFromDto(DiscountDto discountDto, String storeName){
        // these checks are done above in main method, not needed here
        Store store = storeRepository.findByNameIgnoreCase(storeName).get();
        Product product = productRepository.findByProductId(discountDto.productId()).get();

        return new Discount(product, store, discountDto.percentageOfDiscount(), discountDto.fromDate(), discountDto.toDate());
    }

}
