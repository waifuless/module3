package com.epam.esm.gcs.web.assembler.impl;

import com.epam.esm.gcs.business.dto.TagDto;
import com.epam.esm.gcs.web.assembler.TagRepresentationAssembler;
import com.epam.esm.gcs.web.controller.TagController;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TagRepresentationAssemblerImpl implements TagRepresentationAssembler {

    @Override
    public TagDto toModel(TagDto entity) {
        return entity.add(linkTo(methodOn(TagController.class)
                .findById(entity.getId()))
                .withSelfRel());
    }
}
