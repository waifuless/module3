package com.epam.esm.gcs.business.config;

import com.epam.esm.gcs.business.converter.GiftCertificateContextConverter;
import com.epam.esm.gcs.business.dto.GiftCertificateDtoContext;
import com.epam.esm.gcs.persistence.model.GiftCertificateModelContext;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration.AccessLevel;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setFieldMatchingEnabled(true)
                .setSkipNullEnabled(true)
                .setFieldAccessLevel(AccessLevel.PRIVATE);
        modelMapper.addConverter(contextConverter());
        return modelMapper;
    }

    @Bean
    public Converter<GiftCertificateDtoContext, GiftCertificateModelContext> contextConverter() {
        return new GiftCertificateContextConverter();
    }
}
