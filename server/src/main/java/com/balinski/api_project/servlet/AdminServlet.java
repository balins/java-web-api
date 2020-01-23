package com.balinski.api_project.servlet;

import com.balinski.api_project.database.dao.DaoException;
import com.balinski.api_project.database.dao.DaoManager;
import com.balinski.api_project.database.dao.UserDao;
import com.balinski.api_project.database.model.User;
import com.balinski.api_project.servlet.util.JsonResponseBuilder;
import com.balinski.api_project.servlet.util.UserAuthenticator;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class AdminServlet extends HttpServlet {
    private String sha256(String name) throws NoSuchAlgorithmException {
        name = name + LocalDateTime.now().toString();
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(
                name.getBytes(StandardCharsets.UTF_8));

        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }

        return hexString.toString();
    }

    private List<User> addUsers(String[] names, String[] limits) throws DaoException, NoSuchAlgorithmException {
        if(names.length != limits.length)
            throw new DaoException("Quantity of names and limits do not match.");

        UserDao dao = DaoManager.getUserDao();
        int id = dao.getMaxId()+1;
        List<User> users = new ArrayList<>(names.length);

        for(int i = 0; i < names.length; i++) {
            if(Integer.parseInt(limits[i]) < 0)
                limits[i] = "0";

            users.add(new User(id, "user", names[i], sha256(names[i]), 0,
                    Integer.parseInt(limits[i]), LocalDateTime.now(), LocalDateTime.now()));

            id++;
        }

        return DaoManager.getUserDao().add(users, true);
    }

    private List<User> renewAccess(String[] ids, String[] limits) throws DaoException {
        List<User> users = new ArrayList<>(ids.length);

        for(int i = 0; i < ids.length; i++) {
            if(Integer.parseInt(limits[i]) < 0)
                limits[i] = "0";

            users.addAll(DaoManager.getUserDao().renewAccess(Integer.parseInt(ids[i]), Integer.parseInt(limits[i])));
        }

        return users;
    }

    private List<User> deleteUsers(String[] ids) throws DaoException {
        List<User> users = new ArrayList<>(ids.length);
        for (String id : ids)
            users.addAll(DaoManager.getUserDao().delete(Integer.parseInt(id)));

        return users;
    }

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
            String response;
            List<User> users;
            String[] userIds, userNames, userLimits;

            switch(action) {
                case "add":
                    if(req.getParameter("name") == null || req.getParameter("limit") == null)
                        throw new DaoException("You have to provide name and limit to add new user.");

                    userNames = req.getParameter("name").split(",");
                    userLimits = req.getParameter("limit").split(",");

                    users = addUsers(userNames, userLimits);

                    response = JsonResponseBuilder.mergeFromList(users);
                    break;

                case "renew":
                    if(req.getParameter("id") == null || req.getParameter("limit") == null)
                        throw new DaoException("You have to provide id and new limit to renew user's access.");

                    userIds = req.getParameter("id").split(",");
                    userLimits = req.getParameter("limit").split(",");

                    if(userIds.length != userLimits.length)
                        throw new DaoException("Quantity of IDs and limits do not match.");

                    users = renewAccess(userIds, userLimits);

                    response = JsonResponseBuilder.mergeFromList(users);
                    break;

                case "delete":
                    if(req.getParameter("id") == null)
                        throw new DaoException("You have to provide id of user that you want to be deleted.");

                    userIds = req.getParameter("id").split(",");

                    users = deleteUsers(userIds);

                    response = JsonResponseBuilder.mergeFromList(users);
                    break;

                default:
                    throw new DaoException("The action that you specified is not supported.");
            }

            writer.print(response);
        } catch (DaoException | NoSuchAlgorithmException | NumberFormatException e) {
            writer.print(JsonResponseBuilder.getErrorJson(e));
            e.printStackTrace();
        }
    }
}
