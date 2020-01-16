package com.balinski.api_project.database;

import java.sql.*;
import java.util.*;

public class DaoManager {

    public List<Map<String, Object>> query(String sql) {

        try(Connection connection = DatabaseProxy.getDatabaseConnection()) {
            try(Statement statement = connection.createStatement()) {
                try(ResultSet rs = statement.executeQuery(sql)) {
                    ResultSetMetaData md = rs.getMetaData();
                    int columns = md.getColumnCount();
                    List<Map<String, Object>> data = new LinkedList<>();

                    while(rs.next()) {
                        Map<String, Object> row = new HashMap<>(columns);

                        for(int i = 1; i <= columns; i++)
                            row.put(md.getColumnName(i), rs.getObject(i));

                        data.add(row);
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
