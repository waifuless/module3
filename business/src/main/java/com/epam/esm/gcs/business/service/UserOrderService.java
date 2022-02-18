package com.epam.esm.gcs.business.service;

import com.epam.esm.gcs.business.dto.UserOrderDto;

import java.util.List;

public interface UserOrderService extends CrdService<UserOrderDto> {

    /**
     * Finds all UserOrderDtos
     *
     * @return List of all UserOrderDtos
     */
    List<UserOrderDto> findAll();
}
