package com.price_comparator.price_comparator.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

/*
Due to the high number of updates, class was preferred to record
*/

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FinalPrice {
    private String productId;
    private String productName;
    private String brand;
    private Double packageQuantity;
    private String packageUnit;
    private String productCategory;
    private String storeName;
    private Double finalPrice;
    private Double pricePerUnit;

    public FinalPrice(FinalPrice other) {
        this.productId = other.productId;
        this.productName = other.productName;
        this.brand = other.brand;
        this.packageQuantity = other.packageQuantity;
        this.packageUnit = other.packageUnit;
        this.productCategory = other.productCategory;
        this.storeName = other.storeName;
        this.finalPrice = other.finalPrice;
        this.pricePerUnit = other.pricePerUnit;
    }

    @Override
    public String toString() {
        return "FinalPrice{"
                + "productId='"
                + productId
                + '\''
                + ", productName='"
                + productName
                + '\''
                + ", brand='"
                + brand
                + '\''
                + ", packageQuantity="
                + packageQuantity
                + ", packageUnit='"
                + packageUnit
                + '\''
                + ", productCategory='"
                + productCategory
                + '\''
                + ", storeName='"
                + storeName
                + '\''
                + ", finalPrice="
                + finalPrice
                + ", pricePerUnit="
                + pricePerUnit
                + "}\n";
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        FinalPrice that = (FinalPrice) o;
        return Objects.equals(productId, that.productId)
                && Objects.equals(productName, that.productName)
                && Objects.equals(brand, that.brand)
                && Objects.equals(packageQuantity, that.packageQuantity)
                && Objects.equals(packageUnit, that.packageUnit)
                && Objects.equals(productCategory, that.productCategory)
                && Objects.equals(storeName, that.storeName)
                && Objects.equals(finalPrice, that.finalPrice)
                && Objects.equals(pricePerUnit, that.pricePerUnit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId,
                productName,
                brand,
                packageQuantity,
                packageUnit,
                productCategory,
                storeName,
                finalPrice,
                pricePerUnit);
    }
}

