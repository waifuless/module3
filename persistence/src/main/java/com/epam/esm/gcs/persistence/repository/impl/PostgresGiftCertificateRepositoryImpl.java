package com.epam.esm.gcs.persistence.repository.impl;

import com.epam.esm.gcs.persistence.model.GiftCertificateModel;
import com.epam.esm.gcs.persistence.repository.GiftCertificateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;
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
    public Long save(GiftCertificateModel model) {
        return null;
    }

    @Override
    public Optional<GiftCertificateModel> findById(long id) {
        return Optional.empty();
    }

    @Override
    public List<GiftCertificateModel> findAll() {
        return null;
    }

    @Override
    public void update(GiftCertificateModel model) {
    }

    @Override
    public void delete(long id) {

    }

    @Override
    public Boolean existsById(long id) {
        return null;
    }
}
