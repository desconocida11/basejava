package com.basejava.webapp.storage;

import com.basejava.webapp.exception.ResumeNotExistsStorageException;
import com.basejava.webapp.model.Resume;
import com.basejava.webapp.sql.SqlHelper;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SqlStorage implements Storage {

    private final SqlHelper sqlHelper;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        sqlHelper = new SqlHelper(() -> DriverManager.getConnection(dbUrl, dbUser, dbPassword));
    }

    @Override
    public void clear() {
        sqlHelper.executeQuery("DELETE FROM resume",
                PreparedStatement::execute);
    }

    @Override
    public void save(Resume resume) {
        sqlHelper.executeQuery("INSERT INTO resume (uuid, full_name) VALUES (?,?)",
                preparedStatement -> {
                    preparedStatement.setString(1, resume.getUuid());
                    preparedStatement.setString(2, resume.getFullName());
                    preparedStatement.execute();
                    return null;
                });
    }

    @Override
    public Resume get(String uuid) {
        return sqlHelper.executeQuery("SELECT * FROM resume r WHERE r.uuid=?",
                preparedStatement -> {
                    preparedStatement.setString(1, uuid);
                    ResultSet resultSet = preparedStatement.executeQuery();
                    if (!resultSet.next()) {
                        throw new ResumeNotExistsStorageException(uuid);
                    }
                    return new Resume(uuid, resultSet.getString("full_name"));
                });
    }

    @Override
    public void delete(String uuid) {
        sqlHelper.executeQuery("DELETE FROM resume r WHERE r.uuid=?",
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
        sqlHelper.executeQuery("UPDATE resume r SET full_name=? WHERE r.uuid=?",
                preparedStatement -> {
                    preparedStatement.setString(1, resume.getFullName());
                    String uuid = resume.getUuid();
                    preparedStatement.setString(2, uuid);
                    if (preparedStatement.executeUpdate() == 0) {
                        throw new ResumeNotExistsStorageException(uuid);
                    }
                    return null;
                });
    }

    @Override
    public List<Resume> getAllSorted() {
        return sqlHelper.executeQuery("SELECT * FROM resume ORDER BY full_name, uuid",
                preparedStatement -> {
                    ResultSet resultSet = preparedStatement.executeQuery();
                    List<Resume> resumes = new ArrayList<>();
                    while (resultSet.next()) {
                        resumes.add(new Resume(resultSet.getString("uuid").trim(),
                                resultSet.getString("full_name")));
                    }
                    return resumes;
                });
    }

    @Override
    public int size() {
        return sqlHelper.executeQuery("SELECT COUNT(*) AS size FROM resume",
                preparedStatement -> {
                    ResultSet resultSet = preparedStatement.executeQuery();
                    resultSet.next();
                    return resultSet.getInt("size");
                });
    }
}
