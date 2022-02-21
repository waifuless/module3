package com.epam.esm.gcs.persistence.converter;

import com.epam.esm.gcs.persistence.model.ActualityStateModel;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Arrays;

@Converter(autoApply = true)
public class StateConverter implements AttributeConverter<ActualityStateModel, Long> {

    @Override
    public Long convertToDatabaseColumn(ActualityStateModel attribute) {
        return attribute.getId();
    }

    @Override
    public ActualityStateModel convertToEntityAttribute(Long dbData) {
        return Arrays.stream(ActualityStateModel.values())
                .filter(actualityStateModel -> actualityStateModel.getId().equals(dbData))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }
}
