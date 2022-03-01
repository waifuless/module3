package com.epam.esm.gcs.web.assembler;

import com.epam.esm.gcs.business.dto.UserOrderDto;
import org.springframework.hateoas.server.RepresentationModelAssembler;

/**
 * Assembler for UserOrderDto
 */
public interface UserOrderRepresentationAssembler extends RepresentationModelAssembler<UserOrderDto, UserOrderDto> {
}
