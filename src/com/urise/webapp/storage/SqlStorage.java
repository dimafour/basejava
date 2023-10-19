package com.urise.webapp.storage;

import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.*;
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
        sqlHelper.execute("SELECT * FROM resume " +
                "ORDER BY full_name, uuid", ps -> {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String uuid = rs.getString("uuid").trim();
                map.put(uuid, new Resume(uuid, rs.getString("full_name")));
            }
            LOG.info("getAllSorted");
            return null;
        });
        sqlHelper.execute("SELECT * FROM contact", ps -> {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String uuid = rs.getString("resume_uuid").trim();
                addContact(rs, map.get(uuid));
            }
            return null;
        });
        sqlHelper.execute("SELECT * FROM section", ps -> {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String uuid = rs.getString("resume_uuid").trim();
                addSection(rs, map.get(uuid));
            }
            return null;
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
            processResume(c, "UPDATE resume SET full_name=? WHERE uuid = ?", r);
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
                ps.setString(3, entry.getValue().toString());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void addSection(ResultSet rs, Resume r) throws SQLException {
        String sectionType = rs.getString("section_type");
        String content = rs.getString("content");
        if (sectionType != null && content != null) {
            SectionType st = SectionType.valueOf(sectionType);
            switch (st) {
                case PERSONAL, OBJECTIVE -> r.addSectionContent(st, new TextSection(content));
                case ACHIEVEMENT, QUALIFICATIONS -> r.addSectionContent(st,
                        new ListSection(List.of(content.split("\n"))));
            }
        }
    }

    private void deleteSections(Connection c, Resume r) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement("DELETE FROM section WHERE section.resume_uuid = ?")) {
            ps.setString(1, r.getUuid());
            ps.execute();
        }
    }

}
