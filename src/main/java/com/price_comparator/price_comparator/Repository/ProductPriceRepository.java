package com.price_comparator.price_comparator.Repository;

import com.price_comparator.price_comparator.Model.Product;
import com.price_comparator.price_comparator.Model.ProductPrice;
import com.price_comparator.price_comparator.Model.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductPriceRepository extends JpaRepository<ProductPrice, Long> {
    Optional<List<ProductPrice>> findByProduct_ProductIdAndStore_Name(String productProductId, String storeName);

    @Query("SELECT p FROM ProductPrice p WHERE p.product = :product AND p.store = :store AND p.endDate IS NULL")
    Optional<ProductPrice> findCurrentPrice(@Param("product") Product product, @Param("store") Store store);
}

