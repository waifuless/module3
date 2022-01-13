package com.epam.esm.gcs.business.dto;

import lombok.*;

@Value
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class TagDto {

    Long id;
    String name;
}
