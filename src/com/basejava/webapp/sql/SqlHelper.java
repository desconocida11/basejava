package com.basejava.webapp.sql;

import com.basejava.webapp.exception.ResumeExistsStorageException;
import com.basejava.webapp.exception.StorageException;
import com.basejava.webapp.model.Resume;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class SqlHelper {

    private final ConnectionFactory connectionFactory;

    public SqlHelper(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    private <T> T tryInConnection(SqlTransaction<T> executor) {
        try (Connection connection = connectionFactory.getConnection()) {
            return executor.execute(connection);
        } catch (SQLException e) {
            if ("23505".equals(e.getSQLState())) {
                throw new ResumeExistsStorageException(e);
            }
            throw new StorageException(e);
        }
    }

    public <T> T executeQuery(String statement, ThrowableExecutor<T> action) {
        return tryInConnection(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(statement);
            return action.execute(preparedStatement);
        });
    }

    public <T> void executeInConnection(Connection connection, String statement, ThrowableExecutor<T> action) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(statement)) {
            action.execute(ps);
        }
    }

    public <T> T transactionalExecute(SqlTransaction<T> executor) {
        return tryInConnection(connection -> {
            try {
                connection.setAutoCommit(false);
                T result = executor.execute(connection);
                connection.commit();
                return result;
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            }
        });
    }

    public void addSectionContact(Map<String, Resume> resumes, Connection connection, String query, ThrowableAddition adder) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet rsSection = preparedStatement.executeQuery();
            while (rsSection.next()) {
                String uuid = rsSection.getString("uuid");
                if (resumes.containsKey(uuid)) {
                    adder.execute(resumes.get(uuid), rsSection);
                }
            }
        }
    }

}
