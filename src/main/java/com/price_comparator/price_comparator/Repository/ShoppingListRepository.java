package com.price_comparator.price_comparator.Repository;

import com.price_comparator.price_comparator.Model.ShoppingList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingListRepository extends JpaRepository<ShoppingList, Long> {
}
