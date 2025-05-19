package com.price_comparator.price_comparator.Repository;

import com.price_comparator.price_comparator.Model.CurrentDate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrentDateRepository extends JpaRepository<CurrentDate, Long> {
}
