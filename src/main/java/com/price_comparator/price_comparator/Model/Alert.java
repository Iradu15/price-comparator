package com.price_comparator.price_comparator.Model;

import com.price_comparator.price_comparator.Controller.CurrentDateController;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

@Entity
@NoArgsConstructor // Generates a no-args constructor.
@AllArgsConstructor // Generates a constructor with all arguments.
@Table(name="alerts")
@Getter
public class Alert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "productId")
    private Product product;

    private Double targetPrice;

    private LocalDate createdAt;

    public Alert(Product product, Double targetPrice, LocalDate createdAt) {
        this.product = product;
        this.targetPrice = targetPrice;
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Alert{" + "id=" + id + ", product=" + product + ", targetPrice=" + targetPrice + ", createdAt=" + createdAt + "}\n";
    }
}
