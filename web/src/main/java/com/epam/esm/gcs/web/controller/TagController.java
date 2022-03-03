package com.epam.esm.gcs.web.controller;

import com.epam.esm.gcs.business.dto.PageDto;
import com.epam.esm.gcs.business.dto.PageParamsDto;
import com.epam.esm.gcs.business.dto.TagDto;
import com.epam.esm.gcs.business.dto.UserWithMostlyUsedTagsDto;
import com.epam.esm.gcs.business.dto.group.OnTagCreate;
import com.epam.esm.gcs.business.service.TagService;
import com.epam.esm.gcs.web.assembler.AppUserRepresentationAssembler;
import com.epam.esm.gcs.web.assembler.PagedRepresentationAssembler;
import com.epam.esm.gcs.web.assembler.TagRepresentationAssembler;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
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

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(value = "/tags", produces = MediaTypes.HAL_JSON_VALUE)
public class TagController {

    private static final String PATH_VARIABLE_NOT_POSITIVE_MSG = "violation.path.variable.not.positive";

    private final TagService tagService;
    private final TagRepresentationAssembler tagRepresentationAssembler;
    private final AppUserRepresentationAssembler appUserRepresentationAssembler;
    private final PagedRepresentationAssembler<TagDto> pagedRepresentationAssembler;

    @GetMapping
    public PagedModel<TagDto> finalPage(@Valid PageParamsDto pageParams) {
        PageDto<TagDto> foundTags = tagService.findPage(pageParams);
        return pagedRepresentationAssembler.toModel(foundTags, tagRepresentationAssembler);
    }

    @Validated(OnTagCreate.class)
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public TagDto create(@Valid @RequestBody TagDto tag) {
        TagDto createdTag = tagService.create(tag);
        return tagRepresentationAssembler.toModel(createdTag);
    }

    @GetMapping("/most-used-of-users-with-highest-orders-price-amount")
    public CollectionModel<UserWithMostlyUsedTagsDto> findMostUsedTagsOfUsersWithHighestOrdersAmount() {
        List<UserWithMostlyUsedTagsDto> foundUsersWithMostlyUsedTags = tagService
                .findMostWidelyUsedTagsOfUsersWithHighestOrderPriceAmount();
        for (UserWithMostlyUsedTagsDto foundUsersWithMostlyUsedTag : foundUsersWithMostlyUsedTags) {
            appUserRepresentationAssembler.toModel(foundUsersWithMostlyUsedTag.getAppUserModel());
            foundUsersWithMostlyUsedTag.getMostUsedTags().forEach(tagRepresentationAssembler::toModel);
        }
        return CollectionModel.of(foundUsersWithMostlyUsedTags, linkTo(methodOn(TagController.class)
                .findMostUsedTagsOfUsersWithHighestOrdersAmount())
                .withSelfRel());
    }

    @GetMapping("/{id}")
    public TagDto findById(@PathVariable @Positive(message = PATH_VARIABLE_NOT_POSITIVE_MSG) Long id) {
        TagDto foundTag = tagService.findById(id);
        return tagRepresentationAssembler.toModel(foundTag);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable @Positive(message = PATH_VARIABLE_NOT_POSITIVE_MSG) Long id) {
        tagService.delete(id);
    }
}
