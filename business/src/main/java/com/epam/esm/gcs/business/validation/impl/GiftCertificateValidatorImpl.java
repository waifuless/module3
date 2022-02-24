package com.epam.esm.gcs.business.validation.impl;

import com.epam.esm.gcs.business.exception.GiftCertificateCountsNotEnoughException;
import com.epam.esm.gcs.business.validation.GiftCertificateValidator;
import com.epam.esm.gcs.persistence.model.GiftCertificateModel;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GiftCertificateValidatorImpl implements GiftCertificateValidator {

    @Override
    public void validateCountIsEnough(GiftCertificateModel giftCertificate, Integer requestedCount) {
        if (giftCertificate.getCount() < requestedCount) {
            throw new GiftCertificateCountsNotEnoughException(List.of(giftCertificate.getId()));
        }
    }
}
