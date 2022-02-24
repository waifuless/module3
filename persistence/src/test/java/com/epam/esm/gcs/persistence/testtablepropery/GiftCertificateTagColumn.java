package com.epam.esm.gcs.persistence.testtablepropery;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum GiftCertificateTagColumn {

    GIFT_CERTIFICATE_ID("gift_certificate_id"),
    TAG_ID("tag_id");

    private final String columnName;
}
