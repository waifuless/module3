package com.epam.esm.gcs.persistence.util;

import com.epam.esm.gcs.persistence.model.PageParamsModel;

public interface Paginator {

    int findStartPosition(PageParamsModel page);
}
