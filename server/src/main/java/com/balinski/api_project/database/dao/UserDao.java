package com.balinski.api_project.database.dao;

import com.balinski.api_project.database.DaoManager;
import com.balinski.api_project.database.model.User;

import java.util.List;
import java.util.Map;

public class UserDao extends Dao<User> {
    public UserDao(DaoManager manager, boolean transaction) {
        super(manager, DaoType.USER, transaction);
    }

    public List<User> getByName(String name) {
        List<Map<String, Object>> result = manager.queryGetData(
                String.format("SELECT * FROM USER U WHERE lower(U.NAME) = '%s';", name.toLowerCase())
        );

        return toListOfObjects(result);
    }
}
