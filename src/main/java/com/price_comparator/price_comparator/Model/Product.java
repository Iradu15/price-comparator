package com.price_comparator.price_comparator.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name="products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productId; // String value for the id

    private String name;

    private String category;

    private String brand;

    private Double packageQuantity;

    private String packageUnit;

    public Product(String productId, String name, String category, String brand, Double packageQuantity, String packageUnit) {
        this.productId = productId;
        this.name = name;
        this.category = category;
        this.brand = brand;
        this.packageQuantity = packageQuantity;
        this.packageUnit = packageUnit;
    }
}
