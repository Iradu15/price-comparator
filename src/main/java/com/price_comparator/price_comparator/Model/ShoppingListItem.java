package com.price_comparator.price_comparator.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="shopping_list_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingListItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "productId")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "shopping_list_id")
    private ShoppingList shoppingList;

    private Integer quantity;

    public ShoppingListItem(Product product, ShoppingList shoppingList, Integer quantity) {
        this.product = product;
        this.shoppingList = shoppingList;
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "ShoppingListItem{" + "product=" + product + ", quantity=" + quantity + "}\n";
    }
}
