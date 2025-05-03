package com.bcb.bcb.specification.builder;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class SpecSearchCriteria {
    private String key;
    private SearchOperation operation;
    private Object value;

}
