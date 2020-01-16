package com.balinski.api_project.database.dao;


import com.balinski.api_project.database.DaoManager;
import com.balinski.api_project.database.model.Actor;

import java.sql.Timestamp;
import java.util.*;

public class ActorDao extends Dao {
    public ActorDao(DaoManager manager, boolean transaction) {
        super(manager, transaction);
    }

    public List<Actor> getByFirstName(String firstName) {
        List<Map<String, Object>> result = manager.queryGetData(
                "SELECT * FROM ACTOR A WHERE lower(A.FIRST_NAME) = '" + firstName.toLowerCase() + "';"
        );

        return toListOfActors(result);
    }

    public List<Actor> getByLastName(String lastName) {
        List<Map<String, Object>> result = manager.queryGetData(
                "SELECT * FROM ACTOR A WHERE lower(A.LAST_NAME) = '"  + lastName.toLowerCase() + "';"
        );

        return toListOfActors(result);
    }

    @Override
    public Integer getCount() {
        List<Map<String, Object>> result = manager.queryGetData(
                "SELECT COUNT(*) AS COUNT FROM ACTOR;"
        );

        return ((Long)result.get(0).get("COUNT")).intValue();
    }

    @Override
    public List<Actor> getAll() {
        List<Map<String, Object>> result = manager.queryGetData(
                "SELECT * FROM ACTOR;"
        );

        return toListOfActors(result);
    }

    public Object getById(int id) {
        List<Map<String, Object>> result = manager.queryGetData(
                "SELECT * FROM ACTOR A WHERE A.ACTOR_ID = " + id + ";"
        );

        return toListOfActors(result).get(0);
    }

    @Override
    public List<Actor> getIdBetween(int start, int stop) {
        List<Map<String, Object>> result = manager.queryGetData(
                "SELECT * FROM ACTOR A WHERE A.ACTOR_ID BETWEEN " + start + " AND " + stop + ";"
        );

        return toListOfActors(result);
    }

    @Override
    public int add(Object obj) {
        if(!(obj instanceof Actor))
            return 0;

        Actor actor = (Actor) obj;

        String sql = String.format("INSERT INTO ACTOR VALUES (%d, '%s', '%s', TIMESTAMP '%s');",
                actor.getId(), actor.getFirstName(), actor.getLastName(), actor.getLastUpdate().format(toDateTime));

        int rowsAdded = manager.queryModify(sql, transaction);
        count += rowsAdded;

        return rowsAdded;
    }

    @Override
    public int addAll(List<Object> list) {
        if(list == null || list.size() == 0)
            return 0;

        StringBuilder sql = new StringBuilder("INSERT INTO ACTOR VALUES ");

        for(Object obj : list) {
            Actor actor = (Actor) obj;

            sql.append(String.format("(%d, '%s', '%s', TIMESTAMP '%s'), ",
                    actor.getId(), actor.getFirstName(), actor.getLastName(), actor.getLastUpdate().format(toDateTime)));
        }

        sql.replace(sql.lastIndexOf(", "), sql.length(), ";");

        int rowsAdded = manager.queryModify(sql.toString(), transaction);
        count += rowsAdded;

        return rowsAdded;
    }

    public List<Actor> toListOfActors(List<Map<String, Object>> listOfMaps) {
        if(listOfMaps == null)
            return null;

        List<Actor> listOfActors = new ArrayList<>(listOfMaps.size());

        for(var actor : listOfMaps) {
            listOfActors.add(new Actor((int)actor.get("ACTOR_ID"), (String)actor.get("FIRST_NAME"),
                    (String)actor.get("LAST_NAME"), ((Timestamp)actor.get("LAST_UPDATE")).toLocalDateTime()));
        }

        return listOfActors;
    }

}
