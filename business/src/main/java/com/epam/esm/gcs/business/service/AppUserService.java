package com.epam.esm.gcs.business.service;

import com.epam.esm.gcs.business.dto.AppUserDto;
import com.epam.esm.gcs.business.dto.PageDto;
import com.epam.esm.gcs.business.dto.PageParamsDto;
import com.epam.esm.gcs.business.dto.UserOrderDto;

import java.util.List;

/**
 * Interface for interaction with AppUser
 */
public interface AppUserService extends ReadService<AppUserDto> {

    /**
     * Finds page of orders by specified user
     *
     * @param userId        - id of user to search
     * @param pageParamsDto - page parameters
     * @return page of orders by specified user
     */
    PageDto<UserOrderDto> findUserOrders(Long userId, PageParamsDto pageParamsDto);
}
