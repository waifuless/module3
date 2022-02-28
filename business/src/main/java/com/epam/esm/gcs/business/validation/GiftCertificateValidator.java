package com.epam.esm.gcs.business.validation;

import com.epam.esm.gcs.persistence.model.GiftCertificateModel;

public interface GiftCertificateValidator {

    void validateCountIsEnough(GiftCertificateModel giftCertificate, Integer requestedCount);

    void validateStateForArchiveAndCreateSuccessor(Long idToArchive);
}
