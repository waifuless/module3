package com.epam.esm.gcs.business.converter;

import com.epam.esm.gcs.business.dto.ActualityStateDto;
import com.epam.esm.gcs.business.dto.GiftCertificateDto;
import com.epam.esm.gcs.business.dto.TagDto;
import com.epam.esm.gcs.persistence.model.ActualityStateModel;
import com.epam.esm.gcs.persistence.model.GiftCertificateModel;
import com.epam.esm.gcs.persistence.model.TagModel;
import lombok.RequiredArgsConstructor;
import org.modelmapper.AbstractConverter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor(onConstructor_ = @Lazy)
public class GiftCertificateDtoConverter extends
        AbstractConverter<GiftCertificateDto, GiftCertificateModel> {

    private final ModelMapper modelMapper;

    @Override
    protected GiftCertificateModel convert(GiftCertificateDto source) {
        return GiftCertificateModel.builder()
                .id(source.getId())
                .name(source.getName())
                .description(source.getDescription())
                .price(source.getPrice())
                .duration(source.getDuration())
                .createDate(source.getCreateDate())
                .lastUpdateDate(source.getLastUpdateDate())
                .state(convertState(source.getState()))
                .count(source.getCount())
                .successor(convertSuccessor(source.getSuccessorId()))
                .tags(convertTags(source.getTags()))
                .build();
    }

    private ActualityStateModel convertState(ActualityStateDto source) {
        if (source == null) {
            return null;
        }
        return ActualityStateModel.valueOf(source.name());
    }

    private GiftCertificateModel convertSuccessor(Long successorId) {
        if (successorId == null) {
            return null;
        }
        return GiftCertificateModel.builder()
                .id(successorId)
                .build();
    }

    private List<TagModel> convertTags(List<TagDto> source) {
        if (source == null) {
            return null;
        }
        return source.stream()
                .map(tagDto -> modelMapper.map(tagDto, TagModel.class))
                .collect(Collectors.toList());
    }
}
