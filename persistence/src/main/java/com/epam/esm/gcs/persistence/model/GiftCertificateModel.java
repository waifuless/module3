package com.epam.esm.gcs.persistence.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.With;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "gift_certificate")
@Data
@NoArgsConstructor
@Builder
public class GiftCertificateModel {

    private final static int DEFAULT_SCALE = 2;
    private final static RoundingMode DEFAULT_ROUNDING_MODE = RoundingMode.HALF_UP;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @With
    private Long id;

    private String name;
    private String description;

    @Setter(AccessLevel.NONE)
    private BigDecimal price;

    private Integer duration;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    @Column(name = "last_update_date")
    private LocalDateTime lastUpdateDate;

    @ManyToMany
    @JoinTable(name = "gift_certificate_tag",
            joinColumns = @JoinColumn(name = "gift_certificate_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "id"))
    //todo: Make Set instead of list
    private List<TagModel> tags;

    public GiftCertificateModel(Long id, String name, String description, BigDecimal price, Integer duration,
                                LocalDateTime createDate, LocalDateTime lastUpdateDate, List<TagModel> tags) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price == null ? null : price.setScale(DEFAULT_SCALE, DEFAULT_ROUNDING_MODE);
        this.duration = duration;
        this.createDate = createDate;
        this.lastUpdateDate = lastUpdateDate;
        this.tags = tags;
    }

    public GiftCertificateModel(String name, String description, BigDecimal price, Integer duration,
                                LocalDateTime createDate, LocalDateTime lastUpdateDate, List<TagModel> tags) {
        this(null, name, description, price, duration, createDate, lastUpdateDate, tags);
    }

    public GiftCertificateModel(GiftCertificateModel giftCertificate) {
        this(giftCertificate.id, giftCertificate.name, giftCertificate.description, giftCertificate.price,
                giftCertificate.duration, giftCertificate.createDate, giftCertificate.lastUpdateDate,
                giftCertificate.tags);
    }

    public void setPrice(BigDecimal price) {
        this.price = price == null ? null : price.setScale(DEFAULT_SCALE, DEFAULT_ROUNDING_MODE);
    }
}
