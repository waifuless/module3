package com.epam.esm.gcs.web.controller;

import com.epam.esm.gcs.business.dto.UserOrderDto;
import com.epam.esm.gcs.business.dto.group.OnUserOrderCreate;
import com.epam.esm.gcs.business.service.UserOrderService;
import lombok.RequiredArgsConstructor;
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
import java.util.List;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(value = "/orders", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserOrderController {

    private final static String PATH_VARIABLE_NOT_POSITIVE_MSG = "violation.path.variable.not.positive";

    private final UserOrderService orderService;

    @GetMapping
    public List<UserOrderDto> findAll() {
        return orderService.findAll();
    }

    @Validated(OnUserOrderCreate.class)
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public UserOrderDto create(@Valid @RequestBody UserOrderDto userOrderDto) {
        return orderService.create(userOrderDto);
    }

    @GetMapping("/{id}")
    public UserOrderDto findById(@PathVariable @Positive(message = PATH_VARIABLE_NOT_POSITIVE_MSG) Long id) {
        return orderService.findById(id);
    }
}
