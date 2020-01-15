package com.balinski.api_project.database;

import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.stream.Stream;

public class DatabaseProxy {
    static String url;
    static String driver;
    static String username;
    static String password;

    static {
        loadDatabaseProperties();
    }

    static Connection getDatabaseConnection() {
        Connection conn = null;

        try {
            conn = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            handleSqlException(e, "Could not connect to database.");
        }

        return conn;
    }

    static void closeDatabaseConnection(Connection conn) {
        try {
            conn.close();
        } catch (SQLException e) {
            handleSqlException(e, "Could not close the database connection.");
        }
    }

    private static void loadDatabaseProperties() {
        String pathToProperties = new File("server/src/main/resources/database.properties").getAbsolutePath();

        Properties props = null;
        try (InputStream input = new FileInputStream(pathToProperties)) {
            props = new Properties();
            props.load(input);
        } catch (IOException e) {
            handleConfigException(e, "Could not find or load database config file.");
        }

        url = props.getProperty("url");
        driver = props.getProperty("driver");
        username = props.getProperty("username");
        password = props.getProperty("password");

        if(Stream.of(url, driver, username, password).anyMatch(Objects::isNull)) {
            handleConfigException(null, "Cannot initialize database proxy: " +
                    "one or more of properties (url, driver, username, password) is missing");
        }

        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            handleConfigException(e, "Cannot initialize database proxy: driver class " + driver + " not found.");
        }
    }

    static void handleSqlException(SQLException e, String message) {
        System.err.println(message);
        System.err.println(e.getMessage());
        System.err.println("SQL state: " + e.getSQLState());
    }

    private static void handleConfigException(Exception e, String message) {
        System.err.println(message);
        if(e != null)
            System.err.println(e.getMessage());
        System.err.println("Shutting the application...");
        System.exit(-1);
    }
}
