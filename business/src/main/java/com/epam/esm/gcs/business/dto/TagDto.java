package com.epam.esm.gcs.business.dto;

import com.epam.esm.gcs.business.dto.group.OnTagCreate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import javax.validation.constraints.Positive;


@Data
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class TagDto {

    @Null(groups = OnTagCreate.class, message = "violation.entity.field.should.be.null")
    @Positive(message = "{entity.field.not.positive}")
    private Long id;

    @NotBlank(groups = OnTagCreate.class, message = "violation.entity.field.blank")
    @Length(max = 100, message = "{entity.field.length.max}")
    private String name;
}
