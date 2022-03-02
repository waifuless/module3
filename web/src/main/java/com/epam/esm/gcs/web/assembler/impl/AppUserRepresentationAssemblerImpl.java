package com.epam.esm.gcs.web.assembler.impl;

import com.epam.esm.gcs.business.dto.AppUserDto;
import com.epam.esm.gcs.business.dto.PageParamsDto;
import com.epam.esm.gcs.web.assembler.AppUserRepresentationAssembler;
import com.epam.esm.gcs.web.controller.AppUserController;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class AppUserRepresentationAssemblerImpl implements AppUserRepresentationAssembler {

    private static final String PAGE_NUMBER_PARAM = "page";
    private static final int FIRST_PAGE_NUMBER = 1;
    private static final String USER_ORDERS_URI_RELATION = "user-orders";

    @Override
    public AppUserDto toModel(AppUserDto entity) {
        String userOrdersUri = linkTo(methodOn(AppUserController.class)
                .findUserOrders(entity.getId(), new PageParamsDto()))
                .toUriComponentsBuilder().queryParam(PAGE_NUMBER_PARAM, FIRST_PAGE_NUMBER).toUriString();
        entity.add(Link.of(userOrdersUri, USER_ORDERS_URI_RELATION));
        return entity.add(linkTo(methodOn(AppUserController.class)
                .findById(entity.getId()))
                .withSelfRel());
    }
}
