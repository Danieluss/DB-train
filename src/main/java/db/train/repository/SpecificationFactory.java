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

    private static final Pattern pattern = Pattern.compile("(.+?)((__eq__)|(__like__)|(__gt__)|(__eqgt__)|(__lt__)|(__eqlt__)|(__null__)|(__nonnull__))(.*?),");

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
                                    switch(criteria.getOperation().toLowerCase()) {
                                        case "__gt__":
                                            return builder.greaterThan(
                                                    root.get(criteria.getKey()), criteria.getValue().toString());
                                        case "__lt__":
                                            return builder.lessThan(
                                                    root.get(criteria.getKey()), criteria.getValue().toString());
                                        case "__eqgt__":
                                            return builder.greaterThanOrEqualTo(
                                                    root.get(criteria.getKey()), criteria.getValue().toString());
                                        case "__eqlt__":
                                            return builder.lessThanOrEqualTo(
                                                    root.get(criteria.getKey()), criteria.getValue().toString());
                                        case "__like__":
                                            return builder.like(
                                                    root.get(criteria.getKey()).as(String.class), "%" + criteria.getValue().toString() + "%");
                                        case "__eq__":
                                            return builder.equal(root.get(criteria.getKey()), criteria.getValue());
                                        case "__null__":
                                            return builder.isNull(root.get(criteria.getKey()));
                                        case "__nonnull__":
                                            return builder.isNotNull(root.get(criteria.getKey()));
                                    }
                                    return null;
                                } )
                                .toArray(Predicate[]::new)
                );
    }


}
