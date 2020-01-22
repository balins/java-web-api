package com.balinski.api_project.servlet;

import com.balinski.api_project.database.dao.DaoException;
import com.balinski.api_project.database.dao.DaoManager;
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

    private User addUser(String name, int limit) throws DaoException, NoSuchAlgorithmException {
        if(limit < 0)
            throw new DaoException("Limit can not be a negative number.");

        int newId = DaoManager.getUserDao().getMaxId()+1;
        User user = new User(newId, "user", name, sha256(name), 0,
                limit, LocalDateTime.now(), LocalDateTime.now());
        DaoManager.getUserDao().add(user);

        return user;
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
            User user;
            boolean success;

            switch(action) {
                case "add":
                    if(req.getParameter("name") == null || req.getParameter("limit") == null)
                        throw new DaoException("You have to provide name and limit to add new user.");

                    user = addUser(req.getParameter("name"), Integer.parseInt(req.getParameter("limit")));
                    response = JsonResponseBuilder.mergeFromList(List.of(user));
                    break;

                case "renew":
                    if(req.getParameter("id") == null || req.getParameter("limit") == null)
                        throw new DaoException("You have to provide id and new limit to renew user's access.");

                    success = DaoManager.getUserDao()
                            .renewAccess(Integer.parseInt(req.getParameter("id")),
                                    Integer.parseInt(req.getParameter("limit")));
                    if(!success)
                        throw new DaoException("Could not update the user. Check if provided parameters are valid.");

                    response = "User's access has been successfully renewed.";
                    break;

                case "delete":
                    if(req.getParameter("id") == null)
                        throw new DaoException("You have to provide id of user that you want to be deleted.");

                    success = DaoManager.getUserDao()
                            .delete(Integer.parseInt(req.getParameter("id")));

                    if(!success)
                        throw new DaoException("Could not delete the user. Check if provided id is valid.");

                    response = "The user has been successfully removed.";
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
