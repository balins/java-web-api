package com.balinski.api_project.database.dao;


import com.balinski.api_project.database.model.DatabaseModel;

import java.util.*;

public class LanguageDao extends Dao {
    public LanguageDao(DaoManager manager, boolean transaction) {
        super(manager, ModelType.LANGUAGE, transaction);
    }

    public List<? super DatabaseModel> getByName(String name) {
        List<Map<String, Object>> result = manager.queryGetData(
                String.format("SELECT * FROM LANGUAGE L WHERE lower(L.NAME) = '%s';", name.toLowerCase())
        );

        return toListOfObjects(result);
    }
}
