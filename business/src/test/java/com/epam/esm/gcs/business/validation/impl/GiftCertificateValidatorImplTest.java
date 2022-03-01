package com.epam.esm.gcs.business.validation.impl;

import com.epam.esm.gcs.business.dto.ActualityStateDto;
import com.epam.esm.gcs.business.dto.GiftCertificateDto;
import com.epam.esm.gcs.business.exception.EntitiesArchivedException;
import com.epam.esm.gcs.business.exception.GiftCertificateCountsNotEnoughException;
import com.epam.esm.gcs.business.service.GiftCertificateService;
import com.epam.esm.gcs.business.service.impl.ModelMapperTestConfig;
import com.epam.esm.gcs.business.validation.GiftCertificateValidator;
import com.epam.esm.gcs.persistence.model.GiftCertificateModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GiftCertificateValidatorImplTest {

    private final GiftCertificateValidator giftCertificateValidator;
    private final GiftCertificateService giftCertificateService;

    public GiftCertificateValidatorImplTest(@Mock GiftCertificateService giftCertificateService) {
        this.giftCertificateValidator = new GiftCertificateValidatorImpl(giftCertificateService,
                new ModelMapperTestConfig().modelMapper());
        this.giftCertificateService = giftCertificateService;
    }

    @Test
    void validateCountIsEnough_doNothing_ifValid() {
        GiftCertificateModel giftCertificate = GiftCertificateModel.builder()
                .id(1L)
                .count(100)
                .build();
        Integer requestCount = 99;
        assertDoesNotThrow(() -> giftCertificateValidator.validateCountIsEnough(giftCertificate, requestCount));
    }

    @Test
    void validateCountIsEnough_throwException_ifInValid() {
        GiftCertificateModel giftCertificate = GiftCertificateModel.builder()
                .id(1L)
                .count(100)
                .build();
        Integer requestCount = 101;
        assertThrows(GiftCertificateCountsNotEnoughException.class,
                () -> giftCertificateValidator.validateCountIsEnough(giftCertificate, requestCount));
    }

    @Test
    void validateStateIsActual_doNothing_ifValid() {
        Long inputId = 3L;

        GiftCertificateDto foundCertificateDto = GiftCertificateDto.builder()
                .id(inputId)
                .state(ActualityStateDto.ACTUAL)
                .build();

        when(giftCertificateService.findById(inputId)).thenReturn(foundCertificateDto);

        assertDoesNotThrow(()->giftCertificateValidator.validateStateIsActual(inputId));

        verify(giftCertificateService, times(0)).findActualId(any());
    }

    @Test
    void validateStateIsActual_throwException_ifInValid_successorIsNull() {
        Long inputId = 3L;

        GiftCertificateDto foundCertificateDto = GiftCertificateDto.builder()
                .id(inputId)
                .state(ActualityStateDto.ARCHIVED)
                .successorId(null)
                .build();

        when(giftCertificateService.findById(inputId)).thenReturn(foundCertificateDto);

        assertThrows(EntitiesArchivedException.class, ()->giftCertificateValidator.validateStateIsActual(inputId));

        verify(giftCertificateService, times(0)).findActualId(any());
    }

    @Test
    void validateStateIsActual_throwException_ifInValid_successorNotNull() {
        Long inputId = 3L;
        Long successorId = 5L;

        GiftCertificateDto foundCertificateDto = GiftCertificateDto.builder()
                .id(inputId)
                .state(ActualityStateDto.ARCHIVED)
                .successorId(successorId)
                .build();

        when(giftCertificateService.findById(inputId)).thenReturn(foundCertificateDto);

        assertThrows(EntitiesArchivedException.class, ()->giftCertificateValidator.validateStateIsActual(inputId));

        verify(giftCertificateService, times(1)).findActualId(any());
    }
}