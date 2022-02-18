package com.epam.esm.gcs.business.service;

import com.epam.esm.gcs.business.dto.GiftCertificateDto;
import com.epam.esm.gcs.business.dto.GiftCertificateDtoContext;

import java.util.List;
import java.util.Optional;

/**
 * Service interface that contains all methods for interaction with GiftCertificateDto
 */
public interface GiftCertificateService extends CrudService<GiftCertificateDto> {

    /**
     * Finds giftCertificates that fit the @param context with specified order.
     *
     * @param context - contains parameters for search giftCertificates with some specified order. Fields that should
     *                NOT affect the search are null
     * @return List of found giftCertificates with some specified order
     */
    List<GiftCertificateDto> findAll(GiftCertificateDtoContext context);

    void addCount(Long id, Integer count);

    void reduceCount(Long id, Integer count);

    Optional<Long> findActualId(Long id);
}
