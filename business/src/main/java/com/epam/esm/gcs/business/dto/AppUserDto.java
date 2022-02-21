package com.epam.esm.gcs.business.dto;

import com.epam.esm.gcs.business.dto.group.OnUserOrderCreate;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
public class AppUserDto {

    @Positive(message = "{entity.field.not.positive}")
    @NotNull(groups = OnUserOrderCreate.class, message = "violation.entity.field.null")
    private Long id;

    private String email;
}
