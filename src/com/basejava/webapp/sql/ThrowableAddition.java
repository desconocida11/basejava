package com.basejava.webapp.sql;

import com.basejava.webapp.model.Resume;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface ThrowableAddition {
    void execute(Resume resume, ResultSet resultSet) throws SQLException;
}
