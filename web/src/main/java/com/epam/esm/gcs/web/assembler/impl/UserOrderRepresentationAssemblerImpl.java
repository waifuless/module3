package com.epam.esm.gcs.web.assembler.impl;

import com.epam.esm.gcs.business.dto.UserOrderDto;
import com.epam.esm.gcs.web.assembler.AppUserRepresentationAssembler;
import com.epam.esm.gcs.web.assembler.GiftCertificateRepresentationAssembler;
import com.epam.esm.gcs.web.assembler.UserOrderRepresentationAssembler;
import com.epam.esm.gcs.web.controller.UserOrderController;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
@RequiredArgsConstructor
public class UserOrderRepresentationAssemblerImpl implements UserOrderRepresentationAssembler {

    private final AppUserRepresentationAssembler appUserRepresentationAssembler;
    private final GiftCertificateRepresentationAssembler giftCertificateRepresentationAssembler;

    @Override
    public UserOrderDto toModel(UserOrderDto entity) {
        appUserRepresentationAssembler.toModel(entity.getUser());
        entity.getPositions().forEach(position ->
                giftCertificateRepresentationAssembler.toModel(position.getGiftCertificate()));
        return entity.add(linkTo(methodOn(UserOrderController.class)
                .findById(entity.getId()))
                .withSelfRel());
    }
}
