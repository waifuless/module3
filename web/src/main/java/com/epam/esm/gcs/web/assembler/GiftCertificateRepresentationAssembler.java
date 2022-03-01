package com.epam.esm.gcs.web.assembler;

import com.epam.esm.gcs.business.dto.GiftCertificateDto;
import org.springframework.hateoas.server.RepresentationModelAssembler;

/**
 * Assembler for GiftCertificateDto
 */
public interface GiftCertificateRepresentationAssembler
        extends RepresentationModelAssembler<GiftCertificateDto, GiftCertificateDto> {
}
