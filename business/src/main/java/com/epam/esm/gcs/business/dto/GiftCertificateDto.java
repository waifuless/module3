package com.epam.esm.gcs.business.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class GiftCertificateDto {

    Long id;
    String name;
    String description;
    BigDecimal price;
    Integer duration;
    LocalDateTime createDate;
    LocalDateTime lastUpdateDate;
    List<TagDto> tags;
}
