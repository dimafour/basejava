package com.urise.webapp.sql;

import com.urise.webapp.exception.StorageException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SqlHelper {
    private final ConnectionFactory connectionFactory;

    public SqlHelper(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public <T> T execute(String query, SqlProcessor<T> processor) {
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            return processor.process(ps);
        } catch (SQLException e) {
            throw SqlExceptionConverter.convert(e);
        }
    }

    public void transactionalExecute(SqlTransaction processor) {
        try (Connection c = connectionFactory.getConnection()) {
            try {
                c.setAutoCommit(false);
                processor.process(c);
                c.commit();
            } catch (SQLException e) {
                c.rollback();
                throw SqlExceptionConverter.convert(e);
            }
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }
}