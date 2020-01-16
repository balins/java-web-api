package com.balinski.api_project.database.dao;

import com.balinski.api_project.database.DaoManager;
import com.balinski.api_project.database.model.User;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserDao extends Dao {
    public UserDao(DaoManager manager, boolean transaction) {
        super(manager, transaction);
    }

    public User getById(int id) {
        List<Map<String, Object>> result = manager.queryGetData(
                "SELECT * FROM USER U WHERE U.USER_ID = " + id + ";"
        );

        return result.size() > 0 ? toListOfUsers(result).get(0) : null;
    }

    public List<User> getByName(String name) {
        List<Map<String, Object>> result = manager.queryGetData(
                "SELECT * FROM USER U WHERE lower(U.NAME) = '" + name.toLowerCase() + "';"
        );

        return toListOfUsers(result);
    }

    @Override
    public Integer getCount() {
        List<Map<String, Object>> result = manager.queryGetData(
                "SELECT COUNT(*) AS COUNT FROM USER;"
        );

        return ((Long)result.get(0).get("COUNT")).intValue();
    }

    @Override
    public List<?> getAll() {
        List<Map<String, Object>> result = manager.queryGetData(
                "SELECT * FROM USER;"
        );

        return toListOfUsers(result);
    }

    @Override
    public List<?> getIdBetween(int start, int stop) {
        List<Map<String, Object>> result = manager.queryGetData(
                "SELECT * FROM USERS U WHERE U.USER_ID BETWEEN " + start + " AND " + stop + ";"
        );

        return toListOfUsers(result);
    }

    @Override
    public int add(Object obj) {
        if(!(obj instanceof User))
            return 0;

        User user = (User) obj;

        String sql = String.format("INSERT INTO USER VALUES (%d, '%s', '%s', %d, TIMESTAMP '%s', TIMESTAMP '%s');",
                user.getId(), user.getName(), user.getToken(), user.getLimit(),
                user.getDateRegistered().format(toDateTime), user.getLastUpdate().format(toDateTime));

        int rowsAdded = manager.queryModify(sql, transaction);
        count += rowsAdded;

        return rowsAdded;
    }

    @Override
    public int addAll(List<Object> list) {
        if(list == null || list.size() == 0)
            return 0;

        StringBuilder sql = new StringBuilder("INSERT INTO USER VALUES ");

        for(Object obj : list) {
            User user = (User) obj;

            sql.append(String.format("(%d, '%s', '%s', %d, TIMESTAMP '%s', TIMESTAMP '%s');",
                    user.getId(), user.getName(), user.getToken(), user.getLimit(),
                    user.getDateRegistered().format(toDateTime), user.getLastUpdate().format(toDateTime)));
        }

        sql.replace(sql.lastIndexOf(", "), sql.length(), ";");

        int rowsAdded = manager.queryModify(sql.toString(), transaction);
        count += rowsAdded;

        return rowsAdded;
    }

    public List<User> toListOfUsers(List<Map<String, Object>> listOfMaps) {
        if(listOfMaps == null)
            return null;

        List<User> listOfUsers = new ArrayList<>(listOfMaps.size());

        for(var user : listOfMaps) {
            listOfUsers.add(new User((int)user.get("USER_ID"), (String)user.get("NAME"),
                    (String)user.get("TOKEN"), (int)user.get("USED"), (int)user.get("LIMIT"),
                    ((Timestamp)user.get("DATE_REGISTERED")).toLocalDateTime(),
                    ((Timestamp)user.get("LAST_UPDATE")).toLocalDateTime()));
        }

        return listOfUsers;
    }
}
