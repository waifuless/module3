package com.epam.esm.gcs.web.assembler;

import com.epam.esm.gcs.business.dto.AppUserDto;
import org.springframework.hateoas.server.RepresentationModelAssembler;

public interface AppUserRepresentationAssembler extends RepresentationModelAssembler<AppUserDto, AppUserDto> {
}
