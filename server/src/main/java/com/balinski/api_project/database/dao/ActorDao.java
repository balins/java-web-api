package com.balinski.api_project.database.dao;


import com.balinski.api_project.database.model.DatabaseModel;

import java.util.*;

public class ActorDao extends Dao {
    public ActorDao(DaoManager manager, boolean transaction) {
        super(manager, ModelType.ACTOR, transaction);
    }

    public List<? super DatabaseModel> getByFirstName(String firstName) {
        List<Map<String, Object>> result = manager.queryGetData(
                String.format("SELECT * FROM ACTOR A WHERE lower(A.FIRST_NAME) = '%s';", firstName.toLowerCase())
        );

        return toListOfObjects(result);
    }

    public List<? super DatabaseModel> getByLastName(String lastName) {
        List<Map<String, Object>> result = manager.queryGetData(
                String.format("SELECT * FROM ACTOR A WHERE lower(A.LAST_NAME) = '%s';", lastName.toLowerCase())
        );

        return toListOfObjects(result);
    }

}
