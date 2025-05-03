package com.bcb.bcb.specification;

import com.bcb.bcb.entity.Message;
import com.bcb.bcb.specification.builder.SpecSearchCriteria;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

@AllArgsConstructor
public class MessageSpecification implements Specification<Message> {

    private SpecSearchCriteria criteria;

    @Override
    public Predicate toPredicate(Root<Message> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        switch (criteria.getOperation()) {
            case EQUALITY : {
                return builder.equal(root.get(criteria.getKey()), criteria.getValue());
            }
            case NEGATION: {
                return builder.notEqual(root.get(criteria.getKey()), criteria.getValue());
            }
            case GREATER_THAN: {
                return builder.greaterThan(root.get(criteria.getKey()), criteria.getValue().toString());
            }
            case LESS_THAN: {
                return builder.lessThan(root.get(criteria.getKey()), criteria.getValue().toString());
            }
            case CONTAINS: {
                return builder.like(root.get(criteria.getKey()), "%" + criteria.getValue() + "%");
            }
            default: return null;
        }
    }
}
