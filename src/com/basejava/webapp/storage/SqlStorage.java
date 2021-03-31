package com.basejava.webapp.storage;

import com.basejava.webapp.exception.ResumeNotExistsStorageException;
import com.basejava.webapp.model.ContactType;
import com.basejava.webapp.model.Resume;
import com.basejava.webapp.sql.SqlHelper;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

public class SqlStorage implements Storage {

    private final SqlHelper sqlHelper;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        sqlHelper = new SqlHelper(() -> DriverManager.getConnection(dbUrl, dbUser, dbPassword));
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
                    (preparedStatement) ->
                    {
                        preparedStatement.setString(1, resume.getUuid());
                        preparedStatement.setString(2, resume.getFullName());
                        preparedStatement.execute();
                        return null;
                    });
            sqlHelper.executeInConnection(connection, "INSERT INTO contact (type, value, resume_uuid) VALUES (?,?,?)",
                    (preparedStatement) -> {
                        for (Map.Entry<ContactType, String> entry : resume.getAllContacts().entrySet()) {
                            preparedStatement.setString(1, String.valueOf(entry.getKey()));
                            preparedStatement.setString(2, entry.getValue());
                            preparedStatement.setString(3, resume.getUuid());
                            preparedStatement.addBatch();
                        }
                        preparedStatement.executeBatch();
                        return null;
                    });
            return null;
        });
    }

    @Override
    public Resume get(String uuid) {
        return sqlHelper.executeQuery("SELECT * FROM resume r " +
                        " LEFT JOIN contact c " +
                        " ON c.resume_uuid = r.uuid " +
                        " WHERE r.uuid=? ",
                preparedStatement -> {
                    preparedStatement.setString(1, uuid);
                    ResultSet resultSet = preparedStatement.executeQuery();
                    if (!resultSet.next()) {
                        throw new ResumeNotExistsStorageException(uuid);
                    }
                    Resume resume = new Resume(uuid, resultSet.getString("full_name"));
                    do {
                        String value = resultSet.getString("value");
                        if (value != null) {
                            ContactType contactType = ContactType.valueOf(resultSet.getString("type"));
                            resume.addContact(contactType, value);
                        }
                    } while (resultSet.next());
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
                    (preparedStatement) -> {
                        preparedStatement.setString(1, resume.getFullName());
                        preparedStatement.setString(2, uuid);
                        if (preparedStatement.executeUpdate() == 0) {
                            throw new ResumeNotExistsStorageException(uuid);
                        }
                        return null;
                    });
            sqlHelper.executeInConnection(connection, "DELETE FROM contact WHERE resume_uuid=?",
                    (preparedStatement)->{
                        preparedStatement.setString(1, uuid);
                        preparedStatement.execute();
                        return null;
                    });
            sqlHelper.executeInConnection(connection, "INSERT INTO contact (type, value, resume_uuid) VALUES (?,?,?)",
                    (preparedStatement) -> {
                        for (Map.Entry<ContactType, String> entry : resume.getAllContacts().entrySet()) {
                            preparedStatement.setString(1, String.valueOf(entry.getKey()));
                            preparedStatement.setString(2, entry.getValue());
                            preparedStatement.setString(3, uuid);
                            preparedStatement.addBatch();
                        }
                        preparedStatement.executeBatch();
                        return null;
                    });
            return null;
        });
    }

    @Override
    public List<Resume> getAllSorted() {
        return sqlHelper.transactionalExecute(connection -> {
            List<Resume> resumes = new ArrayList<>();
            try (PreparedStatement psResume = connection.prepareStatement("SELECT * FROM resume ORDER BY full_name, uuid");
                 PreparedStatement psContact = connection.prepareStatement("SELECT resume_uuid AS uuid, type, value FROM contact")) {
                ResultSet rsResume = psResume.executeQuery();
                ResultSet rsContact = psContact.executeQuery();
                Map<String, Map<ContactType, String>> contacts = new HashMap<>();
                while (rsContact.next()) {
                    String uuid = rsContact.getString("uuid");
                    ContactType type = ContactType.valueOf(rsContact.getString("type"));
                    String value = rsContact.getString("value");
                    if (contacts.containsKey(uuid)) {
                        contacts.get(uuid).put(type, value);
                    } else {
                        Map<ContactType, String> contact = new EnumMap<>(ContactType.class);
                        contact.put(type, value);
                        contacts.put(uuid, contact);
                    }
                }
                while (rsResume.next()) {
                    String uuid = rsResume.getString("uuid");
                    Resume r = new Resume(uuid, rsResume.getString("full_name"));
                    Map<ContactType, String> resumeContacts = contacts.get(uuid);
                    resumeContacts.forEach(r::addContact);
                    resumes.add(r);
                }
            }
            return resumes;
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
}
