package com.urise.webapp.storage;

import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.ContactType;
import com.urise.webapp.model.Resume;
import com.urise.webapp.sql.SqlHelper;

import java.sql.*;
import java.util.*;
import java.util.logging.Logger;

public class SqlStorage implements Storage {
    private static final Logger LOG = Logger.getLogger(SqlStorage.class.getName());
    private final SqlHelper sqlHelper;

    public SqlStorage(String dbURL, String dbUser, String dbPassword) {
        sqlHelper = new SqlHelper(() -> DriverManager.getConnection(dbURL, dbUser, dbPassword));
    }

    @Override
    public void clear() {
        sqlHelper.execute("DELETE FROM resume", PreparedStatement::execute);
        LOG.info("Storage cleared");
    }

    @Override
    public void save(Resume r) {
        sqlHelper.transactionalExecute(c -> {
            processResume(c, "INSERT INTO resume (uuid, full_name) VALUES(?, ?)", r);
            processContact(c, "INSERT INTO contact (value, type, resume_uuid) VALUES(?, ?, ?)", r);
            LOG.info("Save " + r);
        });
    }

    @Override
    public Resume get(String uuid) {
        return sqlHelper.execute("SELECT * FROM resume r " +
                        "LEFT JOIN contact c " +
                        "ON r.uuid = c.resume_uuid " +
                        "WHERE r.uuid = ?",
                ps -> {
                    ps.setString(1, uuid);
                    ResultSet rs = ps.executeQuery();
                    if (!rs.next()) {
                        throw new NotExistStorageException(uuid);
                    }
                    Resume r = new Resume(uuid, rs.getString("full_name"));
                    do {
                        r.addContact(ContactType.valueOf(rs.getString("type")), rs.getString("value"));
                    } while (rs.next());
                    LOG.info("Get " + uuid);
                    return r;
                });
    }

    @Override
    public void delete(String uuid) {
        sqlHelper.execute("DELETE FROM resume r WHERE r.uuid = ? ", ps -> {
            ps.setString(1, uuid);
            if (ps.executeUpdate() == 0) throw new NotExistStorageException(uuid);
            LOG.info("Delete " + uuid);
            return null;
        });
    }

    @Override
    public List<Resume> getAllSorted() {
        return sqlHelper.execute("SELECT * FROM resume r " +
                "LEFT JOIN contact c " +
                "ON r.uuid = c.resume_uuid " +
                "ORDER BY full_name, uuid", ps -> {
            ResultSet rs = ps.executeQuery();
            Map<String, Resume> map = new LinkedHashMap<>();
            while (rs.next()) {
                String uuid = rs.getString("uuid").trim();
                Resume r = new Resume(uuid, rs.getString("full_name"));
                if (!map.containsKey(uuid)) {
                    map.put(uuid, r);
                }
                map.get(uuid).addContact(ContactType.valueOf(rs.getString("type")), rs.getString("value"));
            }
            LOG.info("getAllSorted");
            return new ArrayList<>(map.values());
        });
    }

    @Override
    public int size() {
        return sqlHelper.execute("SELECT count(*) FROM resume", ps -> {
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1);
        });
    }

    @Override
    public void update(Resume r) {
        sqlHelper.transactionalExecute(c -> {
            processResume(c, "UPDATE resume SET full_name=? WHERE uuid = ?", r);
            processContact(c, "UPDATE contact SET value=? WHERE type=? AND resume_uuid=?", r);
            LOG.info("Update " + r);
        });
    }

    private void processResume(Connection c, String query, Resume r) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement(query)) {
            ps.setString(1, r.getUuid());
            ps.setString(2, r.getFullName());
            ps.execute();
        }
    }

    private void processContact(Connection c, String query, Resume r) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement(query)) {
            for (Map.Entry<ContactType, String> entry : r.getContacts().entrySet()) {
                ps.setString(1, entry.getValue());
                ps.setString(2, entry.getKey().name());
                ps.setString(3, r.getUuid());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }
}
