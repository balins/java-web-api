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
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class UserServlet extends HttpServlet {
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setHeader("Content-Type", "application/vnd.api+json");
        PrintWriter writer = resp.getWriter();

        try {
            User admin = UserAuthenticator
                    .authenticateAndGet(req.getParameter("user"), req.getParameter("token"));

            if(!admin.getRole().equalsIgnoreCase("admin"))
                throw new DaoException("Only administrators are allowed to get users information.");

            String filter = req.getParameter("filter");
            if(filter == null)
                filter = "all";

            filter = filter.toLowerCase();
            List<User> users;

            switch(filter) {
                case "admin":
                    users = DaoManager.getUserDao().getByRole("admin");
                    break;
                case "user":
                    users = DaoManager.getUserDao().getByRole("user");
                    break;
                case "noAccess":
                    users = DaoManager.getUserDao().getAll()
                            .stream().filter(u -> u.getUsed() == u.getLimit())
                            .collect(Collectors.toList());
                    break;
                default:
                    users = DaoManager.getUserDao().getAll();
            }

            if(req.getParameter("order") != null) {
                if(req.getParameter("order").equalsIgnoreCase("desc"))
                    users.sort(Comparator.comparing(User::getId).thenComparing(User::getDateRegistered).reversed());
                else
                    users.sort(Comparator.comparing(User::getId).thenComparing(User::getDateRegistered));
            }

            if(req.getParameter("perPage") != null && req.getParameter("page") != null) {
                int perPage = Integer.parseInt(req.getParameter("perPage"));
                int page = Integer.parseInt(req.getParameter("page"));
                int firstRecord = Math.min(perPage*page, users.size());
                int lastRecord = Math.min(perPage * (page + 1), users.size());
                users = users.subList(firstRecord, lastRecord);
            }

            String response = JsonResponseBuilder.mergeFromList(users);
            writer.print(response);
        } catch (DaoException e) {
            System.err.println("An error occurred in UserServlet: " + e.getMessage());
            writer.print(JsonResponseBuilder.getErrorJson(e));
        }
    }
}
