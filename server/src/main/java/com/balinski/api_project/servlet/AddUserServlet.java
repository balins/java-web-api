package com.balinski.api_project.servlet;

import com.balinski.api_project.database.dao.DaoException;
import com.balinski.api_project.database.dao.DaoManager;
import com.balinski.api_project.database.model.User;
import com.balinski.api_project.servlet.util.JsonResponseBuilder;

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


public class AddUserServlet extends HttpServlet {
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

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setHeader("Content-Type", "application/vnd.api+json");
        PrintWriter writer = resp.getWriter();

        try {
            if(req.getParameter("name") == null || req.getParameter("limit") == null)
                throw new DaoException("You have to provide name and limit to add new user.");

            String name = req.getParameter("name");
            int limit = Integer.parseInt(req.getParameter("limit"));

            if(limit < 0)
                throw new DaoException("Limit can not be a negative number.");

            int newId = DaoManager.getUserDao().getCount()+1;
            User user = new User(newId, name, sha256(name), 0, limit, LocalDateTime.now(), LocalDateTime.now());
            DaoManager.getUserDao().add(user);

            String response = JsonResponseBuilder.mergeFromList(List.of(user));
            writer.print(response);
        } catch (DaoException | NoSuchAlgorithmException | NumberFormatException e) {
            writer.print(JsonResponseBuilder.getErrorJson(e));
            e.printStackTrace();
        }
    }
}
