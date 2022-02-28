package com.epam.esm.gcs.persistence.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageParamsModel {

    private Integer page;
    private Integer size;
}
