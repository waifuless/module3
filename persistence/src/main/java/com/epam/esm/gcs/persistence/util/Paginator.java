package com.epam.esm.gcs.persistence.util;

import com.epam.esm.gcs.persistence.model.PageParamsModel;

/**
 * Interface for paginator
 */
public interface Paginator {

    /**
     * Calculates first start position for page by pageParams
     *
     * @param page - parameters for calculating
     * @return - first start position for page
     */
    int findStartPosition(PageParamsModel page);
}
