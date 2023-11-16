package com.urise.webapp.sql;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.StorageException;

import java.sql.SQLException;

public class SqlExceptionConverter {
    public static StorageException convert(SQLException e) {
        if (e.getSQLState().equals("23505")) {
            return new ExistStorageException(e);
        } else return new StorageException(e);
    }
}
