package com.price_comparator.price_comparator.Repository;

import com.price_comparator.price_comparator.Model.Product;
import com.price_comparator.price_comparator.Model.ProductPrice;
import com.price_comparator.price_comparator.Model.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductPriceRepository extends JpaRepository<ProductPrice, Long> {
    Optional<List<ProductPrice>> findByProduct_ProductIdAndStore_Name(String productProductId, String storeName);

    @Query("""
    SELECT p FROM ProductPrice p
    WHERE p.product = :product
      AND p.store = :store
      AND p.endDate IS NULL
      AND p.startDate <= :currentDate
    """)
    Optional<ProductPrice> findCurrentPrice(@Param("product") Product product,
                                            @Param("store") Store store,
                                            @Param("currentDate") LocalDate currentDate
    );


    @Query(nativeQuery = true, value = """
    SELECT p.* FROM product_prices p
    JOIN product_prices pp ON
        p.product_id = pp.product_id AND
        p.store_id = pp.store_id
    WHERE p.end_date IS NULL
      AND p.start_date < :currentDate
      AND pp.start_date = :currentDate
    """)
    Optional<List<ProductPrice>> findAllCurrentPricesThatNeedToBeReplaced(@Param("currentDate") LocalDate currentDate);



    // overlapping means there are multiple updates planned for the same day
    @Query("SELECT p FROM ProductPrice p WHERE p.product = :product AND p.store = :store and p.startDate = :startDate")
    Optional<List<ProductPrice>> findOverlappingMappings(
            @Param("product") Product product,
            @Param("store") Store store,
            @Param("startDate") LocalDate startDate
    );
}

