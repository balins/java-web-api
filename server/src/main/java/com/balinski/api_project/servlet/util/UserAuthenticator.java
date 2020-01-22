package com.balinski.api_project.servlet.util;

import com.balinski.api_project.database.dao.DaoException;
import com.balinski.api_project.database.dao.DaoManager;
import com.balinski.api_project.database.model.User;

import java.util.List;

public class UserAuthenticator {
    public static User authenticateAndGet(String username, String token) throws DaoException {
        if(username == null || token == null)
            throw new DaoException(("User not authenticated. " +
                    "Please provide your individual username and token with appropriate request parameters."));

        List<User> users = DaoManager.getUserDao().getByName(username);
        User user = null;
        for(var u : users) {
            if(u.getToken().equalsIgnoreCase(token))
                user = u;
        }

        if(user == null) {
            throw new DaoException("Provided username and/or token are invalid.");
        } else if(user.getUsed() == user.getLimit()) {
            throw new DaoException("Your usage limit has been reached. " +
                    "In order to continue using our service you need to get new authentication token.");
        }

        return user;
    }

    public static void incrementUses(User user) throws DaoException {
        boolean success = DaoManager.getUserDao().incrementUses(user.getId());

        if(!success)
            throw new DaoException("There was an error with authentication. " +
                    "Please try again later or contact us if problem still occurs.");
    }
}
