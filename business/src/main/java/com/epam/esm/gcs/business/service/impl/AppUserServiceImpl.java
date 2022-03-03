package com.epam.esm.gcs.business.service.impl;

import com.epam.esm.gcs.business.dto.AppUserDto;
import com.epam.esm.gcs.business.dto.PageDto;
import com.epam.esm.gcs.business.dto.PageParamsDto;
import com.epam.esm.gcs.business.dto.UserOrderDto;
import com.epam.esm.gcs.business.service.AppUserService;
import com.epam.esm.gcs.persistence.model.AppUserModel;
import com.epam.esm.gcs.persistence.model.PageModel;
import com.epam.esm.gcs.persistence.model.PageParamsModel;
import com.epam.esm.gcs.persistence.model.UserOrderModel;
import com.epam.esm.gcs.persistence.repository.AppUserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppUserServiceImpl extends AbstractReadService<AppUserDto, AppUserModel> implements AppUserService {

    private final AppUserRepository appUserRepository;
    private final ModelMapper modelMapper;

    public AppUserServiceImpl(AppUserRepository repository, ModelMapper modelMapper) {
        super(repository, modelMapper, AppUserDto.class);

        this.appUserRepository = repository;
        this.modelMapper = modelMapper;
    }

    @Override
    public PageDto<UserOrderDto> findUserOrders(Long userId, PageParamsDto pageParamsDto) {
        PageParamsModel pageParams = modelMapper.map(pageParamsDto, PageParamsModel.class);

        PageModel<UserOrderModel> foundPage = appUserRepository.findUserOrders(userId, pageParams);
        List<UserOrderDto> contentDto = foundPage.getContent().stream()
                .map(order -> modelMapper.map(order, UserOrderDto.class))
                .collect(Collectors.toList());
        return new PageDto<>(contentDto, pageParamsDto, foundPage.getTotalCount());
    }
}
