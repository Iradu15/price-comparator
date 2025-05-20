package com.price_comparator.price_comparator.Model;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(
        uniqueConstraints = @UniqueConstraint(columnNames = {"product_id", "store", "percentageOfDiscount", "fromDate", "toDate"}),
        name="discounts"
)
@Getter
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "productId")
    private Product product;

    @ManyToOne
    private Store store;

    @Setter
    private Integer percentageOfDiscount;

    @Setter
    private LocalDate fromDate;

    @Setter
    private LocalDate toDate;

    public Discount(Product product, Store store, Integer percentageOfDiscount, LocalDate fromDate, LocalDate toDate) {
        this.product = product;
        this.store = store;
        this.percentageOfDiscount = percentageOfDiscount;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    @Override
    public String toString() {
        return "Discount{" +
                "product=" + product +
                ", store=" + store +
                ", percentageOfDiscount=" + percentageOfDiscount +
                ", fromDate=" + fromDate +
                ", toDate=" + toDate +
                '}';
    }
}
