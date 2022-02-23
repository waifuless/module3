package com.epam.esm.gcs.persistence.util.impl;

import com.epam.esm.gcs.persistence.model.PageModel;
import com.epam.esm.gcs.persistence.util.Paginator;
import org.springframework.stereotype.Component;

@Component
public class PaginatorImpl implements Paginator {

    @Override
    public int findStartPosition(PageModel page) {
        return (page.getPage() - 1) * page.getSize();
    }
}
