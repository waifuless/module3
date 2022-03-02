package com.epam.esm.gcs.business.validation.impl;

import com.epam.esm.gcs.business.dto.UserOrderDto;
import com.epam.esm.gcs.business.exception.EntitiesArchivedException;
import com.epam.esm.gcs.business.exception.GiftCertificateCountsNotEnoughException;
import com.epam.esm.gcs.business.service.GiftCertificateService;
import com.epam.esm.gcs.business.validation.UserOrderValidator;
import com.epam.esm.gcs.persistence.model.ActualityStateModel;
import com.epam.esm.gcs.persistence.model.GiftCertificateModel;
import com.epam.esm.gcs.persistence.model.UserOrderPositionModel;
import lombok.RequiredArgsConstructor;
import org.modelmapper.internal.Pair;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UserOrderValidatorImpl implements UserOrderValidator {

    private final GiftCertificateService giftCertificateService;

    @Override
    public void validateStates(List<UserOrderPositionModel> positions) {
        List<Pair<Long, Long>> archivedToActual = new ArrayList<>();
        List<Long> unavailable = new ArrayList<>();
        for (UserOrderPositionModel position : positions) {
            GiftCertificateModel giftCertificate = position.getGiftCertificate();
            if (ActualityStateModel.ARCHIVED.equals(giftCertificate.getState())) {
                Long archivedId = giftCertificate.getId();
                if (giftCertificate.getSuccessor() != null) {
                    giftCertificateService.findActualId(archivedId)
                            .ifPresent(actualId -> archivedToActual.add(Pair.of(archivedId, actualId)));
                } else {
                    unavailable.add(archivedId);
                }
            }
        }
        if (!archivedToActual.isEmpty() || !unavailable.isEmpty()) {
            throw new EntitiesArchivedException(UserOrderDto.class, archivedToActual, unavailable);
        }
    }

    @Override
    public void validateCountsAreEnough(List<UserOrderPositionModel> positions) {
        List<Long> giftCertificateIdsWithNotEnoughCount = new ArrayList<>();
        positions.forEach(position -> {
            GiftCertificateModel giftCertificate = position.getGiftCertificate();
            if (giftCertificate.getCount() < position.getCount()) {
                giftCertificateIdsWithNotEnoughCount.add(giftCertificate.getId());
            }
        });

        if (!giftCertificateIdsWithNotEnoughCount.isEmpty()) {
            throw new GiftCertificateCountsNotEnoughException(giftCertificateIdsWithNotEnoughCount);
        }
    }
}
