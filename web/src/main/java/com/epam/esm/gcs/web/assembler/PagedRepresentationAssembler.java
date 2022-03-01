package com.epam.esm.gcs.web.assembler;

import com.epam.esm.gcs.business.dto.PageDto;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;

public interface PagedRepresentationAssembler<T extends RepresentationModel<?>> {

    PagedModel<T> toModel(PageDto<T> page, RepresentationModelAssembler<T, T> assembler);
}
