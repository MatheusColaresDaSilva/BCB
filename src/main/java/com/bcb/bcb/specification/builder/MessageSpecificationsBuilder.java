package com.bcb.bcb.specification.builder;

import com.bcb.bcb.entity.Message;
import com.bcb.bcb.specification.MessageSpecification;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageSpecificationsBuilder {
    private final List<SpecSearchCriteria> params = new ArrayList<>();

    public MessageSpecificationsBuilder with(String search) {
        Pattern pattern = Pattern.compile("(\\w+)(==|!=|>=|<=|>|<|%)([^,]+)");
        Matcher matcher = pattern.matcher(search);

        while (matcher.find()) {
            String key = matcher.group(1);
            String operation= matcher.group(2);
            String value = matcher.group(3);

            SearchOperation op = SearchOperation.fromString(operation);
            if (op != null) {
                params.add(new SpecSearchCriteria(key, op, value));
            }
        }
        return this;
    }

    public Specification<Message> build() {
        if (params.isEmpty()) return null;

        Specification<Message> result = new MessageSpecification(params.get(0));

        for (int i = 1; i < params.size(); i++) {
            result = Specification.where(result).and(new MessageSpecification(params.get(i)));
        }

        return result;
    }
}
