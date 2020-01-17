package com.balinski.api_project.database.dao;


import com.balinski.api_project.database.DaoManager;
import com.balinski.api_project.database.model.Language;

import java.util.*;

public class LanguageDao extends Dao<Language> {
    public LanguageDao(DaoManager manager, boolean transaction) {
        super(manager, DaoType.LANGUAGE, transaction);
    }

    public List<Language> getByName(String name) {
        List<Map<String, Object>> result = manager.queryGetData(
                String.format("SELECT * FROM LANGUAGE L WHERE lower(L.NAME) = '%s';", name.toLowerCase())
        );

        return toListOfObjects(result);
    }
}
