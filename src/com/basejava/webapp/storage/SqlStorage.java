package com.basejava.webapp.storage;

import com.basejava.webapp.exception.ResumeNotExistsStorageException;
import com.basejava.webapp.model.ContactType;
import com.basejava.webapp.model.Resume;
import com.basejava.webapp.sql.SqlHelper;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SqlStorage implements Storage {

    private final SqlHelper sqlHelper;

    private static final Comparator<Resume> RESUME_FULL_NAME_COMPARATOR = Comparator.comparing(Resume::getFullName).thenComparing(Resume::getUuid);

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
                        (prepareContacts(preparedStatement, resume)).executeBatch();
                        return null;
                    });
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
            try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT type, value FROM contact WHERE resume_uuid=?")) {
                preparedStatement.setString(1, uuid);
                ResultSet rsContact = preparedStatement.executeQuery();
                while (rsContact.next()) {
                    ContactType type = ContactType.valueOf(rsContact.getString("type"));
                    String value = rsContact.getString("value");
                    resume.addContact(type, value);
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

    private PreparedStatement prepareContacts(PreparedStatement preparedStatement, Resume resume) throws SQLException {
        for (Map.Entry<ContactType, String> entry : resume.getAllContacts().entrySet()) {
            preparedStatement.setString(1, String.valueOf(entry.getKey()));
            preparedStatement.setString(2, entry.getValue());
            preparedStatement.setString(3, resume.getUuid());
            preparedStatement.addBatch();
        }
        return preparedStatement;
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
                    (preparedStatement) -> {
                        preparedStatement.setString(1, uuid);
                        preparedStatement.execute();
                        return null;
                    });
            sqlHelper.executeInConnection(connection, "INSERT INTO contact (type, value, resume_uuid) VALUES (?,?,?)",
                    (preparedStatement) -> {
                        (prepareContacts(preparedStatement, resume)).executeBatch();
                        return null;
                    });
            return null;
        });
    }

    @Override
    public List<Resume> getAllSorted() {
        return sqlHelper.transactionalExecute(connection -> {
            Map<String, Resume> resumes = new HashMap<>();
            try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM resume ORDER BY full_name, uuid")) {
                ResultSet rsResume = preparedStatement.executeQuery();
                while (rsResume.next()) {
                    String uuid = rsResume.getString("uuid");
                    Resume r = new Resume(uuid, rsResume.getString("full_name"));
                    resumes.put(uuid, r);
                }
            }
            try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT resume_uuid AS uuid, type, value FROM contact")) {
                ResultSet rsContact = preparedStatement.executeQuery();
                while (rsContact.next()) {
                    String uuid = rsContact.getString("uuid");
                    ContactType type = ContactType.valueOf(rsContact.getString("type"));
                    String value = rsContact.getString("value");
                    if (resumes.containsKey(uuid)) {
                        resumes.get(uuid).addContact(type, value);
                    }
                }
            }
            return resumes.values().stream().sorted(RESUME_FULL_NAME_COMPARATOR).collect(Collectors.toList());
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
