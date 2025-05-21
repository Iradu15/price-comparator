package com.price_comparator.price_comparator.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "discounts",
        uniqueConstraints = @UniqueConstraint(columnNames = {
                "product_id", "store_id", "percentage_of_discount", "from_date", "to_date"
        })
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

    @Column(name = "percentage_of_discount")
    @Setter
    private Integer percentageOfDiscount;

    @Column(name = "from_date")
    @Setter
    private LocalDate fromDate;

    @Column(name = "to_date")
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
