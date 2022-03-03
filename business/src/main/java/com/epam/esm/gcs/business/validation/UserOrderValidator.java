package com.epam.esm.gcs.business.validation;

import com.epam.esm.gcs.business.exception.EntitiesArchivedException;
import com.epam.esm.gcs.business.exception.GiftCertificateCountsNotEnoughException;
import com.epam.esm.gcs.persistence.model.UserOrderPositionModel;

import java.util.List;

/**
 * Validator for UserOrder
 */
public interface UserOrderValidator {

    /**
     * Checks states of positions. Throw exception if at least one giftCertificate is archived. Exception
     * contains list of archived models with it actual successors
     *
     * @param positions - list of positions in UserOrderModel
     * @throws EntitiesArchivedException if at least one giftCertificate is archived
     */
    void validateStates(List<UserOrderPositionModel> positions);

    /**
     * Checks that count of giftCertificates are enough for given UserOrderPositions
     *
     * @param positions - validation target
     * @throws GiftCertificateCountsNotEnoughException - if count of any giftCertificate is not enough for
     *                                                 UserOrderPosition
     */
    void validateCountsAreEnough(List<UserOrderPositionModel> positions);
}