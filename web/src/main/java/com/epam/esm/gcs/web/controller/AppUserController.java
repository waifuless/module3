package com.epam.esm.gcs.web.controller;

import com.epam.esm.gcs.business.dto.AppUserDto;
import com.epam.esm.gcs.business.dto.PageDto;
import com.epam.esm.gcs.business.dto.PageParamsDto;
import com.epam.esm.gcs.business.dto.UserOrderDto;
import com.epam.esm.gcs.business.service.AppUserService;
import com.epam.esm.gcs.web.assembler.AppUserRepresentationAssembler;
import com.epam.esm.gcs.web.assembler.PagedRepresentationAssembler;
import com.epam.esm.gcs.web.assembler.UserOrderRepresentationAssembler;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@Validated
@RequestMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class AppUserController {

    private static final String PATH_VARIABLE_NOT_POSITIVE_MSG = "violation.path.variable.not.positive";

    private final AppUserService appUserService;
    private final AppUserRepresentationAssembler appUserRepresentationAssembler;
    private final PagedRepresentationAssembler<AppUserDto> pagedAppUserRepresentationAssembler;

    private final UserOrderRepresentationAssembler userOrderRepresentationAssembler;
    private final PagedRepresentationAssembler<UserOrderDto> pagedUserOrderRepresentationAssembler;

    @GetMapping("/{id}")
    public AppUserDto findById(@PathVariable @Positive(message = PATH_VARIABLE_NOT_POSITIVE_MSG) Long id) {
        AppUserDto foundUser = appUserService.findById(id);
        return appUserRepresentationAssembler.toModel(foundUser);
    }

    @GetMapping
    public PagedModel<AppUserDto> findPage(@Valid PageParamsDto pageParams) {
        PageDto<AppUserDto> foundUsers = appUserService.findPage(pageParams);
        return pagedAppUserRepresentationAssembler.toModel(foundUsers, appUserRepresentationAssembler);
    }

    @GetMapping("/{userId}/orders")
    public PagedModel<UserOrderDto> findUserOrders(
            @PathVariable @Positive(message = PATH_VARIABLE_NOT_POSITIVE_MSG) Long userId,
            @Valid PageParamsDto pageParamsDto) {
        PageDto<UserOrderDto> foundOrders = appUserService.findUserOrders(userId, pageParamsDto);
        return pagedUserOrderRepresentationAssembler.toModel(foundOrders, userOrderRepresentationAssembler);
    }
}
