package com.epam.esm.gcs.web.assembler.impl;

import com.epam.esm.gcs.business.dto.GiftCertificateDto;
import com.epam.esm.gcs.web.assembler.GiftCertificateRepresentationAssembler;
import com.epam.esm.gcs.web.assembler.TagRepresentationAssembler;
import com.epam.esm.gcs.web.controller.GiftCertificateController;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
@RequiredArgsConstructor
public class GiftCertificateRepresentationAssemblerImpl implements GiftCertificateRepresentationAssembler {

    //    private static final String ARCHIVE_AND_CREATE_SUCCESSOR_METHOD_NAME = "archiveAndCreateSuccessor";
    //    private static final String ARCHIVE_METHOD_NAME = "archive";
    private final TagRepresentationAssembler tagRepresentationAssembler;

    @Override
    public GiftCertificateDto toModel(GiftCertificateDto entity) {
        entity.getTags().forEach(tagRepresentationAssembler::toModel);
        return entity.add(linkTo(methodOn(GiftCertificateController.class)
                .findById(entity.getId()))
                .withSelfRel());

        //todo: delete it (self routes)
//        entity.add(linkTo(methodOn(GiftCertificateController.class)
//                .archiveAndCreateSuccessor(entity.getId(), GiftCertificateDto.builder().build()))
//                .withRel(ARCHIVE_AND_CREATE_SUCCESSOR_METHOD_NAME));
//        entity.add(linkTo(GiftCertificateController.class)
//                .slash(entity.getId())
//                .withRel(ARCHIVE_METHOD_NAME));
    }
}
