package com.epam.esm.gcs.business.validation.impl;

import com.epam.esm.gcs.business.dto.GiftCertificateDto;
import com.epam.esm.gcs.business.exception.EntitiesArchivedException;
import com.epam.esm.gcs.business.exception.GiftCertificateCountsNotEnoughException;
import com.epam.esm.gcs.business.service.GiftCertificateService;
import com.epam.esm.gcs.business.validation.GiftCertificateValidator;
import com.epam.esm.gcs.persistence.model.ActualityStateModel;
import com.epam.esm.gcs.persistence.model.GiftCertificateModel;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.internal.Pair;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor(onConstructor_ = @Lazy)
public class GiftCertificateValidatorImpl implements GiftCertificateValidator {

    private final GiftCertificateService giftCertificateService;
    private final ModelMapper modelMapper;

    @Override
    public void validateCountIsEnough(GiftCertificateModel giftCertificate, Integer requestedCount) {
        if (giftCertificate.getCount() < requestedCount) {
            throw new GiftCertificateCountsNotEnoughException(List.of(giftCertificate.getId()));
        }
    }

    @Override
    public void validateStateForArchiveAndCreateSuccessor(Long idToArchive) {
        GiftCertificateDto giftCertificateToArchiveDto = giftCertificateService.findById(idToArchive);
        GiftCertificateModel giftCertificateToArchive = modelMapper
                .map(giftCertificateToArchiveDto, GiftCertificateModel.class);

        if (giftCertificateToArchive.getState().equals(ActualityStateModel.ARCHIVED)) {
            List<Pair<Long, Long>> archivedToActual = new ArrayList<>();
            List<Long> unavailable = new ArrayList<>();
            if (giftCertificateToArchive.getSuccessor() != null) {
                giftCertificateService.findActualId(idToArchive)
                        .ifPresent(actualId -> archivedToActual.add(Pair.of(idToArchive, actualId)));
            } else {
                unavailable.add(idToArchive);
            }
            throw new EntitiesArchivedException(GiftCertificateDto.class, archivedToActual, unavailable);
        }
    }
}
