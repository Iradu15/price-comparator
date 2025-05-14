package com.price_comparator.price_comparator.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name="productPrices")
public class ProductPrice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Product product;

    @ManyToOne
    private Store store;

    private String currency;

    private Double price;

    private LocalDate startDate;

    private LocalDate endDate;

    public ProductPrice(Product product, Store store, String currency, Double price, LocalDate startDate, LocalDate endDate) {
        this.product = product;
        this.store = store;
        this.currency = currency;
        this.price = price;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
