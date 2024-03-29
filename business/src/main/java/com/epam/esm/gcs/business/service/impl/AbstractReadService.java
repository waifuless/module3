package com.epam.esm.gcs.business.service.impl;

import com.epam.esm.gcs.business.exception.EntityNotFoundException;
import com.epam.esm.gcs.business.service.ReadService;
import com.epam.esm.gcs.persistence.repository.ReadRepository;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @param <D> - Dto class
 * @param <M> - Model class
 */
public abstract class AbstractReadService<D, M> implements ReadService<D> {

    private static final String ID_FIELD = "id";

    private final ReadRepository<M> readRepository;
    private final ModelMapper modelMapper;
    private final Class<D> dtoClass;

    public AbstractReadService(ReadRepository<M> readRepository, ModelMapper modelMapper, Class<D> dtoClass) {
        this.readRepository = readRepository;
        this.modelMapper = modelMapper;
        this.dtoClass = dtoClass;
    }

    @Override
    public D findById(Long id) {
        M model = readRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(dtoClass, ID_FIELD, String.valueOf(id)));
        return modelMapper.map(model, dtoClass);
    }

    @Override
    public List<D> findAll() {
        return readRepository.findAll().stream()
                .map(model -> modelMapper.map(model, dtoClass))
                .collect(Collectors.toList());
    }
}
