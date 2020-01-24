package db.train.repository;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.List;

public class SpecificationFactory {

    public static <T> Specification<T> containsTextInAttributes(String text, Class<T> clazz) {
        String finalText = "%" + text + "%";
        return (root, query, builder) ->
                builder.or(
                        root.getModel()
                                .getDeclaredSingularAttributes()
                                .stream()
                                .map(a -> builder.like(root.get(a.getName()).as(String.class), finalText))
                                .toArray(Predicate[]::new)
        );
    }

}
