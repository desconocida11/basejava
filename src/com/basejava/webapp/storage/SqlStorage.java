package com.basejava.webapp.storage;

import com.basejava.webapp.exception.ResumeNotExistsStorageException;
import com.basejava.webapp.model.*;
import com.basejava.webapp.sql.SqlHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SqlStorage implements Storage {

    private final SqlHelper sqlHelper;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        sqlHelper = new SqlHelper(() -> DriverManager.getConnection(dbUrl, dbUser, dbPassword));
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("No driver found for PostgreSQL database", e);
        }
    }

    @SuppressWarnings("SqlWithoutWhere")
    @Override
    public void clear() {
        sqlHelper.executeQuery("DELETE FROM resume",
                PreparedStatement::execute);
    }

    @Override
    public void save(Resume resume) {
        sqlHelper.transactionalExecute(connection -> {
            sqlHelper.executeInConnection(connection, "INSERT INTO resume (uuid, full_name) VALUES (?,?)",
                    preparedStatement ->
                    {
                        preparedStatement.setString(1, resume.getUuid());
                        preparedStatement.setString(2, resume.getFullName());
                        preparedStatement.execute();
                        return null;
                    });
            insertContacts(connection, resume);
            insertSections(connection, resume);
            return null;
        });
    }

    @Override
    public Resume get(String uuid) {
        return sqlHelper.transactionalExecute(connection -> {
            Resume resume;
            try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM resume r WHERE r.uuid=? ")) {
                preparedStatement.setString(1, uuid);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (!resultSet.next()) {
                    throw new ResumeNotExistsStorageException(uuid);
                }
                resume = new Resume(uuid, resultSet.getString("full_name"));
            }
            try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT resume_uuid AS uuid, type, value FROM contact WHERE resume_uuid=?")) {
                preparedStatement.setString(1, uuid);
                ResultSet rsContact = preparedStatement.executeQuery();
                while (rsContact.next()) {
                    addContactToResume(resume, rsContact);
                }
            }
            try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT resume_uuid AS uuid, type, value FROM section WHERE resume_uuid=?")) {
                preparedStatement.setString(1, uuid);
                ResultSet rsContact = preparedStatement.executeQuery();
                while (rsContact.next()) {
                    addSectionToResume(resume, rsContact);
                }
            }
            return resume;
        });
    }

    @Override
    public void delete(String uuid) {
        sqlHelper.<Void>executeQuery("DELETE FROM resume r WHERE r.uuid=?",
                preparedStatement -> {
                    preparedStatement.setString(1, uuid);
                    if (preparedStatement.executeUpdate() == 0) {
                        throw new ResumeNotExistsStorageException(uuid);
                    }
                    return null;
                });
    }

    @Override
    public void update(Resume resume) {
        final String uuid = resume.getUuid();
        sqlHelper.transactionalExecute(connection -> {
            sqlHelper.executeInConnection(connection, "UPDATE resume r SET full_name=? WHERE r.uuid=?",
                    preparedStatement -> {
                        preparedStatement.setString(1, resume.getFullName());
                        preparedStatement.setString(2, uuid);
                        if (preparedStatement.executeUpdate() == 0) {
                            throw new ResumeNotExistsStorageException(uuid);
                        }
                        return null;
                    });
            sqlHelper.executeInConnection(connection, "DELETE FROM contact WHERE resume_uuid=?",
                    preparedStatement -> {
                        preparedStatement.setString(1, uuid);
                        preparedStatement.execute();
                        return null;
                    });
            sqlHelper.executeInConnection(connection, "DELETE FROM section WHERE resume_uuid=?",
                    preparedStatement -> {
                        preparedStatement.setString(1, uuid);
                        preparedStatement.execute();
                        return null;
                    });
            insertContacts(connection, resume);
            insertSections(connection, resume);
            return null;
        });
    }

    @Override
    public List<Resume> getAllSorted() {
        return sqlHelper.transactionalExecute(connection -> {
            Map<String, Resume> resumes = new LinkedHashMap<>();
            try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM resume ORDER BY full_name, uuid")) {
                ResultSet rsResume = preparedStatement.executeQuery();
                while (rsResume.next()) {
                    String uuid = rsResume.getString("uuid");
                    Resume r = new Resume(uuid, rsResume.getString("full_name"));
                    resumes.put(uuid, r);
                }
            }
            sqlHelper.addSectionContact(resumes, connection, "SELECT resume_uuid AS uuid, type, value FROM contact",
                    this::addContactToResume);
            sqlHelper.addSectionContact(resumes, connection, "SELECT resume_uuid AS uuid, type, value FROM section",
                    this::addSectionToResume);
            return new ArrayList<>(resumes.values());
        });
    }

    @Override
    public int size() {
        return sqlHelper.executeQuery("SELECT COUNT(*) AS size FROM resume",
                preparedStatement -> {
                    ResultSet resultSet = preparedStatement.executeQuery();
                    return resultSet.next() ? resultSet.getInt("size") : 0;
                });
    }

    private void insertContacts(Connection connection, Resume resume) throws SQLException {
        sqlHelper.executeInConnection(connection, "INSERT INTO contact (type, value, resume_uuid) VALUES (?,?,?)",
                preparedStatement -> {
                    for (Map.Entry<ContactType, String> entry : resume.getAllContacts().entrySet()) {
                        preparedStatement.setString(1, String.valueOf(entry.getKey()));
                        preparedStatement.setString(2, entry.getValue());
                        preparedStatement.setString(3, resume.getUuid());
                        preparedStatement.addBatch();
                    }
                    preparedStatement.executeBatch();
                    return null;
                });
    }

    private void insertSections(Connection connection, Resume resume) throws SQLException {
        sqlHelper.executeInConnection(connection, "INSERT INTO section (type, value, resume_uuid) VALUES (?,?,?)",
                preparedStatement -> {
                    for (Map.Entry<SectionType, AbstractSection> entry : resume.getAllSections().entrySet()) {
                        SectionType entryKey = entry.getKey();
                        String sectionValue = null;
                        AbstractSection abstractSection = entry.getValue();
                        switch (entryKey) {
                            case PERSONAL:
                            case OBJECTIVE:
                                sectionValue = ((SingleLineSection) abstractSection).getValue();
                                break;
                            case ACHIEVEMENT:
                            case QUALIFICATIONS:
                                StringBuilder sb = new StringBuilder();
                                for (String s : ((BulletedListSection) abstractSection).getValue()) {
                                    sb.append(s);
                                    sb.append("\n");
                                }
                                sectionValue = sb.toString();
                                break;
                            case EDUCATION:
                            case EXPERIENCE:
                                break;
                        }
                        if (sectionValue != null) {
                            preparedStatement.setString(1, String.valueOf(entryKey));
                            preparedStatement.setString(2, sectionValue);
                            preparedStatement.setString(3, resume.getUuid());
                            preparedStatement.addBatch();
                        }
                    }
                    preparedStatement.executeBatch();
                    return null;
                });
    }

    private void addContactToResume(Resume resume, ResultSet resultSet) throws SQLException {
        resume.addContact(ContactType.valueOf(resultSet.getString("type")), resultSet.getString("value"));
    }

    private void addSectionToResume(Resume resume, ResultSet resultSet) throws SQLException {
        SectionType type = SectionType.valueOf(resultSet.getString("type"));
        AbstractSection abstractSection = null;
        String sectionValue = resultSet.getString("value");
        switch (type) {
            case PERSONAL:
            case OBJECTIVE:
                abstractSection = new SingleLineSection(sectionValue);
                break;
            case ACHIEVEMENT:
            case QUALIFICATIONS:
                abstractSection = new BulletedListSection(sectionValue.split("\n"));
                break;
            case EDUCATION:
            case EXPERIENCE:
                break;
        }
        if (abstractSection != null) {
            resume.addSection(type, abstractSection);
        }
    }
}
