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

    public List<User> getByRole(String role) throws DaoException {
        List<Map<String, Object>> result = DaoManager.getData(
                String.format("SELECT * FROM USER U WHERE lower(U.ROLE) = '%s';", role.toLowerCase())
        );

        return toListOfObjects(result);
    }

    public List<User> getByToken(String token) throws DaoException {
        List<Map<String, Object>> result = DaoManager.getData(
                String.format("SELECT * FROM USER U WHERE lower(U.token) = '%s';", token.toLowerCase())
        );

        return toListOfObjects(result);
    }

    public List<User> renewAccess(int id, int newLimit) throws DaoException {
        List<Map<String, Object>> result = DaoManager.modifyData(
                String.format("UPDATE USER SET REQUESTS_SENT=0, USE_LIMIT=%d, LAST_UPDATE='%s' WHERE USER_ID=%d;",
                        newLimit, LocalDateTime.now().format(toDateTime), id),
                false);

        return toListOfObjects(result);
    }

    public List<User> incrementUses(int id) throws DaoException {
        List<Map<String, Object>> result = DaoManager.modifyData(
                String.format("UPDATE USER SET REQUESTS_SENT=" +
                        "((SELECT U.REQUESTS_SENT FROM USER U WHERE U.USER_ID=%d)+1), " +
                        "LAST_UPDATE='%s' " +
                        "WHERE USER_ID=%d;", id, LocalDateTime.now().format(toDateTime), id),
                false);

        return toListOfObjects(result);
    }
}
