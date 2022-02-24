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
public class GiftCertificateModelConverter extends
        AbstractConverter<GiftCertificateModel, GiftCertificateDto> {

    private final ModelMapper modelMapper;

    @Override
    protected GiftCertificateDto convert(GiftCertificateModel source) {
        return GiftCertificateDto.builder()
                .id(source.getId())
                .name(source.getName())
                .description(source.getDescription())
                .price(source.getPrice())
                .duration(source.getDuration())
                .createDate(source.getCreateDate())
                .lastUpdateDate(source.getLastUpdateDate())
                .state(convertState(source.getState()))
                .count(source.getCount())
                .successorId(convertSuccessor(source.getSuccessor()))
                .tags(convertTags(source.getTags()))
                .build();
    }

    private ActualityStateDto convertState(ActualityStateModel source) {
        if (source == null) {
            return null;
        }
        return ActualityStateDto.valueOf(source.name());
    }

    private Long convertSuccessor(GiftCertificateModel successor) {
        if (successor == null) {
            return null;
        }
        return successor.getId();
    }

    private List<TagDto> convertTags(List<TagModel> source) {
        if (source == null) {
            return null;
        }
        return source.stream()
                .map(tagModel -> modelMapper.map(tagModel, TagDto.class))
                .collect(Collectors.toList());
    }
}
