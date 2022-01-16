package com.epam.esm.gcs.persistence.testmanager;

import java.sql.SQLException;

public interface TestTablesManager {

    void createOrCleanTables() throws SQLException;
}
