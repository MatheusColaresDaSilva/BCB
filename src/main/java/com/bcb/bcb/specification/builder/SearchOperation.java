package com.bcb.bcb.specification.builder;

public enum SearchOperation {
    EQUALITY("=="),
    NEGATION("!="),
    GREATER_THAN(">"),
    LESS_THAN("<"),
    GT_EQUAL(">="),
    LT_EQUAL("<="),
    CONTAINS("%");

    private final String operator;

    SearchOperation(String operator) {
        this.operator = operator;
    }

    public String getOperator() {
        return operator;
    }

    public static SearchOperation fromString(String input) {
        for (SearchOperation op : values()) {
            if (op.getOperator().equals(input)) {
                return op;
            }
        }
        throw new IllegalArgumentException("Invalid Operator: " + input);
    }
}
