package com.epam.esm.gcs.persistence.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "user_order")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@Builder
public class UserOrderModel {

    private static final int DEFAULT_SCALE = 2;
    private static final RoundingMode DEFAULT_ROUNDING_MODE = RoundingMode.HALF_UP;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private AppUserModel user;

    @Setter(AccessLevel.NONE)
    private BigDecimal price;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    @OneToMany(mappedBy = "userOrder", cascade = CascadeType.ALL)
    List<UserOrderPositionModel> positions;

    public UserOrderModel(UserOrderModel userOrder) {
        this.id = userOrder.id;
        this.user = userOrder.user;
        this.price = userOrder.price == null ? null :
                userOrder.price.setScale(DEFAULT_SCALE, DEFAULT_ROUNDING_MODE);
        this.createDate = userOrder.createDate;

        this.positions = userOrder.positions.stream()
                .map(position ->
                        new UserOrderPositionModel(this, position.getGiftCertificate(), position.getCount()))
                .collect(Collectors.toList());
    }

    public UserOrderModel(Long id, AppUserModel user, BigDecimal price, LocalDateTime createDate,
                          List<UserOrderPositionModel> positions) {
        this.id = id;
        this.user = user;
        this.price = price == null ? null : price.setScale(DEFAULT_SCALE, DEFAULT_ROUNDING_MODE);
        this.createDate = createDate;
        this.positions = positions;
    }

    public void setPrice(BigDecimal price) {
        this.price = price == null ? null : price.setScale(DEFAULT_SCALE, DEFAULT_ROUNDING_MODE);
    }

    @PreUpdate
    @PreRemove
    private void refreshReverseReferencesBeforeAnyAction() {
        this.positions = this.positions.stream()
                .map(position ->
                        new UserOrderPositionModel(this, position.getGiftCertificate(), position.getCount()))
                .collect(Collectors.toList());
    }

    @PrePersist
    private void setCreateDate() {
        refreshReverseReferencesBeforeAnyAction();
        this.createDate = LocalDateTime.now();
    }
}
