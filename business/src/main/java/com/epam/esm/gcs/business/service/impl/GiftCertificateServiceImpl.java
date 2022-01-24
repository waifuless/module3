package com.epam.esm.gcs.business.service.impl;

import com.epam.esm.gcs.business.dto.GiftCertificateDto;
import com.epam.esm.gcs.business.dto.GiftCertificateDtoContext;
import com.epam.esm.gcs.business.dto.TagDto;
import com.epam.esm.gcs.business.exception.EntityNotFoundException;
import com.epam.esm.gcs.business.service.GiftCertificateService;
import com.epam.esm.gcs.business.service.TagService;
import com.epam.esm.gcs.persistence.model.GiftCertificateModel;
import com.epam.esm.gcs.persistence.model.TagModel;
import com.epam.esm.gcs.persistence.repository.GiftCertificateRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private final static String ID_FIELD = "id";

    private final TagService tagService;
    private final GiftCertificateRepository giftCertificateRepository;
    private final ModelMapper modelMapper;

    @Override
    public GiftCertificateDto findById(Long id) {
        GiftCertificateModel giftCertificate = giftCertificateRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(GiftCertificateDto.class,
                        ID_FIELD, String.valueOf(id)));
        return modelMapper.map(giftCertificate, GiftCertificateDto.class);
    }

    @Override
    public List<GiftCertificateDto> findAll() {
        return giftCertificateRepository.findAll().stream()
                .map(model -> modelMapper.map(model, GiftCertificateDto.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional //todo: make enabletransactionmanagement
    public GiftCertificateDto create(GiftCertificateDto giftCertificateDto) {
        GiftCertificateModel giftCertificate = modelMapper.map(giftCertificateDto, GiftCertificateModel.class);
        List<TagModel> tags = giftCertificate.getTags();
        tags = tags.stream()
                .map(this::findTagOrCreate)
                .collect(Collectors.toList());
        giftCertificate.setTags(tags);
        GiftCertificateModel createdGiftCertificate = giftCertificateRepository.create(giftCertificate);
        return modelMapper.map(createdGiftCertificate, GiftCertificateDto.class);
    }

    @Override
    public void delete(Long id) {
        giftCertificateRepository.delete(id);
    }

    @Override
    public void updateById(Long id, GiftCertificateDto dto) {
        GiftCertificateModel giftCertificateModel = modelMapper.map(dto, GiftCertificateModel.class);
        giftCertificateRepository.updateById(id, giftCertificateModel);
    }

    @Override
    public List<GiftCertificateDto> findAll(GiftCertificateDtoContext context) {
        //todo: make model context with fields of sort??
        return null;
    }

    //todo: move to tagService
    private TagModel findTagOrCreate(TagModel tag) {
        Optional<TagModel> optionalTag =
                tagService.findByName(tag.getName())
                        .map(tagDto -> modelMapper.map(tagDto, TagModel.class));
        return optionalTag.orElseGet(() -> {
            TagDto createdTag = tagService.create(new TagDto(null, tag.getName()));
            return modelMapper.map(createdTag, TagModel.class);
        });
    }
}
