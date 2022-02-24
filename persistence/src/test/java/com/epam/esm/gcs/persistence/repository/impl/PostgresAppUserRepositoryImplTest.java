package com.epam.esm.gcs.persistence.repository.impl;

import com.epam.esm.gcs.persistence.model.AppUserModel;
import com.epam.esm.gcs.persistence.repository.AppUserRepository;
import com.epam.esm.gcs.persistence.testapplication.TestApplication;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;

@ExtendWith(SpringExtension.class)
@ActiveProfiles(profiles = "test")
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@RequiredArgsConstructor
@SpringBootTest(classes = TestApplication.class)
@Transactional
class PostgresAppUserRepositoryImplTest {

    private static final String GIFT_CERTIFICATE_TABLE_NAME = "gift_certificate";
    private static final String GIFT_CERTIFICATE_TAG_TABLE_NAME = "gift_certificate_tag";
    private static final String TAG_TABLE = "tag";

    private final AppUserRepository appUserRepository;
    private final JdbcTemplate jdbcTemplate;
    private final EntityManager entityManager;

    private AppUserModel vovaUser;
    private AppUserModel tanyaUser;

    @BeforeEach
    private void prepareModels() {
        vovaUser = entityManager.find(AppUserModel.class, 1L);
        tanyaUser = entityManager.find(AppUserModel.class, 2L);
    }

    @Test
    void findUsersWithHighestPriceAmountOfAllOrders_shouldReturnValidUser() {
        assertIterableEquals(List.of(vovaUser), appUserRepository.findUsersWithHighestPriceAmountOfAllOrders());
    }
}