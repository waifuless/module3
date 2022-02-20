package com.epam.esm.gcs.business.service.impl;

import com.epam.esm.gcs.business.dto.GiftCertificateDto;
import com.epam.esm.gcs.business.dto.GiftCertificateDtoContext;
import com.epam.esm.gcs.business.dto.TagDto;
import com.epam.esm.gcs.business.exception.EntityNotFoundException;
import com.epam.esm.gcs.business.service.GiftCertificateService;
import com.epam.esm.gcs.business.service.TagService;
import com.epam.esm.gcs.business.validation.GiftCertificateValidator;
import com.epam.esm.gcs.persistence.model.GiftCertificateModel;
import com.epam.esm.gcs.persistence.model.GiftCertificateModelContext;
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
    private final GiftCertificateValidator giftCertificateValidator;

    @Override
    public GiftCertificateDto findById(Long id) {
        GiftCertificateModel giftCertificate = giftCertificateRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(GiftCertificateDto.class,
                        ID_FIELD, String.valueOf(id)));
        return modelMapper.map(giftCertificate, GiftCertificateDto.class);
    }

    @Override
    @Transactional
    public GiftCertificateDto create(GiftCertificateDto giftCertificateDto) {
        GiftCertificateModel giftCertificate = modelMapper.map(giftCertificateDto, GiftCertificateModel.class);
        List<TagModel> tags = giftCertificate.getTags();
        tags = prepareTags(tags);
        giftCertificate.setTags(tags);
        GiftCertificateModel createdGiftCertificate = giftCertificateRepository.create(giftCertificate);
        return modelMapper.map(createdGiftCertificate, GiftCertificateDto.class);
    }

    @Override
    public List<GiftCertificateDto> findAll(GiftCertificateDtoContext context) {
        GiftCertificateModelContext modelContext = modelMapper.map(context, GiftCertificateModelContext.class);
        return giftCertificateRepository.findAll(modelContext).stream()
                .map(model -> modelMapper.map(model, GiftCertificateDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public void addCount(Long id, Integer countToAdd) {
        GiftCertificateDto foundGiftCertificateDto = findById(id);

        Integer currentCount = foundGiftCertificateDto.getCount();
        Integer newCount = currentCount + countToAdd;
        giftCertificateRepository.updateCount(id, newCount);
    }

    @Override
    public void reduceCount(Long id, Integer countToReduce) {
        GiftCertificateDto foundGiftCertificateDto = findById(id);
        GiftCertificateModel foundGiftCertificate = modelMapper
                .map(foundGiftCertificateDto, GiftCertificateModel.class);

        giftCertificateValidator.validateCountIsEnough(foundGiftCertificate, countToReduce);

        Integer currentCount = foundGiftCertificate.getCount();
        Integer newCount = currentCount - countToReduce;
        giftCertificateRepository.updateCount(id, newCount);
    }

    @Override
    public Optional<Long> findActualId(Long id) {
        return giftCertificateRepository.findActualId(id);
    }

    @Override
    public void archive(Long id) {
        if (!giftCertificateRepository.existsById(id)) {
            throw new EntityNotFoundException(GiftCertificateDto.class, ID_FIELD, String.valueOf(id));
        }
        giftCertificateRepository.archive(id);
    }

    @Override
    @Transactional
    public GiftCertificateDto archiveAndCreateSuccessor(Long idToArchive, GiftCertificateDto modifications) {
        if (!giftCertificateRepository.existsById(idToArchive)) {
            throw new EntityNotFoundException(GiftCertificateDto.class, ID_FIELD, String.valueOf(idToArchive));
        }
        GiftCertificateModel giftCertificate = modelMapper.map(modifications, GiftCertificateModel.class);

        if (giftCertificate.getTags() != null && !giftCertificate.getTags().isEmpty()) {
            List<TagModel> tags = giftCertificate.getTags();
            tags = prepareTags(tags);
            giftCertificate.setTags(tags);
        }
        GiftCertificateModel successor =
                giftCertificateRepository.archiveAndCreateSuccessor(idToArchive, giftCertificate);

        return modelMapper.map(successor, GiftCertificateDto.class);
    }

    private List<TagModel> prepareTags(List<TagModel> tags) {
        return tags.stream()
                .map(tagModel -> {
                    TagDto tagToFind = modelMapper.map(tagModel, TagDto.class);
                    TagDto preparedTag = tagService.findOrCreate(tagToFind);
                    return modelMapper.map(preparedTag, TagModel.class);
                })
                .distinct()
                .collect(Collectors.toList());
    }
}
