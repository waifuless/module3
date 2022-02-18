package com.epam.esm.gcs.business.dto;

import com.epam.esm.gcs.business.dto.group.OnGiftCertificateCreate;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

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
    private final static String DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS";

    @Null(message = "{entity.field.should.be.null}")
    private Long id;

    @NotBlank(groups = OnGiftCertificateCreate.class, message = "violation.entity.field.blank")
    @Length(max = 500, message = "{entity.field.length.max}")
    private String name;

    @NotBlank(groups = OnGiftCertificateCreate.class, message = "violation.entity.field.blank")
    @Length(max = 20000, message = "{entity.field.length.max}")
    private String description;

    @Setter(AccessLevel.NONE)
    @NotNull(groups = OnGiftCertificateCreate.class, message = "violation.entity.field.null")
    @DecimalMin(value = "0.01", message = "{entity.field.min}")
    @DecimalMax(value = "999999999999999999.99", message = "{entity.field.max}")
    private BigDecimal price;

    @NotNull(groups = OnGiftCertificateCreate.class, message = "violation.entity.field.null")
    @Min(value = 1, message = "{entity.field.min}")
    private Integer duration;

    @Null(message = "{entity.field.should.be.null}")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_PATTERN)
    private LocalDateTime createDate;

    @Null(message = "{entity.field.should.be.null}")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_PATTERN)
    private LocalDateTime lastUpdateDate;

    //todo: add validation to state, count, successorId
    private ActualityStateDto state;

    @NotNull(groups = OnGiftCertificateCreate.class, message = "violation.entity.field.null")
    @Min(value = 1, message = "{entity.field.min}")
    private Integer count;

    private Long successorId;

    @NotNull(groups = OnGiftCertificateCreate.class, message = "violation.entity.field.null")
    @Size(min = 1, message = "{entity.field.size.min}")
    private List<@Valid TagDto> tags;

    public GiftCertificateDto(Long id, String name, String description, BigDecimal price, Integer duration,
                              LocalDateTime createDate, LocalDateTime lastUpdateDate,
                              ActualityStateDto state, Integer count, Long successorId, List<TagDto> tags) {

        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price == null ? null : price.setScale(DEFAULT_SCALE, DEFAULT_ROUNDING_MODE);
        this.duration = duration;
        this.createDate = createDate;
        this.lastUpdateDate = lastUpdateDate;
        this.state = state;
        this.count = count;
        this.successorId = successorId;
        this.tags = tags;
    }

    public void setPrice(BigDecimal price) {
        this.price = price == null ? null : price.setScale(DEFAULT_SCALE, DEFAULT_ROUNDING_MODE);
    }
}
