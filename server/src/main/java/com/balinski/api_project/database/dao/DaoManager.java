package com.balinski.api_project.database.dao;

import com.balinski.api_project.database.DatabaseException;
import com.balinski.api_project.database.DatabaseProxy;

import java.util.List;
import java.util.Map;

public class DaoManager {
    private DaoManager(){}

    public static ActorDao getActorDao() {
        return new ActorDao(false);
    }

    public static FilmDao getFilmDao() {
        return new FilmDao(false);
    }

    public static LanguageDao getLanguageDao() {
        return new LanguageDao(false);
    }

    public static UserDao getUserDao() {
        return new UserDao(false);
    }

    public static ActorDao getActorDao(boolean transaction) {
        return new ActorDao( transaction);
    }

    public static FilmDao getFilmDao(boolean transaction) {
        return new FilmDao(transaction);
    }

    public static LanguageDao getLanguageDao (boolean transaction) {
        return new LanguageDao( transaction);
    }

    public static UserDao getUserDao (boolean transaction) {
        return new UserDao(transaction);
    }

    static List<Map<String, Object>> getData(String sql) throws DaoException {
        try {
            return DatabaseProxy.querySelect(sql);
        } catch (DatabaseException e) {
            throw new DaoException(e.getMessage());
        }
    }

    static int modifyData(String sql, boolean transaction) throws DaoException {
        try {
            return DatabaseProxy.queryUpdate(sql, transaction);
        } catch (DatabaseException e) {
            throw new DaoException(e.getMessage());
        }
    }
}
