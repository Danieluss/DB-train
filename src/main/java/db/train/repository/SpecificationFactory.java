package db.train.repository;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.util.Pair;
import org.springframework.http.converter.json.GsonBuilderUtils;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.w3c.dom.ls.LSOutput;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.metamodel.*;
import javax.swing.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class SpecificationFactory {

    private EntityManager entityManager;

    @Autowired
    public SpecificationFactory(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    private static final Pattern pattern = Pattern.compile("(?<key>[^,_]+?)(?<operator>__eq__|__like__|__gt__|__eqgt__|__lt__|__eqlt__|__null__|__nonnull__)(?<value>[^,_]*?),");

    public <T> Specification<T> containsTextInAttributes(String text, Class<T> clazz) {
        String finalText = "%" + text + "%";
        return (root, query, builder) -> {
            List<Pair<Attribute, EntityType>> toJoin = new ArrayList<>();
            List<SingularAttribute> attributesToSearch = new ArrayList<>();
            List<Pair<Join, Set<SingularAttribute>>> joins = new ArrayList<>();
            root.getModel()
                    .getSingularAttributes()
                    .stream()
                    .filter(a -> a.getType().getJavaType().getAnnotation(Entity.class) != null)
                    .forEach(a -> {
                        System.out.println(a.getName());
                        toJoin.add(Pair.of(a, entityManager.getMetamodel().entity(a.getType().getJavaType())));
                    });
            root.getModel()
                    .getPluralAttributes()
                    .stream()
                    .filter(a -> a.getElementType().getJavaType().getAnnotation(Entity.class) != null)
                    .forEach(a -> {
                        System.out.println(a.getName());
                        toJoin.add(Pair.of(a, entityManager.getMetamodel().entity(a.getElementType().getJavaType())));
                    });
            for(var pair : toJoin) {
                Join join = null;
                if (pair.getFirst() instanceof SingularAttribute) {
                    join = root.join((SingularAttribute) pair.getFirst(), JoinType.LEFT);
                } else if (pair.getFirst() instanceof ListAttribute) {
                    join = root.join((ListAttribute) pair.getFirst(), JoinType.LEFT);
                } else if (pair.getFirst() instanceof CollectionAttribute) {
                    join = root.join((CollectionAttribute) pair.getFirst(), JoinType.LEFT);
                } else if (pair.getFirst() instanceof SetAttribute) {
                    join = root.join((SetAttribute) pair.getFirst(), JoinType.LEFT);
                } else if (pair.getFirst() instanceof MapAttribute) {
                    join = root.join((MapAttribute) pair.getFirst(), JoinType.LEFT);
                }
                joins.add(Pair.of(join, pair.getSecond().getSingularAttributes()));
            }
            List<Predicate> predicates = root.getModel()
                    .getSingularAttributes()
                    .stream()
                    .filter(a -> a.getType().getJavaType().getAnnotation(Entity.class) == null)
                    .map(a -> builder.like(root.get(a.getName()).as(String.class), finalText))
                    .collect(Collectors.toList());
            joins.stream().forEach(
                    joinSetPair -> {
                        joinSetPair.getSecond()
                                .stream()
                                .filter(attribute -> attribute.isId() || attribute.getName().toLowerCase().contains("id"))
                                .forEach(a -> {
                                    System.out.println(a.getName());
                                    System.out.println(finalText);
                                    predicates.add(builder.like(joinSetPair.getFirst().get(a.getName()).as(String.class), finalText));
                                });
                    }
            );
            return builder.or(predicates.toArray(Predicate[]::new));
        };
    }

    public <T> Specification<T> filterQuery(String queryString, Class<T> clazz) {
        Matcher matcher = pattern.matcher(queryString + ",");
        List<FilterCriteria> criterias = new ArrayList<>();
        while(matcher.find()) {
            criterias.add(new FilterCriteria(matcher.group("key"), matcher.group("operator"), matcher.group("value")));
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
