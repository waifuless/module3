package com.epam.esm.gcs.business.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;


@Data
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class TagDto {

    @Null(message = "{entity.field.should.be.null}")
    private Long id;

    @NotBlank(message = "{entity.field.blank}")
    @Size(max = 100, message = "{entity.field.size.max}")
    private String name;
}
