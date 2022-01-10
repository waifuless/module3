package com.epam.esm.gcs.persistence.repository.impl;

import com.epam.esm.gcs.persistence.exception.RepositoryException;
import com.epam.esm.gcs.persistence.model.GiftCertificateModel;
import com.epam.esm.gcs.persistence.repository.GiftCertificateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Optional;

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
    public GiftCertificateModel save(GiftCertificateModel model) throws RepositoryException {
        return null;
    }

    @Override
    public Optional<GiftCertificateModel> findById(long id) throws RepositoryException {
        return Optional.empty();
    }

    @Override
    public Iterable<GiftCertificateModel> findAll() throws RepositoryException {
        return null;
    }

    @Override
    public GiftCertificateModel update(GiftCertificateModel model) throws RepositoryException {
        return null;
    }

    @Override
    public void delete(long id) throws RepositoryException {

    }
}
