package com.epam.esm.gcs.persistence.model;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class GiftCertificateModel {

    Long id;
    String name;
    String description;
    BigDecimal price;
    Integer duration;
    LocalDateTime createDate;
    LocalDateTime lastUpdateDate;
    List<TagModel> tags;

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
