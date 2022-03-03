package com.epam.esm.gcs.business.dto;

import com.epam.esm.gcs.business.dto.group.OnUserOrderCreate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class AppUserDto extends RepresentationModel<AppUserDto> {

    @Positive(message = "{entity.field.not.positive}")
    @NotNull(groups = OnUserOrderCreate.class, message = "violation.entity.field.null")
    private Long id;

    private String email;
}
