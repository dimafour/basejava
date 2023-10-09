package com.urise.webapp.storage;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;
import com.urise.webapp.sql.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SqlStorage implements Storage {
    public final ConnectionFactory connectionFactory;

    public SqlStorage(String dbURL, String dbUser, String dbPassword) {
        connectionFactory = () -> DriverManager.getConnection(dbURL, dbUser, dbPassword);
    }

    @Override
    public void clear() {
        try (Connection c = connectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement("DELETE FROM resume")) {
            ps.execute();
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

    @Override
    public void save(Resume r) {
        try (Connection c = connectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement("INSERT INTO resume (uuid, full_name) VALUES(?, ?) ")) {
            ps.setString(1, r.getUuid());
            ps.setString(2, r.getFullName());
            ps.execute();
        } catch (SQLException e) {
            throw new ExistStorageException(r.getUuid());
        }
    }

    @Override
    public Resume get(String uuid) {
        try (Connection c = connectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT * FROM resume r WHERE r.uuid = ? ")) {
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new NotExistStorageException(uuid);
            }
            return new Resume(uuid, rs.getString("full_name"));
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

    @Override
    public void delete(String uuid) {
        try (Connection c = connectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement("DELETE FROM resume r WHERE r.uuid = ? ")) {
            ps.setString(1, uuid);
            if (ps.executeUpdate() == 0) throw new NotExistStorageException(uuid);
        } catch (SQLException e) {
            throw new NotExistStorageException(uuid);
        }
    }

    @Override
    public List<Resume> getAllSorted() {
        try (Connection c = connectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT * FROM resume ORDER BY full_name")) {
            ResultSet rs = ps.executeQuery();
            List<Resume> list = new ArrayList<>();
            while (rs.next()) {
                list.add(new Resume(rs.getString("uuid").trim(), rs.getString("full_name")));
            }
            return list;
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

    @Override
    public int size() {
        try (Connection c = connectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT count(*) FROM resume")) {
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

    @Override
    public void update(Resume r) {
        try (Connection c = connectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement("UPDATE resume SET full_name=? WHERE uuid = ?")) {
            ps.setString(1, r.getFullName());
            ps.setString(2, r.getUuid());
            ps.execute();
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }
}
