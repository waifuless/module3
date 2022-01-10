package com.epam.esm.gcs.persistence.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class GiftCertificateModel {

    private final Long id;
    private final String name;
    private final String description;
    private final BigDecimal price;
    private final Integer duration;
    private final LocalDateTime createDate;
    private final LocalDateTime lastUpdateDate;

    public GiftCertificateModel(String name, String description, BigDecimal price, Integer duration,
                                LocalDateTime createDate, LocalDateTime lastUpdateDate) {
        this.id = null;
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.createDate = createDate;
        this.lastUpdateDate = lastUpdateDate;
    }

    public GiftCertificateModel(Long id, String name, String description, BigDecimal price, Integer duration,
                                LocalDateTime createDate, LocalDateTime lastUpdateDate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.createDate = createDate;
        this.lastUpdateDate = lastUpdateDate;
    }

    public static GiftCertificateModelBuilder builder() {
        return new GiftCertificateModelBuilder();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Integer getDuration() {
        return duration;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public LocalDateTime getLastUpdateDate() {
        return lastUpdateDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GiftCertificateModel that = (GiftCertificateModel) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (price != null ? price.compareTo(that.price) != 0 : that.price != null) return false;
        if (duration != null ? !duration.equals(that.duration) : that.duration != null) return false;
        if (createDate != null ? !createDate.equals(that.createDate) : that.createDate != null) return false;
        return lastUpdateDate != null ? lastUpdateDate.equals(that.lastUpdateDate) : that.lastUpdateDate == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (price != null ? price.toPlainString().hashCode() : 0);
        result = 31 * result + (duration != null ? duration.hashCode() : 0);
        result = 31 * result + (createDate != null ? createDate.hashCode() : 0);
        result = 31 * result + (lastUpdateDate != null ? lastUpdateDate.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "GiftCertificate{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", duration=" + duration +
                ", createDate=" + createDate +
                ", lastUpdateDate=" + lastUpdateDate +
                '}';
    }

    public static class GiftCertificateModelBuilder {
        private Long id;
        private String name;
        private String description;
        private BigDecimal price;
        private Integer duration;
        private LocalDateTime createDate;
        private LocalDateTime lastUpdateDate;

        public GiftCertificateModelBuilder setId(Long id) {
            this.id = id;
            return this;
        }

        public GiftCertificateModelBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public GiftCertificateModelBuilder setDescription(String description) {
            this.description = description;
            return this;
        }

        public GiftCertificateModelBuilder setPrice(BigDecimal price) {
            this.price = price;
            return this;
        }

        public GiftCertificateModelBuilder setDuration(Integer duration) {
            this.duration = duration;
            return this;
        }

        public GiftCertificateModelBuilder setCreateDate(LocalDateTime createDate) {
            this.createDate = createDate;
            return this;
        }

        public GiftCertificateModelBuilder setLastUpdateDate(LocalDateTime lastUpdateDate) {
            this.lastUpdateDate = lastUpdateDate;
            return this;
        }

        public GiftCertificateModelBuilder setGiftCertificate(GiftCertificateModel giftCertificateModel) {
            this.id = giftCertificateModel.getId();
            this.name = giftCertificateModel.getName();
            this.description = giftCertificateModel.getDescription();
            this.price = giftCertificateModel.getPrice();
            this.duration = giftCertificateModel.getDuration();
            this.createDate = giftCertificateModel.getCreateDate();
            this.lastUpdateDate = giftCertificateModel.getLastUpdateDate();
            return this;
        }

        public GiftCertificateModel build() {
            return new GiftCertificateModel(id, name, description, price, duration, createDate, lastUpdateDate);
        }
    }
}
