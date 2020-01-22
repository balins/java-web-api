package com.balinski.api_project.database;

import org.apache.commons.dbcp2.BasicDataSource;

import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.stream.Stream;

public class DatabaseProxy {
    protected static BasicDataSource dataSource;
    protected static Connection connection;
    protected static String driver;
    protected static String url;
    protected static String username;
    protected static String password;

    static {
        loadDatabaseProperties();
        loadDriver();
        testConnection();
        initDataSource();
    }

    private DatabaseProxy(){};

    public static List<Map<String, Object>> querySelect(String sql) throws DatabaseException {
        List<Map<String, Object>> data;

        try(Connection connection = getConnection()) {
            try(Statement statement = connection.createStatement()) {
                try(ResultSet rs = statement.executeQuery(sql)) {
                    ResultSetMetaData md = rs.getMetaData();
                    int columns = md.getColumnCount();
                    data = new LinkedList<>();

                    while(rs.next()) {
                        Map<String, Object> row = new HashMap<>(columns);

                        for(int i = 1; i <= columns; i++)
                            row.put(md.getColumnName(i), rs.getObject(i));

                        data.add(row);
                    }
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("An error occurred when trying to query the database.", e);
        } finally {
            closeConnection();
        }

        return data;
    }

    public static int queryUpdate(String sql, boolean transaction) throws DatabaseException {
        int rowsAffected = 0;

        try(Connection connection = getConnection()) {
            if(transaction)
                connection.setAutoCommit(false);

            try(Statement statement = connection.createStatement()) {
                rowsAffected = statement.executeUpdate(sql);
            }

            if(transaction) {
                connection.setAutoCommit(true);
                connection.commit();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("An error occurred when trying to update the database.", e);
        } finally {
            closeConnection();
        }

        return rowsAffected;
    }

    protected static Connection getConnection() throws DatabaseException {
        try {
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            throw new DatabaseException("Could not obtain an instance of connection from given data source.", e);
        }

        return connection;
    }

    protected static void closeConnection() throws DatabaseException {
        if(connection == null)
            return;

        try {
            connection.close();
        } catch (SQLException e) {
            throw new DatabaseException("Cannot close the connection.", e);
        }
    }

    private static void testConnection() throws DatabaseException {
        if(Stream.of(url, driver, username, password).anyMatch(Objects::isNull)) {
            throw new DatabaseException("One of the database properties (url, driver, username, password) is missing. " +
                    "Check the file containing database properties.");
        }

        try(Connection ignored = DriverManager.getConnection(url, username, password)) {
            System.out.println("The database connection was configured successfully.");
        } catch (SQLException e) {
            throw new DatabaseException("Wrong credentials or internal database error.", e);
        }
    }

    protected static void initDataSource() {
        dataSource = new BasicDataSource();
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setMinIdle(5);
        dataSource.setMaxIdle(10);
        dataSource.setMaxOpenPreparedStatements(100);
    }

    protected static void loadDatabaseProperties() throws DatabaseException {
        Properties props;
        try {
            props = FilePropertiesLoader.load("server/src/main/resources/database.properties");
        } catch (IOException e) {
            throw new DatabaseException("Could not find database properties file under given path: "
                    + "server/src/main/resources/database.properties", e);
        }

        url = props.getProperty("url");
        driver = props.getProperty("driver");
        username = props.getProperty("username");
        password = props.getProperty("password");
    }

    private static void loadDriver() throws DatabaseException {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            throw new DatabaseException("Driver class " + driver + " not found.", e);
        }
    }
}
