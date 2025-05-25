package com.price_comparator.price_comparator.Service.Impl;

import com.price_comparator.price_comparator.Controller.CurrentDateController;
import com.price_comparator.price_comparator.DTO.FinalPrice;
import com.price_comparator.price_comparator.Model.Discount;
import com.price_comparator.price_comparator.Model.Product;
import com.price_comparator.price_comparator.Model.ProductPrice;
import com.price_comparator.price_comparator.Model.Store;
import com.price_comparator.price_comparator.Repository.DiscountsRepository;
import com.price_comparator.price_comparator.Repository.ProductPriceRepository;
import com.price_comparator.price_comparator.Repository.ProductRepository;
import com.price_comparator.price_comparator.Repository.StoreRepository;
import com.price_comparator.price_comparator.Service.GetFinalPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class GetFinalPriceServiceImpl implements GetFinalPriceService {

    @Autowired
    DiscountsRepository discountsRepository;

    @Autowired
    ProductPriceRepository productPriceRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    StoreRepository storeRepository;

    @Autowired
    CurrentDateController currentDateController;

    @Override
    public FinalPrice getFinalPriceForProduct(String productId, String storeName) {
        LocalDate currentDate = LocalDate.parse(currentDateController.getCurrentDate());

        Product product = productRepository.findByProductId(productId).orElseThrow(() -> new IllegalArgumentException(
                "Product with " + productId + " not found\n"));

        Store store = storeRepository.findByNameIgnoreCase(storeName).orElseThrow(() -> new IllegalArgumentException(
                "Store with " + storeName + " not found\n"));

        ProductPrice currentMapping = productPriceRepository.findCurrentPrice(product, store, currentDate)
                .orElseThrow(() -> new IllegalArgumentException("ProductPrice for "
                                                                + productId
                                                                + " - "
                                                                + storeName
                                                                + "does not exist"));

        int percentageOfDiscount = discountsRepository.findActiveDiscount(store, product, currentDate)
                .map(Discount::getPercentageOfDiscount)
                .orElse(0);

        Double finalPrice = currentMapping.getPrice() - (percentageOfDiscount / 100.0) * currentMapping.getPrice();

        return new FinalPrice(productId,
                product.getName(),
                product.getBrand(),
                product.getPackageQuantity(),
                product.getPackageUnit(),
                product.getCategory(),
                storeName,
                finalPrice,
                -1.0
                // this is temporary until is calculated later
        );
    }

    @Override
    public FinalPrice getFinalPriceForProductAllStores(String productId) {

        LocalDate currentDate = LocalDate.parse(currentDateController.getCurrentDate());
        Product product = productRepository.findByProductId(productId).orElseThrow(() -> new IllegalArgumentException(
                "Product with " + productId + " does not exist"));

        Optional<List<Store>> storesHavingProduct = productPriceRepository.findStoresHavingProduct(product, currentDate)
                .filter(list -> !list.isEmpty());

        if (storesHavingProduct.isEmpty()) {
            String errMsg = String.format("In %s there are no stores having product %s%n",
                    currentDate,
                    product.getProductId());
            throw new IllegalArgumentException(errMsg);
        }


        Double minPrice = Double.MAX_VALUE;
        String storeName = "";

        for (Store store : storesHavingProduct.get()) {

            try {
                Double price = getFinalPriceForProduct(product.getProductId(), store.getName()).getFinalPrice();

                if (price <= minPrice) {
                    minPrice = price;
                    storeName = store.getName();
                }

            } catch (Exception e) {
                System.out.printf("Error retrieving price for %s within %s: %s%n",
                        product.getProductId(),
                        store.getName(),
                        e.getMessage());
            }
        }

        return new FinalPrice(productId,
                product.getName(),
                product.getBrand(),
                product.getPackageQuantity(),
                product.getPackageUnit(),
                product.getCategory(),
                storeName,
                minPrice,
                -1.0
                // this is temporary until is calculated later
        );
    }
}
