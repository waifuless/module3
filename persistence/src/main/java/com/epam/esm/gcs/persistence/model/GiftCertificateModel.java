package com.epam.esm.gcs.persistence.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.With;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "gift_certificate")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@Builder
public class GiftCertificateModel {

    private static final int DEFAULT_SCALE = 2;
    private static final RoundingMode DEFAULT_ROUNDING_MODE = RoundingMode.HALF_UP;

    @Id
    //todo: think about batch (needs GenerationType.SEQUENCE)
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

    @Column(name = "state_id")
    private ActualityStateModel state;

    private Integer count;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "successor_id")
    private GiftCertificateModel successor;

    @ManyToMany
    @JoinTable(name = "gift_certificate_tag",
            joinColumns = @JoinColumn(name = "gift_certificate_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private List<TagModel> tags;

    public GiftCertificateModel(Long id, String name, String description, BigDecimal price, Integer duration,
                                LocalDateTime createDate, LocalDateTime lastUpdateDate, ActualityStateModel state,
                                Integer count, GiftCertificateModel successor, List<TagModel> tags) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price == null ? null : price.setScale(DEFAULT_SCALE, DEFAULT_ROUNDING_MODE);
        this.duration = duration;
        this.createDate = createDate;
        this.lastUpdateDate = lastUpdateDate;
        this.tags = tags;
        this.state = state;
        this.count = count;
        this.successor = successor;
    }

    public GiftCertificateModel(GiftCertificateModel giftCertificate) {
        this(giftCertificate.id, giftCertificate.name, giftCertificate.description, giftCertificate.price,
                giftCertificate.duration, giftCertificate.createDate, giftCertificate.lastUpdateDate,
                giftCertificate.state, giftCertificate.count, giftCertificate.successor,
                new ArrayList<>(giftCertificate.tags));
    }

    public void setPrice(BigDecimal price) {
        this.price = price == null ? null : price.setScale(DEFAULT_SCALE, DEFAULT_ROUNDING_MODE);
    }

    @PrePersist
    private void setDates() {
        LocalDateTime currentTime = LocalDateTime.now();
        if (createDate == null) {
            createDate = currentTime;
        }
        lastUpdateDate = currentTime;
    }
}
