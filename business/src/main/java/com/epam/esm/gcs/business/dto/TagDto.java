package com.epam.esm.gcs.business.dto;

import com.epam.esm.gcs.business.dto.group.OnTagCreate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;


@Data
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class TagDto {

    @Null(groups = OnTagCreate.class, message = "violation.entity.field.should.be.null")
    @Positive(message = "{entity.field.not.positive}")
    private Long id;

    @NotBlank(message = "{entity.field.blank}")
    @Size(max = 100, message = "{entity.field.size.max}")
    private String name;
}
