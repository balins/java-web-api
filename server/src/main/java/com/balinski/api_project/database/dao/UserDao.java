package com.balinski.api_project.database.dao;

import com.balinski.api_project.database.model.DatabaseModel;

import java.util.List;
import java.util.Map;

public class UserDao extends Dao {
    public UserDao(DaoManager manager, boolean transaction) {
        super(manager, ModelType.USER, transaction);
    }

    public List<? super DatabaseModel> getByName(String name) {
        List<Map<String, Object>> result = manager.queryGetData(
                String.format("SELECT * FROM USER U WHERE lower(U.NAME) = '%s';", name.toLowerCase())
        );

        return toListOfObjects(result);
    }
}
