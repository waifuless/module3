package com.epam.esm.gcs.persistence.repository;

import com.epam.esm.gcs.persistence.model.ActionWithCountModel;
import com.epam.esm.gcs.persistence.model.GiftCertificateModel;
import com.epam.esm.gcs.persistence.model.GiftCertificateModelContext;
import com.epam.esm.gcs.persistence.model.PageModel;
import com.epam.esm.gcs.persistence.model.PageParamsModel;

/**
 * Repository interface that contains all methods for interaction with GiftCertificateModel
 */
public interface GiftCertificateRepository extends CrRepository<GiftCertificateModel>,
        ArchiverRepository<GiftCertificateModel> {

    /**
     * Finds giftCertificates that fit the @param context with specified order of some Page.
     *
     * @param context    - contains parameters for search giftCertificates with some specified order. Fields that should
     *                   NOT affect the search are null
     * @param pageParams - page to select items
     * @return List of found giftCertificates with some specified order of some page
     */
    PageModel<GiftCertificateModel> findPage(GiftCertificateModelContext context, PageParamsModel pageParams);

    /**
     * Finds count of giftCertificates by Context
     *
     * @param context - parameters for search
     * @return - count of giftCertificates by Context
     */
    Long count(GiftCertificateModelContext context);

    /**
     * Update count of specified giftCertificate
     *
     * @param id     - id of giftCertificate
     * @param action - action with count
     */
    void updateCount(Long id, ActionWithCountModel action);
}
