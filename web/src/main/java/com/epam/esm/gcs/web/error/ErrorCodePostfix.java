package com.epam.esm.gcs.web.error;

import com.epam.esm.gcs.business.dto.AppUserDto;
import com.epam.esm.gcs.business.dto.GiftCertificateDto;
import com.epam.esm.gcs.business.dto.TagDto;
import com.epam.esm.gcs.business.dto.UserOrderDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum ErrorCodePostfix {
    TAG("01", TagDto.class),
    GIFT_CERTIFICATE("02", GiftCertificateDto.class),
    APP_USER("03", AppUserDto.class),
    USER_ORDER("04", UserOrderDto.class),
    OTHER("00", Object.class);

    private final String postfix;
    private final Class<?> classForPostfix;

    public static String findPostfixByClass(Class<?> classForPostfix) {
        return Arrays.stream(values())
                .filter(errorCodePostfix -> errorCodePostfix.classForPostfix.equals(classForPostfix))
                .findAny()
                .map(ErrorCodePostfix::getPostfix)
                .orElseGet(OTHER::getPostfix);
    }
}
