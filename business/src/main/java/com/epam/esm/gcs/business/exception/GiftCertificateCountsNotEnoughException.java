package com.epam.esm.gcs.business.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class GiftCertificateCountsNotEnoughException extends RuntimeException {

    private final List<Long> giftCertificateIdsWithNotEnoughCount;
}
