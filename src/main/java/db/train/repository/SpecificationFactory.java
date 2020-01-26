package db.train.repository;

import db.train.persistence.model.Connection;
import db.train.persistence.model.Station;
import db.train.persistence.model.join.StationsConnections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.metamodel.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
//TODO refactor - add wildcards, common method for search, optimization
public class SpecificationFactory {

    private EntityManager entityManager;

    @Autowired
    public SpecificationFactory(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    private static final Pattern pattern = Pattern.compile("(?<key>[^,_]+?)(?<operator>__eq__|__like__|__gt__|__eqgt__|__lt__|__eqlt__|__null__|__nonnull__)(?<value>[^,_]*?),");

    public <T> Specification<T> search(String text, Class<T> clazz) {
        String finalText = "%" + text + "%";
        return (root, query, builder) -> {
            List<Predicate> predicates = root.getModel()
                    .getSingularAttributes()
                    .stream()
                    .filter(a -> a.getType().getJavaType().getAnnotation(Entity.class) == null)
                    .map(a -> builder.like(root.get(a.getName()).as(String.class), finalText))
                    .collect(Collectors.toList());
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

    public <T> Specification<T> deepSearch(String string, Class<T> clazz, Integer depth) {
        String finalText = "%" + string + "%";
        return (root, query, builder) -> {
            query.distinct(true);
            Set<EntityType> joinedTypes = new HashSet<>();
            List<Pair<Attribute, EntityType>> toJoin = new ArrayList<>();
            List<Pair<Attribute, EntityType>> toJoinNew = new ArrayList<>();
            List<Pair<Join, Set<SingularAttribute>>> joins = new ArrayList<>();
            root.getModel()
                    .getSingularAttributes()
                    .stream()
                    .filter(a -> a.getType().getJavaType().getAnnotation(Entity.class) != null)
                    .forEach(a -> toJoin.add(Pair.of(a, entityManager.getMetamodel().entity(a.getType().getJavaType()))));
            root.getModel()
                    .getPluralAttributes()
                    .stream()
                    .filter(a -> a.getElementType().getJavaType().getAnnotation(Entity.class) != null)
                    .forEach(a -> toJoin.add(Pair.of(a, entityManager.getMetamodel().entity(a.getElementType().getJavaType()))));
            for(int i = 0; i < depth; i++) {
                for (var pair : toJoin) {
                    if(!joinedTypes.contains(pair.getSecond())) {
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
                        joinedTypes.add(pair.getSecond());
                        joins.add(Pair.of(join, pair.getSecond().getSingularAttributes()));
                        pair.getSecond()
                                .getSingularAttributes()
                                .stream()
                                .filter(a -> ((SingularAttribute) a).getType().getJavaType().getAnnotation(Entity.class) != null)
                                .forEach(a -> toJoinNew.add(Pair.of(((SingularAttribute) a), entityManager.getMetamodel().entity(((SingularAttribute) a).getType().getJavaType()))));
                        pair.getSecond()
                                .getPluralAttributes()
                                .stream()
                                .filter(a -> ((PluralAttribute) a).getElementType().getJavaType().getAnnotation(Entity.class) != null)
                                .forEach(a -> toJoinNew.add(Pair.of(((PluralAttribute) a), entityManager.getMetamodel().entity(((PluralAttribute) a).getElementType().getJavaType()))));
                    }
                }
                toJoin.clear();
                toJoin.addAll(List.copyOf(toJoinNew));
                toJoinNew.clear();
            }
            List<Predicate> predicates = root.getModel()
                    .getSingularAttributes()
                    .stream()
                    .filter(a -> a.getType().getJavaType().getAnnotation(Entity.class) == null)
                    .map(a -> builder.like(root.get(a.getName()).as(String.class), finalText))
                    .collect(Collectors.toList());
            joins.stream().forEach(
                    joinSetPair -> joinSetPair.getSecond()
                            .stream()
                            .filter(a -> a.getType().getJavaType().getAnnotation(Entity.class) == null)
                            .forEach(a -> predicates.add(builder.like(joinSetPair.getFirst().get(a.getName()).as(String.class), finalText)))
            );

            return builder.or(predicates.toArray(Predicate[]::new));
        };
    }

}
