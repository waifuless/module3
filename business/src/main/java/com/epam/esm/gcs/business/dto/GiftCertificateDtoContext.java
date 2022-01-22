package com.epam.esm.gcs.business.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GiftCertificateDtoContext {

    private String tagName;
    private String searchValue;
    private List<String> sortBy;
}
