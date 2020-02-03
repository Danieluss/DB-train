package db.train.repository;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.criteria.From;
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

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    private static class FromAttributeEntity {
        private From from;
        private Attribute attribute;
        private EntityType entityType;
    }

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
            List<FromAttributeEntity> toJoin = new ArrayList<>();
            List<FromAttributeEntity> toJoinNew = new ArrayList<>();
            List<Pair<From, Set<SingularAttribute>>> joins = new ArrayList<>();
            joinedTypes.add(entityManager.getMetamodel().entity(clazz));
            root.getModel()
                    .getSingularAttributes()
                    .stream()
                    .filter(a -> a.getType().getJavaType().getAnnotation(Entity.class) != null)
                    .forEach(a -> toJoin.add(new FromAttributeEntity(root, a, entityManager.getMetamodel().entity(a.getType().getJavaType()))));
            root.getModel()
                    .getPluralAttributes()
                    .stream()
                    .filter(a -> a.getElementType().getJavaType().getAnnotation(Entity.class) != null)
                    .forEach(a -> toJoin.add(new FromAttributeEntity(root, a, entityManager.getMetamodel().entity(a.getElementType().getJavaType()))));
            for(int i = 0; i < depth; i++) {
                for (var triple : toJoin) {
                    if(!joinedTypes.contains(triple.getEntityType())) {
                        Join join = null;
                        Attribute attribute = triple.getAttribute();
                        From from = triple.getFrom();
                        EntityType entityType = triple.getEntityType();
                        if (attribute instanceof SingularAttribute) {
                            join = from.join((SingularAttribute) attribute, JoinType.LEFT);
                        } else if (attribute instanceof ListAttribute) {
                            join = from.join((ListAttribute) attribute, JoinType.LEFT);
                        } else if (attribute instanceof CollectionAttribute) {
                            join = from.join((CollectionAttribute) attribute, JoinType.LEFT);
                        } else if (attribute instanceof SetAttribute) {
                            join = from.join((SetAttribute) attribute, JoinType.LEFT);
                        } else if (attribute instanceof MapAttribute) {
                            join = from.join((MapAttribute) attribute, JoinType.LEFT);
                        }
                        joinedTypes.add(entityType);
                        joins.add(Pair.of(join, entityType.getSingularAttributes()));
                        Join finalJoin = join;
                        entityType
                                .getSingularAttributes()
                                .stream()
                                .filter(a -> ((SingularAttribute) a).getType().getJavaType().getAnnotation(Entity.class) != null)
                                .forEach(a -> toJoinNew.add(new FromAttributeEntity(finalJoin, ((SingularAttribute) a), entityManager.getMetamodel().entity(((SingularAttribute) a).getType().getJavaType()))));
                        entityType
                                .getPluralAttributes()
                                .stream()
                                .filter(a -> ((PluralAttribute) a).getElementType().getJavaType().getAnnotation(Entity.class) != null)
                                .forEach(a -> toJoinNew.add(new FromAttributeEntity(finalJoin, ((PluralAttribute) a), entityManager.getMetamodel().entity(((PluralAttribute) a).getElementType().getJavaType()))));
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
