package com.epam.esm.gcs.business.service.impl;

import com.epam.esm.gcs.business.config.ModelMapperConfig;
import com.epam.esm.gcs.business.converter.GiftCertificateContextDtoConverter;
import com.epam.esm.gcs.business.converter.GiftCertificateDtoConverter;
import com.epam.esm.gcs.business.converter.GiftCertificateModelConverter;
import org.modelmapper.ModelMapper;

import java.util.Collections;

public class ModelMapperTestConfig {

    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapperConfig().modelMapper(Collections.emptyList());
        modelMapper.addConverter(new GiftCertificateContextDtoConverter());
        modelMapper.addConverter(new GiftCertificateDtoConverter(modelMapper));
        modelMapper.addConverter(new GiftCertificateModelConverter(modelMapper));
        return modelMapper;
    }
}
