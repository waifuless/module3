package com.epam.esm.gcs.business.dto;

import com.epam.esm.gcs.business.dto.group.OnCreate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
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
public class GiftCertificateDto {

    private final static int DEFAULT_SCALE = 2;
    private final static RoundingMode DEFAULT_ROUNDING_MODE = RoundingMode.HALF_UP;

    @Null
    private Long id;

    @NotBlank(groups = OnCreate.class)
    @Size(max = 500)
    private String name;

    @NotBlank(groups = OnCreate.class)
    @Size(max = 20000)
    private String description;

    @Setter(AccessLevel.NONE)
    @NotNull(groups = OnCreate.class)
    @DecimalMin("0.01")
    @DecimalMax("999999999999999999.99")
    private BigDecimal price;

    @NotNull(groups = OnCreate.class)
    @Min(1)
    private Integer duration;

    @Null
    private LocalDateTime createDate;

    @Null
    private LocalDateTime lastUpdateDate;

    @NotNull(groups = OnCreate.class)
    @Size(min = 1)
    private List<@Valid TagDto> tags;

    public GiftCertificateDto(Long id, String name, String description, BigDecimal price, Integer duration,
                              LocalDateTime createDate, LocalDateTime lastUpdateDate,
                              List<TagDto> tags) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price.setScale(DEFAULT_SCALE, DEFAULT_ROUNDING_MODE);
        this.duration = duration;
        this.createDate = createDate;
        this.lastUpdateDate = lastUpdateDate;
        this.tags = tags;
    }

    public void setPrice(BigDecimal price) {
        this.price = price.setScale(DEFAULT_SCALE, DEFAULT_ROUNDING_MODE);
    }
}
