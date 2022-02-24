package com.epam.esm.gcs.web.controller;

import com.epam.esm.gcs.business.dto.GiftCertificateDto;
import com.epam.esm.gcs.business.dto.GiftCertificateDtoContext;
import com.epam.esm.gcs.business.dto.PageDto;
import com.epam.esm.gcs.business.dto.group.OnGiftCertificateCreate;
import com.epam.esm.gcs.business.service.GiftCertificateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import java.util.List;

@RestController
@Validated
@RequestMapping(value = "/gift-certificates", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class GiftCertificateController {

    private static final String PATH_VARIABLE_NOT_POSITIVE_MSG = "violation.path.variable.not.positive";

    private final GiftCertificateService giftCertificateService;

    @GetMapping
    public List<GiftCertificateDto> finalPage(@Valid GiftCertificateDtoContext giftCertificateDtoContext,
                                              @Valid PageDto page) {
        return giftCertificateService.findPage(giftCertificateDtoContext, page);
    }

    @Validated(OnGiftCertificateCreate.class)
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public GiftCertificateDto create(@Valid @RequestBody GiftCertificateDto giftCertificate) {
        return giftCertificateService.create(giftCertificate);
    }

    @GetMapping("/{id}")
    public GiftCertificateDto findById(@PathVariable @Positive(message = PATH_VARIABLE_NOT_POSITIVE_MSG) Long id) {
        return giftCertificateService.findById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable @Positive(message = PATH_VARIABLE_NOT_POSITIVE_MSG) Long id) {
        giftCertificateService.archive(id);
    }

    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public GiftCertificateDto update(@PathVariable @Positive(message = PATH_VARIABLE_NOT_POSITIVE_MSG) Long id,
                                     @Valid @RequestBody GiftCertificateDto giftCertificate) {
        return giftCertificateService.archiveAndCreateSuccessor(id, giftCertificate);
    }
}
