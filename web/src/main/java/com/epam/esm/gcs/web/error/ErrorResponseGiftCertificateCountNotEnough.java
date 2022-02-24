package com.epam.esm.gcs.web.error;

import com.epam.esm.gcs.business.exception.GiftCertificateCountsNotEnoughException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
public class ErrorResponseGiftCertificateCountNotEnough extends ErrorResponse {

    private List<Long> giftCertificateIdsWithNotEnoughCount;

    public ErrorResponseGiftCertificateCountNotEnough(String errorMessage, HttpStatus errorCode,
                                                      Class<?> targetClass,
                                                      GiftCertificateCountsNotEnoughException ex) {
        super(errorMessage, errorCode, targetClass);

        this.giftCertificateIdsWithNotEnoughCount = ex.getGiftCertificateIdsWithNotEnoughCount();
    }
}
