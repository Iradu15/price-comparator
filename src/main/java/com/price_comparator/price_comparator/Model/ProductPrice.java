package com.price_comparator.price_comparator.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@Getter
@Setter
@Table(
        uniqueConstraints = @UniqueConstraint(columnNames = {"product_id", "store_id", "startDate"}),
        name="product_prices"
)
public class ProductPrice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "productId")
    private Product product;

    @ManyToOne
    private Store store;

    private String currency;

    private Double price;

    private LocalDate startDate;

    private LocalDate endDate;

    public ProductPrice(){}

    public ProductPrice(Product product, Store store, String currency, Double price, LocalDate startDate, LocalDate endDate) {
        this.product = product;
        this.store = store;
        this.currency = currency;
        this.price = price;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
