package com.epam.esm.gcs.persistence.repository.impl;

import com.epam.esm.gcs.persistence.model.ActualityStateModel;
import com.epam.esm.gcs.persistence.model.GiftCertificateModel;
import com.epam.esm.gcs.persistence.model.GiftCertificateModelContext;
import com.epam.esm.gcs.persistence.model.PageModel;
import com.epam.esm.gcs.persistence.queryconstructor.GiftCertificateQueryConstructor;
import com.epam.esm.gcs.persistence.repository.GiftCertificateRepository;
import com.epam.esm.gcs.persistence.util.Paginator;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public class PostgresGiftCertificateRepositoryImpl extends AbstractReadRepository<GiftCertificateModel>
        implements GiftCertificateRepository {

    private final EntityManager entityManager;
    private final GiftCertificateQueryConstructor queryConstructor;

    public PostgresGiftCertificateRepositoryImpl(EntityManager entityManager,
                                                 GiftCertificateQueryConstructor queryConstructor,
                                                 Paginator paginator) {
        super(entityManager, GiftCertificateModel.class, paginator);

        this.entityManager = entityManager;
        this.queryConstructor = queryConstructor;
    }

    @Override
    @Transactional
    public GiftCertificateModel create(GiftCertificateModel giftCertificate) {
        GiftCertificateModel giftCertificateCopy = new GiftCertificateModel(giftCertificate);
        LocalDateTime creationDateTime = LocalDateTime.now();

        giftCertificateCopy.setCreateDate(creationDateTime);
        giftCertificateCopy.setLastUpdateDate(creationDateTime);

        entityManager.persist(giftCertificateCopy);
        return giftCertificateCopy;
    }

    @Override
    public List<GiftCertificateModel> findAll(GiftCertificateModelContext context) {
        CriteriaQuery<GiftCertificateModel> criteriaQuery = queryConstructor.constructFindAllQueryByContext(context);
        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public List<GiftCertificateModel> findPage(GiftCertificateModelContext context, PageModel page) {
        CriteriaQuery<GiftCertificateModel> criteriaQuery = queryConstructor.constructFindAllQueryByContext(context);
        return entityManager.createQuery(criteriaQuery)
                .setFirstResult(paginator.findStartPosition(page))
                .setMaxResults(page.getSize())
                .getResultList();
    }

    @Override
    public void updateCount(Long id, Integer newCount) {
        GiftCertificateModel giftCertificate = entityManager.find(GiftCertificateModel.class, id);
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
        successor.setLastUpdateDate(LocalDateTime.now());
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
