package com.price_comparator.price_comparator.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "shopping_lists")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "shoppingList", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<ShoppingListItem> shoppingListItems;

    public ShoppingList(List<ShoppingListItem> shoppingListItems) {
        this.shoppingListItems = shoppingListItems;
    }

    @Override
    public String toString() {
        return "ShoppingList{" + "id=" + id + ", shoppingListItems=\n" + shoppingListItems + "}\n";
    }
}
