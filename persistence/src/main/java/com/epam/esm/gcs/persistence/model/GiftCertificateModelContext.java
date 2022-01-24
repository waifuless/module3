package com.epam.esm.gcs.persistence.model;

import com.epam.esm.gcs.persistence.mapper.GiftCertificateColumn;
import com.epam.esm.gcs.persistence.mapper.SortDirection;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class GiftCertificateModelContext {

    private String tagName;
    private String searchValue;
    private Map<GiftCertificateColumn, SortDirection> sortBy;
}
