package com.epam.esm.gcs.business.validation;

import com.epam.esm.gcs.business.exception.EntitiesArchivedException;
import com.epam.esm.gcs.business.exception.GiftCertificateCountsNotEnoughException;
import com.epam.esm.gcs.persistence.model.GiftCertificateModel;

/**
 * Validator for GiftCertificates
 */
public interface GiftCertificateValidator {

    /**
     * Validates that count is enough to reduce (gift certificate count always should be >=0)
     *
     * @param giftCertificate - validation target
     * @param requestedCount  - count trying to reduce from giftCertificate
     * @throws GiftCertificateCountsNotEnoughException - if @param requestCount > giftCertificate.count
     */
    void validateCountIsEnough(GiftCertificateModel giftCertificate, Integer requestedCount);

    /**
     * Validates that specified GiftCertificate has ACTUAL status
     *
     * @param id - id of giftCertificate to validate
     * @throws EntitiesArchivedException - if entity is ARCHIVED
     */
    void validateStateIsActual(Long id);
}
