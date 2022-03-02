package com.epam.esm.gcs.web.controller;

import com.epam.esm.gcs.business.dto.ActionWithCountDto;
import com.epam.esm.gcs.business.dto.GiftCertificateDto;
import com.epam.esm.gcs.business.dto.GiftCertificateDtoContext;
import com.epam.esm.gcs.business.dto.PageDto;
import com.epam.esm.gcs.business.dto.PageParamsDto;
import com.epam.esm.gcs.business.dto.group.OnGiftCertificateCreate;
import com.epam.esm.gcs.business.service.GiftCertificateService;
import com.epam.esm.gcs.web.assembler.GiftCertificateRepresentationAssembler;
import com.epam.esm.gcs.web.assembler.PagedRepresentationAssembler;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
@RequestMapping(value = "/gift-certificates", produces = MediaTypes.HAL_JSON_VALUE)
@RequiredArgsConstructor
public class GiftCertificateController {

    private static final String PATH_VARIABLE_NOT_POSITIVE_MSG = "violation.path.variable.not.positive";

    private final GiftCertificateService giftCertificateService;
    private final GiftCertificateRepresentationAssembler giftCertificateRepresentationAssembler;
    private final PagedRepresentationAssembler<GiftCertificateDto> pagedRepresentationAssembler;

    @GetMapping
    public PagedModel<GiftCertificateDto> finalPage(@Valid GiftCertificateDtoContext giftCertificateDtoContext,
                                                    @Valid PageParamsDto pageParams) {
        PageDto<GiftCertificateDto> foundGiftCertificates = giftCertificateService
                .findPage(giftCertificateDtoContext, pageParams);
        return pagedRepresentationAssembler.toModel(foundGiftCertificates, giftCertificateRepresentationAssembler);
    }

    @Validated(OnGiftCertificateCreate.class)
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public GiftCertificateDto create(@Valid @RequestBody GiftCertificateDto giftCertificate) {
        GiftCertificateDto createdGiftCertificate = giftCertificateService.create(giftCertificate);
        return giftCertificateRepresentationAssembler.toModel(createdGiftCertificate);
    }

    @GetMapping("/{id}")
    public GiftCertificateDto findById(@PathVariable @Positive(message = PATH_VARIABLE_NOT_POSITIVE_MSG) Long id) {
        GiftCertificateDto foundGiftCertificate = giftCertificateService.findById(id);
        return giftCertificateRepresentationAssembler.toModel(foundGiftCertificate);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void archive(@PathVariable @Positive(message = PATH_VARIABLE_NOT_POSITIVE_MSG) Long id) {
        giftCertificateService.archive(id);
    }

    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public GiftCertificateDto archiveAndCreateSuccessor(
            @PathVariable @Positive(message = PATH_VARIABLE_NOT_POSITIVE_MSG) Long id,
            @Valid @RequestBody GiftCertificateDto giftCertificate) {
        GiftCertificateDto successor = giftCertificateService.archiveAndCreateSuccessor(id, giftCertificate);
        return giftCertificateRepresentationAssembler.toModel(successor);
    }

    @PatchMapping(value = "/{id}/count", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateCount(@PathVariable @Positive(message = PATH_VARIABLE_NOT_POSITIVE_MSG) Long id,
                                            @Valid @RequestBody ActionWithCountDto action) {
        giftCertificateService.updateCount(id, action);
        return ResponseEntity.ok().build();
    }
}
