package com.epam.esm.gcs.business.service.impl;

import com.epam.esm.gcs.business.dto.AppUserDto;
import com.epam.esm.gcs.business.service.AppUserService;
import com.epam.esm.gcs.persistence.model.AppUserModel;
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
    public List<AppUserDto> findUsersWithHighestPriceAmountOfAllOrders() {
        return appUserRepository.findUsersWithHighestPriceAmountOfAllOrders()
                .stream()
                .map(model -> modelMapper.map(model, AppUserDto.class))
                .collect(Collectors.toList());
    }
}
