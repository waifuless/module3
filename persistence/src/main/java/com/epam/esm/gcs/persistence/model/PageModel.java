package com.epam.esm.gcs.persistence.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageModel<T> {

    private List<T> content;
    private PageParamsModel pageParams;
    private Long totalCount;
}
