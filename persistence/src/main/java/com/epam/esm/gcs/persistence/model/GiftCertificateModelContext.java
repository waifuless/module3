package com.epam.esm.gcs.persistence.model;

import com.epam.esm.gcs.persistence.tableproperty.SortDirection;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@EqualsAndHashCode
@Builder
public class GiftCertificateModelContext {

    private String tagName;
    private String searchValue;
    private Map<String, SortDirection> sortBy;
}
