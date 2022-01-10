package com.epam.esm.gcs.business.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class GiftCertificateDto {

    private final Long id;
    private final String name;
    private final String description;
    private final BigDecimal price;
    private final Integer duration;
    private final LocalDateTime createDate;
    private final LocalDateTime lastUpdateDate;
    private final List<TagDto> tags;

    protected GiftCertificateDto() {
        id = null;
        name = null;
        description = null;
        price = null;
        duration = null;
        createDate = null;
        lastUpdateDate = null;
        tags = null;
    }

    public GiftCertificateDto(Long id, String name, String description, BigDecimal price, Integer duration,
                              LocalDateTime createDate, LocalDateTime lastUpdateDate, List<TagDto> tags) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.createDate = createDate;
        this.lastUpdateDate = lastUpdateDate;
        this.tags = tags;
    }

    public static GiftCertificateDtoBuilder builder() {
        return new GiftCertificateDtoBuilder();
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

    public List<TagDto> getTags() {
        return tags == null ? null : new ArrayList<>(tags);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GiftCertificateDto that = (GiftCertificateDto) o;

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

    public static class GiftCertificateDtoBuilder {
        private Long id;
        private String name;
        private String description;
        private BigDecimal price;
        private Integer duration;
        private LocalDateTime createDate;
        private LocalDateTime lastUpdateDate;
        private List<TagDto> tags;

        public GiftCertificateDtoBuilder setId(Long id) {
            this.id = id;
            return this;
        }

        public GiftCertificateDtoBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public GiftCertificateDtoBuilder setDescription(String description) {
            this.description = description;
            return this;
        }

        public GiftCertificateDtoBuilder setPrice(BigDecimal price) {
            this.price = price;
            return this;
        }

        public GiftCertificateDtoBuilder setDuration(Integer duration) {
            this.duration = duration;
            return this;
        }

        public GiftCertificateDtoBuilder setCreateDate(LocalDateTime createDate) {
            this.createDate = createDate;
            return this;
        }

        public GiftCertificateDtoBuilder setLastUpdateDate(LocalDateTime lastUpdateDate) {
            this.lastUpdateDate = lastUpdateDate;
            return this;
        }

        public void setTags(List<TagDto> tags) {
            this.tags = tags;
        }

        public GiftCertificateDtoBuilder setGiftCertificate(GiftCertificateDto giftCertificate) {
            this.id = giftCertificate.getId();
            this.name = giftCertificate.getName();
            this.description = giftCertificate.getDescription();
            this.price = giftCertificate.getPrice();
            this.duration = giftCertificate.getDuration();
            this.createDate = giftCertificate.getCreateDate();
            this.lastUpdateDate = giftCertificate.getLastUpdateDate();
            return this;
        }

        public GiftCertificateDto build() {
            return new GiftCertificateDto(id, name, description, price, duration, createDate, lastUpdateDate, tags);
        }
    }
}
