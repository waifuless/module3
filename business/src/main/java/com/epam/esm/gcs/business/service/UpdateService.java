package com.epam.esm.gcs.business.service;

import lombok.NonNull;

public interface UpdateService<T> {

    void update(@NonNull T dto);
}
