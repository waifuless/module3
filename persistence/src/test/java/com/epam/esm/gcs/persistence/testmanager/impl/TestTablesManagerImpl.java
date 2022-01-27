package com.epam.esm.gcs.persistence.testmanager.impl;

import com.epam.esm.gcs.persistence.testmanager.TestTablesManager;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Component;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.sql.SQLException;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
@RequiredArgsConstructor
public class TestTablesManagerImpl implements TestTablesManager {

    private final static String CREATE_TABLES_SQL = "/sql/database.sql";
    private final static String[] TEST_TABLES = new String[]{"tag", "gift_certificate", "gift_certificate_tag"};

    private final JdbcTemplate jdbcTemplate;

    private final AtomicBoolean tablesAreCreated = new AtomicBoolean(false);

    @Override
    public void createOrCleanTables() throws SQLException {
        if (tablesAreCreated.compareAndSet(false, true)) {
            ScriptUtils.executeSqlScript(Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection(),
                    new ClassPathResource(CREATE_TABLES_SQL));
        } else {
            JdbcTestUtils.deleteFromTables(jdbcTemplate, TEST_TABLES);
        }
    }
}
