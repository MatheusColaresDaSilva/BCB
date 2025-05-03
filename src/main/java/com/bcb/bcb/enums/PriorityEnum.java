package com.bcb.bcb.enums;

public enum PriorityEnum {
    NORMAL(1),
    URGENT(0);

    private final Integer weigth;

    PriorityEnum(Integer weigth) {
        this.weigth = weigth;
    }

    public Integer getWeigth() {
        return weigth;
    }
}
