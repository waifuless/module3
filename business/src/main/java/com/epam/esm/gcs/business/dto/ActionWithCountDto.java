package com.epam.esm.gcs.business.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActionWithCountDto {

    @NotNull(message = "{parameter.is.null}")
    private Mode mode;

    @NotNull(message = "{parameter.is.null}")
    @Positive(message = "{parameter.not.positive}")
    private Integer count;

    public enum Mode {
        ADD, REDUCE
    }
}
