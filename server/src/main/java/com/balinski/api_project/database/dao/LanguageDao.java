package com.balinski.api_project.database.dao;

import com.balinski.api_project.database.model.Language;

import java.util.*;

public class LanguageDao extends Dao<Language> {
    public LanguageDao(DaoManager manager, boolean transaction) {
        super(manager, DaoType.LANGUAGE, transaction);
    }

    public List<Language> getByName(String name) throws DaoException {
        List<Map<String, Object>> result = manager.getData(
                String.format("SELECT * FROM LANGUAGE L WHERE lower(L.NAME) = '%s';", name.toLowerCase())
        );

        return toListOfObjects(result);
    }
}
