package com.price_comparator.price_comparator.Enum;

import lombok.Getter;

@Getter
public enum AllMeasureUnits {
    L("l"),
    KG("kg"),
    BUC("buc"),
    G("g"),
    ML("ml"),
    ROLE("role");

    public final String value;

    AllMeasureUnits(String value) {
        this.value = value;
    }

    public static AllMeasureUnits fromValue(String value) {
        for (AllMeasureUnits unit : values()) {
            if (unit.getValue().equalsIgnoreCase(value)) {
                return unit;
            }
        }
        throw new IllegalArgumentException("Unknown unit: " + value);
    }

}