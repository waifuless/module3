package com.epam.esm.gcs.persistence.repository.impl;

import com.epam.esm.gcs.persistence.exception.RepositoryException;
import com.epam.esm.gcs.persistence.model.GiftCertificateModel;
import com.epam.esm.gcs.persistence.repository.GiftCertificateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;

@Component
public class PostgresGiftCertificateRepositoryImpl implements GiftCertificateRepository {

    private final DataSource dataSource;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    private PostgresGiftCertificateRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Long save(GiftCertificateModel model) throws RepositoryException {
        return null;
    }

    @Override
    public GiftCertificateModel findById(long id) throws RepositoryException {
        return null;
    }

    @Override
    public List<GiftCertificateModel> findAll() throws RepositoryException {
        return null;
    }

    @Override
    public void update(GiftCertificateModel model) throws RepositoryException {
    }

    @Override
    public void delete(long id) throws RepositoryException {

    }
}
