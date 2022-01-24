package com.epam.esm.gcs.persistence.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class GiftCertificateModel {

    @With
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer duration;
    //todo: remake to ZonedDateTime or something like that
    private LocalDateTime createDate;
    private LocalDateTime lastUpdateDate;
    private List<TagModel> tags;

    public GiftCertificateModel(String name, String description, BigDecimal price, Integer duration,
                                LocalDateTime createDate, LocalDateTime lastUpdateDate, List<TagModel> tags) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.createDate = createDate;
        this.lastUpdateDate = lastUpdateDate;
        this.tags = tags;
    }
}
