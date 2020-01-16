package com.balinski.api_project.util;

import java.sql.SQLException;

public class SqlExceptionPrinter {
    public static void print(String message, SQLException e) {
        System.err.println(message);
        System.err.println(e.getMessage());
        System.err.println("SQL state: " + e.getSQLState());
    }
}