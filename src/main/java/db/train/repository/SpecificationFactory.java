package db.train.repository;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.Entity;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpecificationFactory {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class FilterCriteria {
        private String key;
        private String operation;
        private Object value;
    }

    private static final Pattern pattern = Pattern.compile("(\\w+?)([:<>\\[\\]_])(\\w+?),");

    public static <T> Specification<T> containsTextInAttributes(String text, Class<T> clazz) {
        String finalText = "%" + text + "%";
        return (root, query, builder) ->
                builder.or(
                        root.getModel()
                                .getSingularAttributes()
                                .stream()
                                .filter(a -> a.getType().getJavaType().getAnnotation(Entity.class) == null)
                                .map(a -> builder.like(root.get(a.getName()).as(String.class), finalText)                                )
                                .toArray(Predicate[]::new)
                );
    }

    public static <T> Specification<T> filterQuery(String queryString, Class<T> clazz) {
        Matcher matcher = pattern.matcher(queryString + ",");
        List<FilterCriteria> criterias = new ArrayList<>();
        while(matcher.find()) {
            criterias.add(new FilterCriteria(matcher.group(1), matcher.group(2), matcher.group(3)));
        }
        return (root, query, builder) ->
                builder.and(
                        criterias
                                .stream()
                                .map( criteria -> {
                                    if (criteria.getOperation().equalsIgnoreCase(">")) {
                                        return builder.greaterThan(
                                                root.get(criteria.getKey()), criteria.getValue().toString());
                                    } else if (criteria.getOperation().equalsIgnoreCase("<")) {
                                        return builder.lessThan(
                                                root.get(criteria.getKey()), criteria.getValue().toString());
                                    } else if (criteria.getOperation().equalsIgnoreCase("[")) {
                                        return builder.greaterThanOrEqualTo(
                                                root.get(criteria.getKey()), criteria.getValue().toString());
                                    } else if (criteria.getOperation().equalsIgnoreCase("]")) {
                                        return builder.lessThanOrEqualTo(
                                                root.get(criteria.getKey()), criteria.getValue().toString());
                                    } else if (criteria.getOperation().equalsIgnoreCase(":")) {
                                        if (root.get(criteria.getKey()).getJavaType() == String.class) {
                                            return builder.like(
                                                    root.get(criteria.getKey()), "%" + criteria.getValue() + "%");
                                        } else {
                                            return builder.equal(root.get(criteria.getKey()), criteria.getValue());
                                        }
                                    } else if (criteria.getOperation().equalsIgnoreCase("_")) {
                                        return builder.equal(root.get(criteria.getKey()), criteria.getValue());
                                    }
                                    return null;
                                } )
                                .toArray(Predicate[]::new)
                );
    }


}
