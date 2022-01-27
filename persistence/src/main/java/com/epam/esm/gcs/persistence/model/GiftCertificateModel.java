package com.epam.esm.gcs.persistence.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.With;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class GiftCertificateModel {

    private final static int DEFAULT_SCALE = 2;
    private final static RoundingMode DEFAULT_ROUNDING_MODE = RoundingMode.HALF_UP;

    @With
    private Long id;

    private String name;
    private String description;

    @Setter(AccessLevel.NONE)
    private BigDecimal price;

    private Integer duration;
    private LocalDateTime createDate;
    private LocalDateTime lastUpdateDate;
    private List<TagModel> tags;

    public GiftCertificateModel(String name, String description, BigDecimal price, Integer duration,
                                LocalDateTime createDate, LocalDateTime lastUpdateDate, List<TagModel> tags) {
        this.name = name;
        this.description = description;
        this.price = price == null ? null : price.setScale(DEFAULT_SCALE, DEFAULT_ROUNDING_MODE);
        this.duration = duration;
        this.createDate = createDate;
        this.lastUpdateDate = lastUpdateDate;
        this.tags = tags;
    }

    public void setPrice(BigDecimal price) {
        this.price = price == null ? null : price.setScale(DEFAULT_SCALE, DEFAULT_ROUNDING_MODE);
    }
}
