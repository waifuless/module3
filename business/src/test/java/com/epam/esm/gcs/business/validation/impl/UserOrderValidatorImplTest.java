package com.epam.esm.gcs.business.validation.impl;

import com.epam.esm.gcs.business.exception.EntitiesArchivedException;
import com.epam.esm.gcs.business.exception.GiftCertificateCountsNotEnoughException;
import com.epam.esm.gcs.business.service.GiftCertificateService;
import com.epam.esm.gcs.business.validation.UserOrderValidator;
import com.epam.esm.gcs.persistence.model.ActualityStateModel;
import com.epam.esm.gcs.persistence.model.GiftCertificateModel;
import com.epam.esm.gcs.persistence.model.UserOrderPositionModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserOrderValidatorImplTest {

    private final UserOrderValidator userOrderValidator;
    private final GiftCertificateService giftCertificateService;

    public UserOrderValidatorImplTest(@Mock GiftCertificateService giftCertificateService) {
        this.userOrderValidator = new UserOrderValidatorImpl(giftCertificateService);
        this.giftCertificateService = giftCertificateService;
    }

    @Test
    void validateStates_doNothing_ifValid() {
        Long certificateId1 = 3L;
        Long certificateId2 = 4L;

        GiftCertificateModel certificate1 = GiftCertificateModel.builder()
                .id(certificateId1)
                .state(ActualityStateModel.ACTUAL)
                .build();
        Integer positionCount1 = 20;

        GiftCertificateModel certificate2 = GiftCertificateModel.builder()
                .id(certificateId2)
                .state(ActualityStateModel.ACTUAL)
                .build();
        Integer positionCount2 = 30;

        List<UserOrderPositionModel> inputPositions =
                List.of(UserOrderPositionModel.builder().giftCertificate(certificate1).count(positionCount1).build(),
                        UserOrderPositionModel.builder().giftCertificate(certificate2).count(positionCount2).build());

        assertDoesNotThrow(() -> userOrderValidator.validateStates(inputPositions));

        verify(giftCertificateService, times(0)).findActualId(any());
    }

    @Test
    void validateStates_throwException_ifInValid() {
        Long actualCertificateId = 3L;
        Long archivedCertificateId = 4L;

        GiftCertificateModel actualCertificate = GiftCertificateModel.builder()
                .id(actualCertificateId)
                .state(ActualityStateModel.ACTUAL)
                .build();
        Integer positionCount1 = 20;

        GiftCertificateModel archivedCertificateWithSuccessor = GiftCertificateModel.builder()
                .id(archivedCertificateId)
                .state(ActualityStateModel.ARCHIVED)
                .successor(GiftCertificateModel.builder().build())
                .build();
        Integer positionCount2 = 30;

        List<UserOrderPositionModel> inputPositions =
                List.of(UserOrderPositionModel.builder()
                                .giftCertificate(actualCertificate)
                                .count(positionCount1)
                                .build(),
                        UserOrderPositionModel.builder()
                                .giftCertificate(archivedCertificateWithSuccessor)
                                .count(positionCount2)
                                .build());

        when(giftCertificateService.findActualId(archivedCertificateId)).thenReturn(Optional.of(10L));

        assertThrows(EntitiesArchivedException.class, () -> userOrderValidator.validateStates(inputPositions));

        verify(giftCertificateService, times(1)).findActualId(archivedCertificateId);
    }

    @Test
    void validateCountsAreEnough_doNothing_ifValid() {
        GiftCertificateModel giftCertificate = GiftCertificateModel.builder()
                .id(1L)
                .count(100)
                .build();
        Integer requestCount = 99;
        List<UserOrderPositionModel> positions = List.of(
                UserOrderPositionModel.builder()
                        .giftCertificate(giftCertificate)
                        .count(requestCount)
                        .build());

        assertDoesNotThrow(() -> userOrderValidator.validateCountsAreEnough(positions));
    }

    @Test
    void validateCountsAreEnough_throwException_ifInValid() {
        GiftCertificateModel giftCertificate = GiftCertificateModel.builder()
                .id(1L)
                .count(100)
                .build();
        Integer requestCount = 202;
        List<UserOrderPositionModel> positions = List.of(
                UserOrderPositionModel.builder()
                        .giftCertificate(giftCertificate)
                        .count(requestCount)
                        .build());

        assertThrows(GiftCertificateCountsNotEnoughException.class,
                () -> userOrderValidator.validateCountsAreEnough(positions));
    }
}