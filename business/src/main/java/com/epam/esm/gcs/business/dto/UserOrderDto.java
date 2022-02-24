package com.epam.esm.gcs.business.dto;

import com.epam.esm.gcs.business.dto.group.OnUserOrderCreate;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
@Builder
public class UserOrderDto {

    private static final int DEFAULT_SCALE = 2;
    private static final RoundingMode DEFAULT_ROUNDING_MODE = RoundingMode.HALF_UP;
    private static final String DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS";

    @Null(groups = OnUserOrderCreate.class, message = "violation.entity.field.should.be.null")
    private Long id;

    @NotNull(groups = OnUserOrderCreate.class, message = "violation.entity.field.null")
    @Valid
    private AppUserDto user;

    @Setter(AccessLevel.NONE)
    @Null(message = "{entity.field.should.be.null}")
    @DecimalMin(value = "0.01", message = "{entity.field.min}")
    @DecimalMax(value = "999999999999999999.99", message = "{entity.field.max}")
    private BigDecimal price;

    @Null(message = "{entity.field.should.be.null}")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_PATTERN)
    private LocalDateTime createDate;

    @NotNull(groups = OnUserOrderCreate.class, message = "violation.entity.field.null")
    @Size(min = 1, message = "{entity.field.size.min}")
    List<@Valid UserOrderPositionDto> positions;

    public UserOrderDto(Long id, AppUserDto user, BigDecimal price, LocalDateTime createDate,
                        List<UserOrderPositionDto> positions) {
        this.id = id;
        this.user = user;
        this.price = price == null ? null : price.setScale(DEFAULT_SCALE, DEFAULT_ROUNDING_MODE);
        this.createDate = createDate;
        this.positions = positions;
    }

    public void setPrice(BigDecimal price) {
        this.price = price == null ? null : price.setScale(DEFAULT_SCALE, DEFAULT_ROUNDING_MODE);
    }
}
