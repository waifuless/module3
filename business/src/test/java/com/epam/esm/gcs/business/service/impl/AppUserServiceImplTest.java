package com.epam.esm.gcs.business.service.impl;

import com.epam.esm.gcs.business.dto.PageParamsDto;
import com.epam.esm.gcs.business.service.AppUserService;
import com.epam.esm.gcs.persistence.model.PageModel;
import com.epam.esm.gcs.persistence.model.PageParamsModel;
import com.epam.esm.gcs.persistence.model.UserOrderModel;
import com.epam.esm.gcs.persistence.repository.AppUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AppUserServiceImplTest {

    private final AppUserService appUserService;
    private final AppUserRepository appUserRepository;

    public AppUserServiceImplTest(@Mock AppUserRepository appUserRepository) {
        this.appUserService = new AppUserServiceImpl(appUserRepository,
                new ModelMapperTestConfig().modelMapper());

        this.appUserRepository = appUserRepository;
    }

    @Test
    void findUserOrders_invokeSuchRepositoryMethod() {
        Long userId = 33L;
        Integer page = 1;
        Integer size = 22;
        PageParamsDto pageParamsDto = new PageParamsDto(page, size);
        PageParamsModel pageParamsModel = new PageParamsModel(page, size);
        PageModel<UserOrderModel> returnedPageModel = PageModel.<UserOrderModel>builder()
                .content(new ArrayList<>())
                .build();
        when(appUserRepository.findUserOrders(userId, pageParamsModel)).thenReturn(returnedPageModel);

        appUserService.findUserOrders(userId, pageParamsDto);

        verify(appUserRepository, times(1)).findUserOrders(userId, pageParamsModel);
    }
}