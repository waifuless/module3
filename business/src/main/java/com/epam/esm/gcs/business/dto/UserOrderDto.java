package com.epam.esm.gcs.business.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Null;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
//todo: set default price scale
//todo: add validation
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
public class UserOrderDto {

    private final static String DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS";
    @Size(min = 1)
    List<UserOrderPositionDto> positions;
    @Positive(message = "{entity.field.not.positive}")
    private Long id;
    private AppUserDto user;

    @Null(message = "{entity.field.should.be.null}")
    @DecimalMin(value = "0.01", message = "{entity.field.min}")
    @DecimalMax(value = "999999999999999999.99", message = "{entity.field.max}")
    private BigDecimal price;

    @Null(message = "{entity.field.should.be.null}")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_PATTERN)
    private LocalDateTime createDate;
}
