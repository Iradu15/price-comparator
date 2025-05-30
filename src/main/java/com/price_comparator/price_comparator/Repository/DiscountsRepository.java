package com.price_comparator.price_comparator.Repository;

import com.price_comparator.price_comparator.Model.Discount;
import com.price_comparator.price_comparator.Model.Product;
import com.price_comparator.price_comparator.Model.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DiscountsRepository extends JpaRepository<Discount, Long> {
    @Query("SELECT d FROM Discount d WHERE d.store = :store AND d.product = :product AND d.fromDate <= :currentDate "
           + "AND d.toDate >= :currentDate")
    Optional<Discount> findActiveDiscount(@Param("store") Store store, @Param("product") Product product, @Param(
            "currentDate") LocalDate currentDate);

    @Query("SELECT d FROM Discount d WHERE d.fromDate <= :currentDate AND d.toDate >= :currentDate ORDER BY d"
           + ".percentageOfDiscount DESC LIMIT :size")
    Optional<List<Discount>> findTopActiveDiscounts(@Param("currentDate") LocalDate currentDate,
                                                    @Param("size") int size);

    @Query("SELECT d from Discount d WHERE d.store = :store AND d.product = :product AND d.fromDate = :from_date")
    Optional<List<Discount>> findAllByFromDate(@Param("store") Store store, @Param("product") Product product,
                                               @Param("from_date") LocalDate fromDate);

    @Query("""
            SELECT d from Discount d
            WHERE d.fromDate = :yesterday
                AND  d.toDate >= :currentDate
            """)
    Optional<List<Discount>> findAllAvailableLastDayDiscounts(@Param("currentDate") LocalDate currentDate, @Param(
            "yesterday") LocalDate yesterday);


    // overlapping means that there are discounts with:
    // existentDiscountFrom <= toBeInsertedDiscountFrom <= existentDiscountTo or (left overlap)
    // existentDiscountFrom <= toBeInsertedDiscountTo <= existentDiscountTo or (right overlap)
    // existentDiscountFrom <= toBeInsertedDiscountFrom && toBeInsertedDiscountTo <= existentDiscountTo (contains)
    // toBeInsertedDiscountFrom <= existentDiscountFrom && existentDiscountTo <= toBeInsertedDiscountTo (is contained)
    @Query("""
            SELECT d from Discount d
                WHERE d.store = :store AND d.product = :product
                    AND (
                        (:fromDate BETWEEN d.fromDate AND d.toDate)
                        OR (:toDate BETWEEN d.fromDate AND d.toDate)
                        OR (d.fromDate <= :fromDate AND :toDate <= d.toDate)
                        OR (:fromDate <= d.fromDate AND d.toDate <= :toDate)
                    )
            """)
    Optional<List<Discount>> findOverlappingDiscounts(@Param("store") Store store, @Param("product") Product product,
                                                      @Param("fromDate") LocalDate fromDate,
                                                      @Param("toDate") LocalDate toDate);
}
