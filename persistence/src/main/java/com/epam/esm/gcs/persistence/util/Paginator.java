package com.epam.esm.gcs.persistence.util;

import com.epam.esm.gcs.persistence.model.PageModel;

public interface Paginator {

    int findStartPosition(PageModel page);
}
