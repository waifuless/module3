package com.epam.esm.gcs.web.assembler.impl;

import com.epam.esm.gcs.business.dto.AppUserDto;
import com.epam.esm.gcs.web.assembler.AppUserRepresentationAssembler;
import com.epam.esm.gcs.web.controller.AppUserController;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class AppUserRepresentationAssemblerImpl implements AppUserRepresentationAssembler {

    @Override
    public AppUserDto toModel(AppUserDto entity) {
        return entity.add(linkTo(methodOn(AppUserController.class)
                .findById(entity.getId()))
                .withSelfRel());
    }
}
