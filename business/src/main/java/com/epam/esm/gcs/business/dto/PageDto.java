package com.epam.esm.gcs.business.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageDto<T> {

    private List<T> content;
    private PageParamsDto pageParams;
    private Long totalCount;
}
