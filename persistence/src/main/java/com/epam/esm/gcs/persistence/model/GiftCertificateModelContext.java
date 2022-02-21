package com.epam.esm.gcs.persistence.model;

import com.epam.esm.gcs.persistence.tableproperty.SortDirection;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@EqualsAndHashCode
@Builder
public class GiftCertificateModelContext {

    private List<String> tagNames;
    private String searchValue;
    private Map<String, SortDirection> sortDirectionByFieldNameMap;
}
