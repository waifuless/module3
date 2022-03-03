package com.epam.esm.gcs.persistence.repository.impl;

import com.epam.esm.gcs.persistence.model.ActionWithCountModel;
import com.epam.esm.gcs.persistence.model.ActualityStateModel;
import com.epam.esm.gcs.persistence.model.GiftCertificateModel;
import com.epam.esm.gcs.persistence.model.GiftCertificateModelContext;
import com.epam.esm.gcs.persistence.model.PageModel;
import com.epam.esm.gcs.persistence.model.PageParamsModel;
import com.epam.esm.gcs.persistence.queryconstructor.GiftCertificateQueryConstructor;
import com.epam.esm.gcs.persistence.repository.GiftCertificateRepository;
import com.epam.esm.gcs.persistence.util.Paginator;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public class JpaGiftCertificateRepositoryImpl extends AbstractReadRepository<GiftCertificateModel>
        implements GiftCertificateRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    private final GiftCertificateQueryConstructor queryConstructor;
    private final Paginator paginator;

    public JpaGiftCertificateRepositoryImpl(EntityManager entityManager,
                                            GiftCertificateQueryConstructor queryConstructor,
                                            Paginator paginator) {
        super(entityManager, GiftCertificateModel.class, paginator);

        this.entityManager = entityManager;
        this.queryConstructor = queryConstructor;
        this.paginator = paginator;
    }

    @Override
    @Transactional
    public GiftCertificateModel create(GiftCertificateModel giftCertificate) {
        GiftCertificateModel giftCertificateCopy = new GiftCertificateModel(giftCertificate);

        entityManager.persist(giftCertificateCopy);
        return giftCertificateCopy;
    }

    @Override
    public PageModel<GiftCertificateModel> findPage(GiftCertificateModelContext context, PageParamsModel pageParams) {
        CriteriaQuery<GiftCertificateModel> criteriaQuery = queryConstructor.constructFindAllQueryByContext(context);
        List<GiftCertificateModel> content = entityManager.createQuery(criteriaQuery)
                .setFirstResult(paginator.findStartPosition(pageParams))
                .setMaxResults(pageParams.getSize())
                .getResultList();
        return new PageModel<>(content, pageParams, count(context));
    }

    @Override
    public Long count(GiftCertificateModelContext context) {
        CriteriaQuery<Long> countQuery = queryConstructor.constructCountQueryByContext(context);
        return entityManager.createQuery(countQuery)
                .getSingleResult();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED)
    public void updateCount(Long id, ActionWithCountModel action) {
        GiftCertificateModel giftCertificate = entityManager
                .find(GiftCertificateModel.class, id, LockModeType.PESSIMISTIC_WRITE);
        Integer currentCount = giftCertificate.getCount();
        int newCount;
        if (action.getMode().equals(ActionWithCountModel.Mode.ADD)) {
            newCount = currentCount + action.getCount();
        } else {
            newCount = currentCount - action.getCount();
        }
        giftCertificate.setCount(newCount);
    }

    @Override
    public Optional<Long> findActualId(Long archivedId) {
        GiftCertificateModel archived = entityManager.find(GiftCertificateModel.class, archivedId);
        GiftCertificateModel successor;
        Set<Long> visitedIds = new HashSet<>();
        while ((successor = archived.getSuccessor()) != null) {
            visitedIds.add(archived.getId());
            if (visitedIds.contains(successor.getId())) {
                //todo: make ex, inconsistent data
//                throw new RuntimeException("inconsistent data");
                return Optional.empty();
            }

            if (successor.getState().equals(ActualityStateModel.ACTUAL)) {
                return Optional.of(successor.getId());
            }
            archived = successor;
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public void archive(Long id) {
        GiftCertificateModel giftCertificate = entityManager.find(GiftCertificateModel.class, id);
        giftCertificate.setState(ActualityStateModel.ARCHIVED);
        giftCertificate.setCount(0);
    }

    @Override
    @Transactional
    public GiftCertificateModel archiveAndCreateSuccessor(Long idToArchive, GiftCertificateModel modifications) {
        GiftCertificateModel giftCertificateToArchive = entityManager.find(GiftCertificateModel.class, idToArchive);
        GiftCertificateModel successor = new GiftCertificateModel(giftCertificateToArchive);

        successor.setId(null);
        setNotNullFieldsAvailableForModification(successor, modifications);
        successor.setState(ActualityStateModel.ACTUAL);

        entityManager.persist(successor);

        archive(idToArchive);
        giftCertificateToArchive.setSuccessor(successor);
        return successor;
    }

    private void setNotNullFieldsAvailableForModification(GiftCertificateModel destination,
                                                          GiftCertificateModel source) {
        if (source.getName() != null) {
            destination.setName(source.getName());
        }
        if (source.getDescription() != null) {
            destination.setDescription(source.getDescription());
        }
        if (source.getPrice() != null) {
            destination.setPrice(source.getPrice());
        }
        if (source.getDuration() != null) {
            destination.setDuration(source.getDuration());
        }
        if (source.getTags() != null) {
            destination.setTags(source.getTags());
        }
    }
}
