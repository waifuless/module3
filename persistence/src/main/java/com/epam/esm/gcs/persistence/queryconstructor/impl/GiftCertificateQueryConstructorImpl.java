package com.epam.esm.gcs.persistence.queryconstructor.impl;

import com.epam.esm.gcs.persistence.model.GiftCertificateModel;
import com.epam.esm.gcs.persistence.model.GiftCertificateModelContext;
import com.epam.esm.gcs.persistence.model.TagModel;
import com.epam.esm.gcs.persistence.queryconstructor.GiftCertificateQueryConstructor;
import com.epam.esm.gcs.persistence.tableproperty.SortDirection;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class GiftCertificateQueryConstructorImpl implements GiftCertificateQueryConstructor {

    private final CriteriaBuilder criteriaBuilder;
    private final EntityType<GiftCertificateModel> giftCertificateType;
    private final EntityType<TagModel> tagType;

    public GiftCertificateQueryConstructorImpl(EntityManager entityManager) {
        this.criteriaBuilder = entityManager.getCriteriaBuilder();
        Metamodel metamodel = entityManager.getMetamodel();
        this.giftCertificateType = metamodel.entity(GiftCertificateModel.class);
        this.tagType = metamodel.entity(TagModel.class);
    }

    @Override
    public CriteriaQuery<GiftCertificateModel> constructFindAllQueryByContext(GiftCertificateModelContext context) {

        CriteriaQuery<GiftCertificateModel> criteriaQuery = criteriaBuilder.createQuery(GiftCertificateModel.class);
        Root<GiftCertificateModel> giftCertificateRoot = criteriaQuery.from(GiftCertificateModel.class);

        List<Predicate> predicates = constructPredicates(context, criteriaQuery, giftCertificateRoot);

        List<Order> ordersList = constructOrdersList(context, giftCertificateRoot);

        criteriaQuery.select(giftCertificateRoot)
                .where(predicates.toArray(new Predicate[0]))
                .orderBy(ordersList);
        return criteriaQuery;
    }

    @Override
    public CriteriaQuery<Long> constructCountQueryByContext(GiftCertificateModelContext context) {
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<GiftCertificateModel> giftCertificateRoot = criteriaQuery.from(GiftCertificateModel.class);

        List<Predicate> predicates = constructPredicates(context, criteriaQuery, giftCertificateRoot);

        criteriaQuery.select(criteriaBuilder.count(giftCertificateRoot))
                .where(predicates.toArray(new Predicate[0]));
        return criteriaQuery;
    }

    private List<Predicate> constructPredicates(GiftCertificateModelContext context,
                                                CriteriaQuery<?> criteriaQuery,
                                                Root<GiftCertificateModel> giftCertificateRoot) {
        List<Predicate> predicates = new ArrayList<>();
        if (context.getTagNames() != null && !context.getTagNames().isEmpty()) {
            Join<GiftCertificateModel, TagModel> tagJoin =
                    giftCertificateRoot.join(giftCertificateType.getList("tags", TagModel.class));
            predicates.add(constructTagNamesPredicate(context.getTagNames(), giftCertificateRoot, tagJoin));
            criteriaQuery.groupBy(giftCertificateRoot)
                    .having(criteriaBuilder.equal(criteriaBuilder.count(tagJoin), context.getTagNames().size()));
        }

        if (context.getSearchValue() != null) {
            predicates.add(constructSearchValuePredicate(context.getSearchValue(), giftCertificateRoot));
        }
        return predicates;
    }

    private Predicate constructTagNamesPredicate(Set<String> tagNames,
                                                 Root<GiftCertificateModel> giftCertificateRoot,
                                                 Join<GiftCertificateModel, TagModel> tagJoin) {
        List<Predicate> tagEqualPredicates = new ArrayList<>();
        for (String tagName : tagNames) {
            Predicate tagEqualPredicate = criteriaBuilder.equal(
                    tagJoin.get(
                            tagType.getSingularAttribute("name", String.class))
                    , tagName);
            tagEqualPredicates.add(tagEqualPredicate);
        }
        return criteriaBuilder.or(tagEqualPredicates.toArray(new Predicate[0]));
    }

    private Predicate constructSearchValuePredicate(String searchValue,
                                                    Root<GiftCertificateModel> giftCertificateRoot) {

        String searchValueRegex = "%" + searchValue.toUpperCase() + "%";

        Predicate likeNamePredicate = constructLikeIgnoreCasePredicate(giftCertificateRoot,
                "name", searchValueRegex);
        Predicate likeDescriptionPredicate = constructLikeIgnoreCasePredicate(giftCertificateRoot,
                "description", searchValueRegex);

        return criteriaBuilder.or(likeNamePredicate, likeDescriptionPredicate);
    }

    private Predicate constructLikeIgnoreCasePredicate(Root<GiftCertificateModel> giftCertificateRoot,
                                                       String attributeName, String searchValueRegex) {
        return criteriaBuilder.like(
                criteriaBuilder.upper(
                        giftCertificateRoot.get(
                                giftCertificateType.getSingularAttribute(attributeName, String.class))
                ),
                searchValueRegex);
    }

    private List<Order> constructOrdersList(GiftCertificateModelContext context,
                                            Root<GiftCertificateModel> giftCertificateRoot) {
        List<Order> orderList = new ArrayList<>();
        if (context.getSortDirectionByFieldNameMap() != null) {
            for (Map.Entry<String, SortDirection> sortByEntry : context.getSortDirectionByFieldNameMap().entrySet()) {
                Expression<GiftCertificateModel> attribute = giftCertificateRoot.get(sortByEntry.getKey());
                Order order;
                if (sortByEntry.getValue().equals(SortDirection.ASC)) {
                    order = criteriaBuilder.asc(attribute);
                } else {
                    order = criteriaBuilder.desc(attribute);
                }
                orderList.add(order);
            }
        }
        return orderList;
    }
}
