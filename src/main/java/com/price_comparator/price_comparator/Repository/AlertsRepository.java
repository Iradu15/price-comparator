package com.price_comparator.price_comparator.Repository;

import com.price_comparator.price_comparator.Model.Alert;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlertsRepository extends JpaRepository<Alert, Long> {
}
