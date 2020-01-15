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
    static boolean isInitialized = false;

    public DatabaseProxy() {
        if(!isInitialized)
            loadDatabaseProperties();
    }

    static Connection getDatabaseConnection() {
        Connection conn = null;

        try {
            conn = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            handleSqlException(e, "Could not connect to database");
        }

        return conn;
    }

    static void closeDatabaseConnection(Connection conn) {
        try {
            conn.close();
        } catch (SQLException e) {
            handleSqlException(e, "Could not close the database connection");
        }
    }

    static void handleSqlException(SQLException sqle, String message) {
        System.err.println(message);
        System.err.println("SQL state: " + sqle.getSQLState());

        Exception e = sqle.getNextException();
        while(e != null) {
            System.err.println(e.getMessage());
            e = sqle.getNextException();
        }
    }

    private static void loadDatabaseProperties() {
        String pathToProperties = new File("server/src/main/resources/database.properties").getAbsolutePath();

        Properties props = null;
        try (InputStream input = new FileInputStream(pathToProperties)) {
            props = new Properties();
            props.load(input);

        } catch (IOException e) {
            System.err.println("Could not find or load database config file: " + e.getMessage());
            System.err.println("Shutting down the server...");
            System.exit(-1);
        }

        url = props.getProperty("url");
        driver = props.getProperty("driver");
        username = props.getProperty("username");
        password = props.getProperty("password");

        if(Stream.of(url, driver, username, password).anyMatch(Objects::isNull)) {
            System.out.println("Cannot initialize database proxy: ");
            System.err.println("one or more of properties (url, driver, username, password) is missing");
            System.err.println("Shutting down the server...");
            System.exit(-1);
        }

        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            System.err.println("Cannot initialize database proxy:");
            System.err.println("Driver class " + driver + " not found: " + e.getCause());
            System.err.println("Shutting down the server...");
            System.exit(-1);
        }

        isInitialized = true;
    }
}
