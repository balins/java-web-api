package com.balinski.api_project.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

public class DaoManager {
    Connection connection;

    public Set<Properties> query(String sql, String[] selectedFields) {
        try(Connection connection = DatabaseProxy.getDatabaseConnection()) {
            try(Statement statement = connection.createStatement()) {
                try(ResultSet result = statement.executeQuery(sql)) {
                    Set<Properties> data = new HashSet<>();
                    Properties record = new Properties();

                    while(result.next()) {
                        for(var field : selectedFields) {
                            record.put(field, result.getString(field));
                        }
                        data.add(record);
                        record.clear();
                    }

                    return data;
                }
            }
        } catch (SQLException e) {
            DatabaseProxy.handleSqlException(e, "An error occurred when trying to query the database");
        }

        return null;
    }
}
