package com.price_comparator.price_comparator.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name="system_date")
public class CurrentDate {
    @Id
    private Long id = 1L;

    @Setter
    @Getter
    private LocalDate currentDay;

    public CurrentDate() {
    }

}