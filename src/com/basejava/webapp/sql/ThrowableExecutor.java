package com.basejava.webapp.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface ThrowableExecutor<T> {
    T execute(PreparedStatement statement) throws SQLException;
}
