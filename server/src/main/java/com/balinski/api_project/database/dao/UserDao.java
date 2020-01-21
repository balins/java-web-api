package com.balinski.api_project.database.dao;

import com.balinski.api_project.database.model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class UserDao extends Dao<User> {
    static final DateTimeFormatter toDateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public UserDao() {
        super(DaoType.USER);
    }

    public List<User> getByName(String name) throws DaoException {
        List<Map<String, Object>> result = DaoManager.getData(
                String.format("SELECT * FROM USER U WHERE lower(U.NAME) = '%s';", name.toLowerCase())
        );

        return toListOfObjects(result);
    }

    public List<User> getByToken(String token) throws DaoException {
        List<Map<String, Object>> result = DaoManager.getData(
                String.format("SELECT * FROM USER U WHERE lower(U.token) = '%s';", token.toLowerCase())
        );

        return toListOfObjects(result);
    }

    public boolean incrementUses(int id) throws DaoException {
        return DaoManager.modifyData(
                String.format("UPDATE USER SET REQUESTS_SENT=" +
                        "(SELECT U.REQUESTS_SENT FROM USERS U WHERE U.ID=%d)+1, " +
                        "LAST_UPDATE='%s' " +
                        "WHERE U.ID=%d;", id, LocalDateTime.now().format(toDateTime), id),
                true) > 0;
    }
}
