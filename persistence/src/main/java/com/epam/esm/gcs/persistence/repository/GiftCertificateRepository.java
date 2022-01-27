package com.epam.esm.gcs.persistence.repository;

import com.epam.esm.gcs.persistence.model.GiftCertificateModel;
import com.epam.esm.gcs.persistence.model.GiftCertificateModelContext;

import java.util.List;

/**
 * Repository interface that contains all methods for interaction with GiftCertificateModel
 */
public interface GiftCertificateRepository extends CrudRepository<GiftCertificateModel> {

    /**
     * Finds giftCertificates that fit the @param context with specified order.
     *
     * @param context - contains parameters for search giftCertificates with some specified order. Fields that should
     *                NOT affect the search are null
     * @return List of found giftCertificates with some specified order
     */
    List<GiftCertificateModel> findAll(GiftCertificateModelContext context);
}
