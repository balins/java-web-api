package com.balinski.api_project.database;

import com.balinski.api_project.database.dao.*;
import com.balinski.api_project.util.FilePropertiesLoader;
import com.balinski.api_project.util.SqlExceptionPrinter;

import java.io.IOException;
import java.sql.*;
import java.util.*;

public class DaoManager {
    protected DatabaseProxy databaseProxy;

    public DaoManager() throws DaoException {
        try {
            initDataSource("server/src/main/resources/database.properties");
        } catch (IOException e) {
            throw new DaoException(e);
        }
    }

    public ActorDao getActorDao() {
        return new ActorDao(this, false);
    }

    public FilmDao getFilmDao() {
        return new FilmDao(this, false);
    }

    public LanguageDao getLanguageDao() {
        return new LanguageDao(this, false);
    }

    public UserDao getUserDao() {
        return new UserDao(this, false);
    }

    public ActorDao getActorDao(boolean transaction) {
        return new ActorDao(this, transaction);
    }

    public FilmDao getFilmDao(boolean transaction) {
        return new FilmDao(this, transaction);
    }

    public LanguageDao getLanguageDao (boolean transaction) {
        return new LanguageDao(this, transaction);
    }

    public UserDao getUserDao (boolean transaction) {
        return new UserDao(this, transaction);
    }

    public List<Map<String, Object>> queryGetData(String sql) {
        List<Map<String, Object>> data = null;

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
            SqlExceptionPrinter.print("An error occurred when trying to query the database", e);
        } finally {
            closeConnection();
        }

        return data;
    }

    public int queryModify(String sql, boolean transaction) {
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
            SqlExceptionPrinter.print("An error occurred when trying to query the database", e);
        } finally {
            closeConnection();
        }

        return rowsAffected;
    }

    public Connection getConnection() {
        return databaseProxy.getConnection();
    }

    protected void closeConnection() {
        databaseProxy.closeConnection();
    }

    protected void initDataSource(String path) throws IOException {
        Properties dbProps = FilePropertiesLoader.load(path);
        this.databaseProxy = new DatabaseProxy(dbProps);
    }

}
