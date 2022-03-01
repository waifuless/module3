package com.epam.esm.gcs.web.assembler;

import com.epam.esm.gcs.business.dto.PageDto;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;

/**
 * Paged assembler for some Representation model
 */
public interface PagedRepresentationAssembler<T extends RepresentationModel<?>> {

    /**
     * Makes PagedModel by page and assembler
     *
     * @param page      - page of some dtos
     * @param assembler - assembler for dto
     * @return - constructed pagedModel
     */
    PagedModel<T> toModel(PageDto<T> page, RepresentationModelAssembler<T, T> assembler);
}
