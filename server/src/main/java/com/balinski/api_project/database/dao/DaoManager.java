package com.balinski.api_project.database.dao;

import com.balinski.api_project.database.DatabaseException;
import com.balinski.api_project.database.DatabaseProxy;

import java.util.List;
import java.util.Map;

public class DaoManager {
    private DaoManager(){}

    public static ActorDao getActorDao() {
        return new ActorDao();
    }

    public static FilmDao getFilmDao() {
        return new FilmDao();
    }

    public static LanguageDao getLanguageDao() {
        return new LanguageDao();
    }

    public static UserDao getUserDao() {
        return new UserDao();
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
