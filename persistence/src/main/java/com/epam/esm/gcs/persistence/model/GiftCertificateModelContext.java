package com.epam.esm.gcs.persistence.model;

import com.epam.esm.gcs.persistence.tableproperty.SortDirection;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode
@Builder
public class GiftCertificateModelContext {

    private Set<String> tagNames;
    private String searchValue;
    private Map<String, SortDirection> sortDirectionByFieldNameMap;
    private StateForSearchModel state;

    public enum StateForSearchModel {
        ACTUAL, ARCHIVED, ALL;
    }
}
