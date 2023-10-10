package com.urise.webapp.sql;

import java.sql.Connection;
import java.sql.SQLException;

public interface SqlTransaction {
    void process(Connection c) throws SQLException;
}
