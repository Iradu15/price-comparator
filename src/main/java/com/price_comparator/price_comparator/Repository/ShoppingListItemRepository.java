package com.price_comparator.price_comparator.Repository;

import com.price_comparator.price_comparator.Model.ShoppingListItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingListItemRepository extends JpaRepository<ShoppingListItem, Long> {
}
