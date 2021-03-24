package com.basejava.webapp.sql;

import com.basejava.webapp.exception.StorageException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SqlHelper {

    private final ConnectionFactory connectionFactory;

    public SqlHelper(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @FunctionalInterface
    public interface ExecuteQuery<T> {
        T execute(Connection connection, PreparedStatement statement) throws SQLException;
    }

    public <T> T executeQuery(String statement, ExecuteQuery<T> action) {
        try (Connection connection = connectionFactory.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(statement);
            return action.execute(connection, preparedStatement);
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }
}
