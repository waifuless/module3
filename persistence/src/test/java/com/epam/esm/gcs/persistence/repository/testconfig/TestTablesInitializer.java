package com.epam.esm.gcs.persistence.repository.testconfig;

import org.springframework.jdbc.core.JdbcTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

public class TestTablesInitializer {

    private final JdbcTemplate jdbcTemplate;

    public TestTablesInitializer(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void initialize() throws IOException {
        try (BufferedReader sqlInput = new BufferedReader
                (new InputStreamReader(Objects.requireNonNull(TestTablesInitializer
                        .class.getClassLoader().getResourceAsStream("sql/database.sql"))))) {
            StringBuilder sql = new StringBuilder();
            sqlInput.lines().forEachOrdered(line -> sql.append(line).append("\n"));
            String sqlToRun = new String(sql);
            System.out.println(sqlToRun);
            jdbcTemplate.execute(sqlToRun);
        }
    }
}
