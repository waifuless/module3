package com.epam.esm.gcs.business.service.impl;

import com.epam.esm.gcs.business.dto.AppUserDto;
import com.epam.esm.gcs.business.service.AppUserService;
import com.epam.esm.gcs.persistence.model.AppUserModel;
import com.epam.esm.gcs.persistence.repository.AppUserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class AppUserServiceImpl extends AbstractReadService<AppUserDto, AppUserModel> implements AppUserService {

    public AppUserServiceImpl(AppUserRepository repository, ModelMapper modelMapper) {
        super(repository, modelMapper, AppUserDto.class);
    }
}
