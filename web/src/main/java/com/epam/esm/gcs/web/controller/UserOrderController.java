package com.epam.esm.gcs.web.controller;

import com.epam.esm.gcs.business.dto.PageDto;
import com.epam.esm.gcs.business.dto.PageParamsDto;
import com.epam.esm.gcs.business.dto.UserOrderDto;
import com.epam.esm.gcs.business.dto.group.OnUserOrderCreate;
import com.epam.esm.gcs.business.service.UserOrderService;
import com.epam.esm.gcs.web.assembler.PagedRepresentationAssembler;
import com.epam.esm.gcs.web.assembler.UserOrderRepresentationAssembler;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(value = "/orders", produces = MediaTypes.HAL_JSON_VALUE)
public class UserOrderController {

    private static final String PATH_VARIABLE_NOT_POSITIVE_MSG = "violation.path.variable.not.positive";

    private final UserOrderService orderService;
    private final UserOrderRepresentationAssembler userOrderRepresentationAssembler;
    private final PagedRepresentationAssembler<UserOrderDto> pagedRepresentationAssembler;

    @GetMapping
    public PagedModel<UserOrderDto> findPage(@Valid PageParamsDto pageParams) {
        PageDto<UserOrderDto> foundPage = orderService.findPage(pageParams);
        return pagedRepresentationAssembler.toModel(foundPage, userOrderRepresentationAssembler);
    }

    @Validated(OnUserOrderCreate.class)
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public UserOrderDto create(@Valid @RequestBody UserOrderDto userOrderDto) {
        UserOrderDto createdOrder = orderService.create(userOrderDto);
        return userOrderRepresentationAssembler.toModel(createdOrder);
    }

    @GetMapping("/{id}")
    public UserOrderDto findById(@PathVariable @Positive(message = PATH_VARIABLE_NOT_POSITIVE_MSG) Long id) {
        UserOrderDto foundOrder = orderService.findById(id);
        return userOrderRepresentationAssembler.toModel(foundOrder);
    }
}
