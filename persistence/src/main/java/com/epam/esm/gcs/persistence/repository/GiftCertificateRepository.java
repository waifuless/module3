package com.epam.esm.gcs.persistence.repository;

import com.epam.esm.gcs.persistence.model.GiftCertificateModel;
import com.epam.esm.gcs.persistence.model.GiftCertificateModelContext;

import java.util.List;

public interface GiftCertificateRepository extends CrudRepository<GiftCertificateModel> {

    List<GiftCertificateModel> findAll(GiftCertificateModelContext context);
}
