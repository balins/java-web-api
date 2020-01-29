package com.balinski.api_project.servlet;

import com.balinski.api_project.database.dao.DaoException;
import com.balinski.api_project.database.dao.DaoManager;
import com.balinski.api_project.database.dao.UserDao;
import com.balinski.api_project.database.model.User;
import com.balinski.api_project.servlet.util.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class AdminServlet extends HttpServlet {
    UserDao userDao = DaoManager.getUserDao();

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setHeader("Content-Type", "application/vnd.api+json");
        PrintWriter writer = resp.getWriter();

        try {
            User admin = UserAuthenticator
                    .authenticateAndGet(req.getParameter("user"), req.getParameter("token"));

            if(!admin.getRole().equalsIgnoreCase("admin"))
                throw new DaoException("Only administrators are allowed to modify data.");

            if(req.getParameter("action") == null)
                throw new DaoException("You have to specify action that you want to perform (add, renew, delete).");

            String action = req.getParameter("action").toLowerCase();
            String ids, roles, names, limits;
            List<User> changed;

            switch(action) {
                case "add":
                    if(req.getParameter("role") == null || req.getParameter("name") == null || req.getParameter("limit") == null)
                        throw new DaoException("You have to provide role, name and limit to add new user.");

                    roles = req.getParameter("role");
                    names = req.getParameter("name");
                    limits = req.getParameter("limit");

                    changed = addUsers(roles, names, limits);
                    break;
                case "renew":
                    if(req.getParameter("id") == null || req.getParameter("limit") == null)
                        throw new DaoException("You have to provide id and new limit to renew user's access.");

                    ids = req.getParameter("id");
                    limits = req.getParameter("limit");

                    changed = renewAccess(ids, limits);
                    break;
                case "delete":
                    if(req.getParameter("id") == null)
                        throw new DaoException("You have to provide id of user that you want to delete.");

                    ids = req.getParameter("id");

                    changed = deleteUsers(ids);
                    break;
                default:
                    throw new DaoException("The action that you specified is not supported.");
            }

            String response = JsonResponseBuilder.mergeFromList(changed);
            writer.print(response);

        } catch (DaoException | NoSuchAlgorithmException | NumberFormatException e) {
            writer.print(JsonResponseBuilder.getErrorJson(e));
            e.printStackTrace();
        }
    }

    private List<User> addUsers(String sRoles, String sNames, String sLimits) throws DaoException, NoSuchAlgorithmException {
        int[] limits = StringToArrayConverter.toIntArray(sLimits);
        String[] roles = StringToArrayConverter.toStringArray(sRoles);
        String[] names = StringToArrayConverter.toStringArray(sNames);

        if(roles.length != limits.length || names.length != limits.length)
            throw new DaoException("Quantity of roles, names and limits do not match.");

        int id = userDao.getMaxId()+1;

        List<User> users = new ArrayList<>(limits.length);

        for(int i = 0; i < limits.length; i++) {
            if(limits[i] < 0)
                limits[i] = 0;

            users.add(new User(id+i, roles[i], names[i], Encoder.encodeSha256(names[i]), 0,
                    limits[i], LocalDateTime.now(), LocalDateTime.now()));
        }

        userDao.add(users, true);

        return users;
    }

    private List<User> renewAccess(String sIds, String sLimits) throws DaoException {
        int[] ids = StringToArrayConverter.toIntArray(sIds);
        int[] limits = StringToArrayConverter.toIntArray(sLimits);

        if(ids.length != limits.length)
            throw new DaoException("Quantity of IDs and limits do not match.");

        List<User> renewed = new ArrayList<>(ids.length);

        for(int i = 0; i < ids.length; i++) {
            if(limits[i] < 0)
                limits[i] = 0;
            userDao.renewAccess(ids[i], limits[i]);
            renewed.add(userDao.getById(ids[i]));
        }

        return renewed;
    }

    private List<User> deleteUsers(String sIds) throws DaoException {
        int[] ids = StringToArrayConverter.toIntArray(sIds);

        List<User> deleted = new ArrayList<>(ids.length);

        for (int id : ids) {
            deleted.add(userDao.getById(id));
            userDao.delete(id);
        }

        return deleted;
    }
}
