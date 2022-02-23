package com.epam.esm.gcs.web.controller;

import com.epam.esm.gcs.business.dto.TagDto;
import com.epam.esm.gcs.business.dto.UserWithMostlyUsedTagsDto;
import com.epam.esm.gcs.business.dto.group.OnTagCreate;
import com.epam.esm.gcs.business.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
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
@RequestMapping(value = "/tags", produces = MediaType.APPLICATION_JSON_VALUE)
public class TagController {

    private final static String PATH_VARIABLE_NOT_POSITIVE_MSG = "violation.path.variable.not.positive";

    private final TagService tagService;

    @GetMapping
    public List<TagDto> findAll() {
        return tagService.findAll();
    }

    @Validated(OnTagCreate.class)
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public TagDto create(@Valid @RequestBody TagDto tag) {
        return tagService.create(tag);
    }

    @GetMapping("/most-used-of-users-with-highest-orders-price-amount")
    public List<UserWithMostlyUsedTagsDto> findMostUsedTagsOfUsersWithHighestOrdersAmount() {
        return tagService.findMostWidelyUsedTagsOfUsersWithHighestOrderPriceAmount();
    }

    @GetMapping("/{id}")
    public TagDto findById(@PathVariable @Positive(message = PATH_VARIABLE_NOT_POSITIVE_MSG) Long id) {
        return tagService.findById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable @Positive(message = PATH_VARIABLE_NOT_POSITIVE_MSG) Long id) {
        tagService.delete(id);
    }
}
