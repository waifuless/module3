package com.epam.esm.gcs.business.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
public class AppUserDto {
    //todo: add validations if app user will be added (not only get operation)

    private Long id;
    private String email;
}
