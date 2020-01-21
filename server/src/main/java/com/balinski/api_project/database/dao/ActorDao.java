package com.balinski.api_project.database.dao;

import com.balinski.api_project.database.model.Actor;

import java.util.*;

public class ActorDao extends Dao<Actor> {
    public ActorDao() {
        super(DaoType.ACTOR);
    }

    public List<Actor> getByFirstName(String firstName) throws DaoException {
        List<Map<String, Object>> result = DaoManager.getData(
                String.format("SELECT * FROM ACTOR A WHERE lower(A.FIRST_NAME) = '%s';", firstName.toLowerCase())
        );

        return toListOfObjects(result);
    }

    public List<Actor> getByLastName(String lastName) throws DaoException {
        List<Map<String, Object>> result = DaoManager.getData(
                String.format("SELECT * FROM ACTOR A WHERE lower(A.LAST_NAME) = '%s';", lastName.toLowerCase())
        );

        return toListOfObjects(result);
    }

}
