package com.basejava.webapp.storage;

import com.basejava.webapp.exception.ResumeNotExistsStorageException;
import com.basejava.webapp.model.Resume;
import com.basejava.webapp.sql.SqlHelper;

import java.sql.DriverManager;
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
        sqlHelper.executeQuery("DELETE FROM resumes.public.resume",
                (connection, preparedStatement) -> preparedStatement.execute());
    }

    @Override
    public void save(Resume resume) {
        sqlHelper.executeQuery("INSERT INTO resumes.public.resume (uuid, full_name) VALUES (?,?)",
                (connection, preparedStatement) -> {
                    preparedStatement.setString(1, resume.getUuid());
                    preparedStatement.setString(2, resume.getFullName());
                    preparedStatement.execute();
                    return null;
                });
    }

    @Override
    public Resume get(String uuid) {
        return sqlHelper.executeQuery("SELECT * FROM resumes.public.resume r WHERE r.uuid=?",
                (connection, preparedStatement) -> {
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
        sqlHelper.executeQuery("DELETE FROM resumes.public.resume r WHERE r.uuid=?",
                (connection, preparedStatement) -> {
                    preparedStatement.setString(1, uuid);
                    preparedStatement.execute();
                    return null;
                });
    }

    @Override
    public void update(Resume resume) {
        sqlHelper.executeQuery("UPDATE resumes.public.resume r SET full_name=? WHERE r.uuid=?",
                (connection, preparedStatement) -> {
                    preparedStatement.setString(1, resume.getFullName());
                    preparedStatement.setString(2, resume.getUuid());
                    preparedStatement.execute();
                    return null;
                });
    }

    @Override
    public List<Resume> getAllSorted() {
        return sqlHelper.executeQuery("SELECT * FROM resumes.public.resume ORDER BY full_name",
                (connection, preparedStatement) -> {
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
        return sqlHelper.executeQuery("SELECT COUNT(*) AS size FROM resumes.public.resume",
                (connection, preparedStatement) -> {
                    ResultSet resultSet = preparedStatement.executeQuery();
                    if (!resultSet.next()) {
                        return 0;
                    }
                    return resultSet.getInt(1);
                });
    }
}
