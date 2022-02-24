package com.epam.esm.gcs.persistence.queryconstructor;

import com.epam.esm.gcs.persistence.model.GiftCertificateModel;
import com.epam.esm.gcs.persistence.model.GiftCertificateModelContext;

import javax.persistence.criteria.CriteriaQuery;

public interface GiftCertificateQueryConstructor {

    CriteriaQuery<GiftCertificateModel> constructFindAllQueryByContext(GiftCertificateModelContext context);
}
