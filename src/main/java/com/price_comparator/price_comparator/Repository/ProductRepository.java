package com.price_comparator.price_comparator.Repository;

import com.price_comparator.price_comparator.Model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
//    Optional<Product> findByProductId(String productId);
}

