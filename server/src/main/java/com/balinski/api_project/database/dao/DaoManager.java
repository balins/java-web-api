package com.balinski.api_project.database.dao;

import com.balinski.api_project.database.DatabaseException;
import com.balinski.api_project.database.DatabaseProxy;

import java.util.List;
import java.util.Map;

public class DaoManager {
    protected DatabaseProxy databaseProxy;

    public DaoManager() throws DaoException {
        try {
            this.databaseProxy = new DatabaseProxy();
        } catch (DatabaseException e) {
            throw new DaoException("DaoManager could not be initialized due to database connection error.", e);
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

    List<Map<String, Object>> getData(String sql) throws DaoException {
        try {
            return this.databaseProxy.querySelect(sql);
        } catch (DatabaseException e) {
            throw new DaoException(e.getMessage());
        }
    }

    int modifyData(String sql, boolean transaction) throws DaoException {
        try {
            return this.databaseProxy.queryUpdate(sql, transaction);
        } catch (DatabaseException e) {
            throw new DaoException(e.getMessage());
        }
    }
}
