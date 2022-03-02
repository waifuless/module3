package com.epam.esm.gcs.persistence.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActionWithCountModel {

    private Mode mode;
    private Integer count;

    public enum Mode {
        ADD, REDUCE
    }
}
