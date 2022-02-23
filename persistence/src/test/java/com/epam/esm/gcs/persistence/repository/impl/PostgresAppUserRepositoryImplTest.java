package com.epam.esm.gcs.persistence.repository.impl;

import com.epam.esm.gcs.persistence.model.AppUserModel;
import com.epam.esm.gcs.persistence.repository.AppUserRepository;
import com.epam.esm.gcs.persistence.testapplication.TestApplication;
import lombok.RequiredArgsConstructor;
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

@ExtendWith(SpringExtension.class)
@ActiveProfiles(profiles = "test")
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@RequiredArgsConstructor
@SpringBootTest(classes = TestApplication.class)
@Transactional
class PostgresAppUserRepositoryImplTest {

    private final static String GIFT_CERTIFICATE_TABLE_NAME = "gift_certificate";
    private final static String GIFT_CERTIFICATE_TAG_TABLE_NAME = "gift_certificate_tag";
    private final static String TAG_TABLE = "tag";

    private final AppUserRepository appUserRepository;
    private final JdbcTemplate jdbcTemplate;
    private final EntityManager entityManager;

    //todo:remake tests
    @Test
    void findUsersWithHighestPriceAmountOfAllOrders() {
        List<AppUserModel> users = appUserRepository.findUsersWithHighestPriceAmountOfAllOrders();
        System.out.println(users);
    }
}