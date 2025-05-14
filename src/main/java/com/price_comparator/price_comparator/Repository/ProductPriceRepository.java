package com.price_comparator.price_comparator.Repository;

import com.price_comparator.price_comparator.Model.ProductPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductPriceRepository extends JpaRepository<ProductPrice, Long> {
//    Optional<ProductPrice> findByProductIdAndStore_Name(String productProductId, String storeName); //
}

