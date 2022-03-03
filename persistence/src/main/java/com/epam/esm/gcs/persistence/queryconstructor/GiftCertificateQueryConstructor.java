package com.epam.esm.gcs.persistence.queryconstructor;

import com.epam.esm.gcs.persistence.model.GiftCertificateModel;
import com.epam.esm.gcs.persistence.model.GiftCertificateModelContext;

import javax.persistence.criteria.CriteriaQuery;

/**
 * Constructor for dynamic criteriaQueries for giftCertificate
 */
public interface GiftCertificateQueryConstructor {

    /**
     * Constructs query for giftCertificate searching by context
     *
     * @param context - parameters for query
     * @return - Constructed query
     */
    CriteriaQuery<GiftCertificateModel> constructFindAllQueryByContext(GiftCertificateModelContext context);

    /**
     * Constructs query for giftCertificate counting by context
     *
     * @param context - parameters for query
     * @return - Constructed query
     */
    CriteriaQuery<Long> constructCountQueryByContext(GiftCertificateModelContext context);
}
