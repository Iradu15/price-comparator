package com.price_comparator.price_compparator.Models;

import jakarta.persistence.*;

@Entity
@Table(name="products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String productId;

    private String name;

    private String category;

    private String brand;

    private Double packageQuantity;

    private String packageUnit;

    public Product() {
    }

    public Product(Integer id, String productId, String name, String category, String brand, Double packageQuantity, String packageUnit) {
        this.id = id;
        this.productId = productId;
        this.name = name;
        this.category = category;
        this.brand = brand;
        this.packageQuantity = packageQuantity;
        this.packageUnit = packageUnit;
    }
}
