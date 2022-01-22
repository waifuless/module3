package com.epam.esm.gcs.web.controller;

import com.epam.esm.gcs.business.dto.GiftCertificateDto;
import com.epam.esm.gcs.business.dto.GiftCertificateDtoContext;
import com.epam.esm.gcs.business.service.GiftCertificateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/gift-certificates", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class GiftCertificateController {

    private final GiftCertificateService giftCertificateService;

    @GetMapping
    private List<GiftCertificateDto> findAll() {
        return giftCertificateService.findAll();
    }

    @GetMapping("/tagged/{tagName}")
    private List<GiftCertificateDto> findAllTagged(GiftCertificateDtoContext giftCertificateDtoContext) {
        System.out.println(giftCertificateDtoContext);
        return giftCertificateService.findAll();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    private GiftCertificateDto create(@Valid @RequestBody GiftCertificateDto giftCertificate) {
        return giftCertificateService.create(giftCertificate);
    }

    @GetMapping("/{id}")
    private GiftCertificateDto findById(@PathVariable Long id) {
        return giftCertificateService.findById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    private void delete(@PathVariable Long id) {
        giftCertificateService.delete(id);
    }
}
