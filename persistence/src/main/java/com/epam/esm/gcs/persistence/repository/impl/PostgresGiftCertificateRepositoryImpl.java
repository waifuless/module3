package com.epam.esm.gcs.persistence.repository.impl;

import com.epam.esm.gcs.persistence.model.GiftCertificateModel;
import com.epam.esm.gcs.persistence.repository.GiftCertificateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PostgresGiftCertificateRepositoryImpl implements GiftCertificateRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public GiftCertificateModel create(GiftCertificateModel model) {
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
    public void updateById(long id, GiftCertificateModel model) {
    }

    @Override
    public void delete(long id) {

    }

    @Override
    public Boolean existsById(long id) {
        return null;
    }
}
