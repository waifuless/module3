package com.epam.esm.gcs.business.service;

import com.epam.esm.gcs.business.dto.AppUserDto;
import com.epam.esm.gcs.business.dto.PageDto;
import com.epam.esm.gcs.business.dto.PageParamsDto;
import com.epam.esm.gcs.business.dto.UserOrderDto;

import java.util.List;

public interface AppUserService extends ReadService<AppUserDto> {

    List<AppUserDto> findUsersWithHighestPriceAmountOfAllOrders();

    PageDto<UserOrderDto> findUserOrders(Long userId, PageParamsDto pageParamsDto);
}
