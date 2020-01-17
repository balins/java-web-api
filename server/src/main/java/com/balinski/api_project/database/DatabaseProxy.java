package com.balinski.api_project.database;

import com.balinski.api_project.util.SqlExceptionPrinter;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.*;
import java.util.*;
import java.util.stream.Stream;

public class DatabaseProxy {
    private BasicDataSource dataSource;
    private Connection connection;
    private String url;
    private String username;
    private String password;

    public DatabaseProxy(Properties props) {
        try {
            loadDatabaseProperties(props);
            initDataSource();
        } catch (Exception e) {
            System.err.println("Cannot initialize database data source: " + e.getMessage());
        }
    }

    public Connection getConnection() {
        try {
            this.connection = dataSource.getConnection();
        } catch (SQLException e) {
            SqlExceptionPrinter.print("Could not obtain an instance of connection from given data source.", e);
        }

        return connection;
    }

    public void closeConnection() {
        if(connection == null)
            return;

        try {
            this.connection.close();
        } catch (SQLException e) {
            SqlExceptionPrinter.print("Cannot close the connection.", e);
        }
    }

    private void initDataSource() {
        dataSource = new BasicDataSource();
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setMinIdle(5);
        dataSource.setMaxIdle(10);
        dataSource.setMaxOpenPreparedStatements(100);
    }

    private void loadDatabaseProperties(Properties props) throws NullPointerException, ClassNotFoundException {
        url = props.getProperty("url");
        String driver = props.getProperty("driver");
        username = props.getProperty("username");
        password = props.getProperty("password");

        if(Stream.of(url, driver, username, password).anyMatch(Objects::isNull)) {
            throw new NullPointerException("One or more of properties (url, driver, username, password) is missing");
        }

        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            throw new ClassNotFoundException("Driver class " + driver + " not found.", e);
        }
    }
}
