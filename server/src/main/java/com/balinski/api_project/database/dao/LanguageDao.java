package com.balinski.api_project.database.dao;

import com.balinski.api_project.database.model.Language;

import java.util.*;

public class LanguageDao extends Dao<Language> {
    public LanguageDao() {
        super(DaoType.LANGUAGE);
    }

    public List<Language> getByName(String name) throws DaoException {
        List<Map<String, Object>> result = DaoManager.getData(
                String.format("SELECT * FROM LANGUAGE L WHERE lower(L.NAME) = '%s';", name.toLowerCase())
        );

        return toListOfObjects(result);
    }
}
