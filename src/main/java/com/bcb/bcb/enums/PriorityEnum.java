package com.bcb.bcb.enums;

import java.math.BigDecimal;

public enum PriorityEnum {
    NORMAL(1, new BigDecimal("0.25")),
    URGENT(0, new BigDecimal("0.50"));

    private final Integer weigth;
    private final BigDecimal cost;

    PriorityEnum(Integer weigth, BigDecimal cost) {
        this.weigth = weigth;
        this.cost = cost;
    }

    public Integer getWeigth() {
        return weigth;
    }

    public BigDecimal getCost() { return cost; }
}
