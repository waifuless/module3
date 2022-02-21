package com.epam.esm.gcs.persistence.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "actuality_state")
@Getter
@RequiredArgsConstructor
public enum ActualityStateModel {
    ACTUAL(1L, "ACTUAL"), ARCHIVED(2L, "ARCHIVED");

    @Id
    private final Long id;

    private final String name;
}
