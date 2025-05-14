package com.price_comparator.price_comparator.Repository;

import com.price_comparator.price_comparator.Model.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
//    Optional<Store> findByNameIgnoreCase(String name);
}
