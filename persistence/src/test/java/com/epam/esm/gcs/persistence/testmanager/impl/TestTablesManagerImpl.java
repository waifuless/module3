package com.epam.esm.gcs.persistence.testmanager.impl;

import com.epam.esm.gcs.persistence.testmanager.TestTablesManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Component;
import org.springframework.test.jdbc.JdbcTestUtils;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class TestTablesManagerImpl implements TestTablesManager {

    private final static String CREATE_TABLES_SQL = "/sql/database.sql";
    private final static String[] TEST_TABLES = new String[]{"tag", "gift_certificate", "gift_certificate_tag"};

    private final DataSource dataSource;
    private final JdbcTemplate jdbcTemplate;

    private final AtomicBoolean tablesAreCreated = new AtomicBoolean(false);

    @Autowired
    public TestTablesManagerImpl(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void createOrCleanTables() throws SQLException {
        if (tablesAreCreated.compareAndSet(false, true)) {
            ScriptUtils.executeSqlScript(dataSource.getConnection(),
                    new ClassPathResource(CREATE_TABLES_SQL));
        } else {
            JdbcTestUtils.deleteFromTables(jdbcTemplate, TEST_TABLES);
        }
    }
}
