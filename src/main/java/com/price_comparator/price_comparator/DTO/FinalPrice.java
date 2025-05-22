package com.price_comparator.price_comparator.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Override
    public String toString() {
        return "FinalPrice{" + "productId='" + productId + '\'' + ", productName='" + productName + '\'' + ", brand='" + brand + '\'' + ", packageQuantity=" + packageQuantity + ", packageUnit='" + packageUnit + '\'' + ", productCategory='" + productCategory + '\'' + ", storeName='" + storeName + '\'' + ", finalPrice=" + finalPrice + ", pricePerUnit=" + pricePerUnit + "}\n";
    }
}

