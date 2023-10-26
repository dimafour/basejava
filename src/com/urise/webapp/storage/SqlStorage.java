package com.urise.webapp.storage;

import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.*;
import com.urise.webapp.sql.SqlHelper;
import com.urise.webapp.util.JsonParser;

import java.sql.*;
import java.util.*;
import java.util.logging.Logger;

public class SqlStorage implements Storage {
    private static final Logger LOG = Logger.getLogger(SqlStorage.class.getName());
    private final SqlHelper sqlHelper;

    public SqlStorage(String dbURL, String dbUser, String dbPassword) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
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
            try (PreparedStatement ps = c.prepareStatement("INSERT INTO resume (uuid, full_name) VALUES (?,?)")) {
                ps.setString(1, r.getUuid());
                ps.setString(2, r.getFullName());
                ps.execute();
            }
            insertContacts(c, r);
            insertSections(c, r);
            LOG.info("Save " + r);
        });
    }

    @Override
    public Resume get(String uuid) {
        return sqlHelper.execute("SELECT * FROM resume r " +
                        "LEFT JOIN contact c " +
                        "ON r.uuid = c.resume_uuid " +
                        "LEFT JOIN section s " +
                        "ON c.resume_uuid = s.resume_uuid " +
                        "WHERE r.uuid = ?",
                ps -> {
                    ps.setString(1, uuid);
                    ResultSet rs = ps.executeQuery();
                    if (!rs.next()) {
                        throw new NotExistStorageException(uuid);
                    }
                    Resume r = new Resume(uuid, rs.getString("full_name"));
                    do {
                        addContact(rs, r);
                        addSection(rs, r);
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
        Map<String, Resume> map = new LinkedHashMap<>();
        sqlHelper.transactionalExecute(c -> {
            try (PreparedStatement ps = c.prepareStatement("SELECT * FROM resume " +
                    "ORDER BY full_name, uuid")) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    String uuid = rs.getString("uuid").trim();
                    map.put(uuid, new Resume(uuid, rs.getString("full_name")));
                }
            }
            try (PreparedStatement ps = c.prepareStatement("SELECT * FROM contact")) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    String uuid = rs.getString("resume_uuid").trim();
                    addContact(rs, map.get(uuid));
                }
            }
            try (PreparedStatement ps = c.prepareStatement("SELECT * FROM section")) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    String uuid = rs.getString("resume_uuid").trim();
                    addSection(rs, map.get(uuid));
                }
            }
        });
        return new ArrayList<>(map.values());
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
            try (PreparedStatement ps = c.prepareStatement("UPDATE resume SET full_name = ? WHERE uuid = ?")) {
                ps.setString(1, r.getFullName());
                ps.setString(2, r.getUuid());
                if (ps.executeUpdate() != 1) {
                    throw new NotExistStorageException(r.getUuid());
                }
            }
            deleteContacts(c, r);
            insertContacts(c, r);
            deleteSections(c, r);
            insertSections(c, r);
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

    private void insertContacts(Connection c, Resume r) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement("INSERT INTO contact (resume_uuid, type, value) VALUES(?, ?, ?)")) {
            for (Map.Entry<ContactType, String> entry : r.getContacts().entrySet()) {
                ps.setString(1, r.getUuid());
                ps.setString(2, entry.getKey().name());
                ps.setString(3, entry.getValue());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void deleteContacts(Connection c, Resume r) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement("DELETE FROM contact WHERE contact.resume_uuid = ?")) {
            ps.setString(1, r.getUuid());
            ps.execute();
        }
    }

    private void addContact(ResultSet rs, Resume r) throws SQLException {
        String type = rs.getString("type");
        String value = rs.getString("value");
        if (type != null && value != null) {
            r.addContact(ContactType.valueOf(type), value);
        }
    }

    private void insertSections(Connection c, Resume r) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement("INSERT INTO section (resume_uuid, section_type, content) VALUES(?, ?, ?)")) {
            for (Map.Entry<SectionType, Section> entry : r.getSections().entrySet()) {
                ps.setString(1, r.getUuid());
                ps.setString(2, entry.getKey().name());
                ps.setString(3, JsonParser.write(entry.getValue(), Section.class));
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void addSection(ResultSet rs, Resume r) throws SQLException {
        String content = rs.getString("content");
        if (content != null) {
            SectionType type = SectionType.valueOf(rs.getString("section_type"));
            r.addSectionContent(type, JsonParser.read(content, Section.class));
        }
    }

    private void deleteSections(Connection c, Resume r) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement("DELETE FROM section WHERE section.resume_uuid = ?")) {
            ps.setString(1, r.getUuid());
            ps.execute();
        }
    }

}
