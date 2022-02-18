package com.epam.esm.gcs.persistence.repository.impl;

import com.epam.esm.gcs.persistence.model.GiftCertificateModel;
import com.epam.esm.gcs.persistence.model.GiftCertificateModelContext;
import com.epam.esm.gcs.persistence.queryconstructor.GiftCertificateQueryConstructor;
import com.epam.esm.gcs.persistence.repository.GiftCertificateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PostgresGiftCertificateRepositoryImpl implements GiftCertificateRepository {

    private final static String DELETE_QUERY = "DELETE FROM GiftCertificateModel gc WHERE gc.id=:id";
    private final static String EXISTS_BY_ID_QUERY = "SELECT COUNT(gc)>0 FROM GiftCertificateModel gc WHERE gc.id=:id";

    private final EntityManager entityManager;
    private final GiftCertificateQueryConstructor queryConstructor;

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
    public Optional<GiftCertificateModel> findById(long id) {
        return Optional.ofNullable(entityManager.find(GiftCertificateModel.class, id));
    }

    @Override
    @Transactional
    public void updateById(long id, GiftCertificateModel giftCertificate) {
        GiftCertificateModel foundGiftCertificate = entityManager.find(GiftCertificateModel.class, id);
        setNotNullFields(giftCertificate, foundGiftCertificate);

        foundGiftCertificate.setLastUpdateDate(LocalDateTime.now());
    }

    @Override
    public void delete(long id) {
        entityManager
                .createQuery(DELETE_QUERY)
                .setParameter("id", id)
                .executeUpdate();
    }

    @Override
    public Boolean existsById(long id) {
        return entityManager
                .createQuery(EXISTS_BY_ID_QUERY,
                        Boolean.class)
                .setParameter("id", id)
                .getSingleResult();
    }

    @Override
    public List<GiftCertificateModel> findAll(GiftCertificateModelContext context) {
        CriteriaQuery<GiftCertificateModel> criteriaQuery = queryConstructor.constructFindAllQueryByContext(context);
        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    private void setNotNullFields(GiftCertificateModel source, GiftCertificateModel destination) {
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
        if (source.getState() != null) {
            destination.setState(source.getState());
        }
        if (source.getCount() != null) {
            destination.setCount(source.getCount());
        }
        if (source.getSuccessor() != null) {
            destination.setSuccessor(source.getSuccessor());
        }
    }
}
