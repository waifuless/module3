package com.epam.esm.gcs.business.service;

import com.epam.esm.gcs.business.dto.GiftCertificateDto;
import com.epam.esm.gcs.business.dto.GiftCertificateDtoContext;

import java.util.List;

public interface GiftCertificateService extends CrudService<GiftCertificateDto> {

    List<GiftCertificateDto> findAll(GiftCertificateDtoContext context);
}
