package com.price_comparator.price_compparator.Repositories;

import com.price_comparator.price_compparator.Models.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface ProductRepository extends CrudRepository<Product, Integer> {
}

