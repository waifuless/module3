package com.epam.esm.gcs.persistence.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
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
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "user_order")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
//todo: set default price scale
public class UserOrderModel {

    @OneToMany(mappedBy = "userOrder", cascade = CascadeType.ALL)
    List<UserOrderPositionModel> positions;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private AppUserModel user;

    private BigDecimal price;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    public UserOrderModel(UserOrderModel userOrder) {
        this.id = userOrder.id;
        this.user = userOrder.user;
        this.price = userOrder.price;
        this.createDate = userOrder.createDate;

        this.positions = userOrder.positions.stream()
                .map(position ->
                        new UserOrderPositionModel(this, position.getGiftCertificate(), position.getCount()))
                .collect(Collectors.toList());
    }

    @PrePersist
    @PreUpdate
    @PreRemove
    private void refreshReverseReferencesBeforeAnyAction() {
        this.positions = this.positions.stream()
                .map(position ->
                        new UserOrderPositionModel(this, position.getGiftCertificate(), position.getCount()))
                .collect(Collectors.toList());
    }
}
