package com.basejava.webapp.sql;

import com.basejava.webapp.exception.ResumeExistsStorageException;
import com.basejava.webapp.exception.StorageException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SqlHelper {

    private final ConnectionFactory connectionFactory;

    public SqlHelper(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public <T> T executeQuery(String statement, ThrowableExecutor<T> action) {
        try (Connection connection = connectionFactory.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(statement);
            return action.execute(preparedStatement);
        } catch (SQLException e) {
            if ("23505".equals(e.getSQLState())) {
                throw new ResumeExistsStorageException(e);
            }
            throw new StorageException(e);
        }
    }

    @FunctionalInterface
    public interface ThrowableExecutor<T> {
        T execute(PreparedStatement statement) throws SQLException;
    }
}
