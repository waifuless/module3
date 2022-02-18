package com.epam.esm.gcs.business.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
//todo: write converter to model with setting appUser field
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
public class UserOrderPositionDto {

    private GiftCertificateDto giftCertificate;
    private Integer count;
}
