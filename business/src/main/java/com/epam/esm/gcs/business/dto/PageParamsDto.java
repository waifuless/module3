package com.epam.esm.gcs.business.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageParamsDto {

    private static final int DEFAULT_PAGE_SIZE = 10;

    @NotNull(message = "{parameter.is.null}")
    @Positive(message = "{parameter.not.positive}")
    private Integer page;

    @NotNull(message = "{parameter.is.null}")
    @Positive(message = "{parameter.not.positive}")
    @Max(value = 50, message = "{parameter.field.max}")
    private Integer size = DEFAULT_PAGE_SIZE;
}
