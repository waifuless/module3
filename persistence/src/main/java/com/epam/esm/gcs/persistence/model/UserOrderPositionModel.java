package com.epam.esm.gcs.persistence.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "user_order_position")
@IdClass(UserOrderPositionModel.UserOrderGiftCertificatePk.class)
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserOrderPositionModel {

    @Id
    @ManyToOne
    @JoinColumn(name = "user_order_id")
    private UserOrderModel userOrder;

    @Id
    @ManyToOne
    @JoinColumn(name = "gift_certificate_id")
    private GiftCertificateModel giftCertificate;

    private Integer count;

    @Data
    public static class UserOrderGiftCertificatePk implements Serializable {

        private Long userOrder;
        private Long giftCertificate;
    }
}
