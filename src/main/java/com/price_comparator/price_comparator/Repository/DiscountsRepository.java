package com.price_comparator.price_comparator.Repository;

import com.price_comparator.price_comparator.Model.Discount;
import com.price_comparator.price_comparator.Model.Product;
import com.price_comparator.price_comparator.Model.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface DiscountsRepository extends JpaRepository<Discount, Long> {
    @Query("SELECT d FROM Discount d WHERE d.store = :store AND d.product = :product AND d.toDate IS null")
    Optional<Discount> findCurrentDiscount(@Param("store") Store store, @Param("product")Product product);

    Optional<Discount> findDiscountByFromDate(LocalDate fromDate);
}
