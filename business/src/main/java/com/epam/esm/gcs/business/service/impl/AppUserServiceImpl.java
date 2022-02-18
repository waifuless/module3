package com.epam.esm.gcs.business.service.impl;

import com.epam.esm.gcs.business.dto.AppUserDto;
import com.epam.esm.gcs.business.exception.EntityNotFoundException;
import com.epam.esm.gcs.business.service.AppUserService;
import com.epam.esm.gcs.persistence.model.AppUserModel;
import com.epam.esm.gcs.persistence.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppUserServiceImpl implements AppUserService {

    private final static String ID_FIELD = "id";

    private final AppUserRepository appUserRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<AppUserDto> findAll() {
        return appUserRepository.findAll().stream()
                .map(model -> modelMapper.map(model, AppUserDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public AppUserDto findById(Long id) {
        AppUserModel appUser = appUserRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(AppUserDto.class, ID_FIELD, String.valueOf(id)));
        return modelMapper.map(appUser, AppUserDto.class);
    }
}
