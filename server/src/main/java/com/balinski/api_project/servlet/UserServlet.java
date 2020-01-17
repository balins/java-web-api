package com.balinski.api_project.servlet;

import com.balinski.api_project.database.DaoManager;
import com.balinski.api_project.database.dao.DaoException;
import com.balinski.api_project.util.JsonResponseBuilder;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class UserServlet extends HttpServlet {
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setHeader("Content-Type", "application/vnd.api+json");
        PrintWriter writer = resp.getWriter();
        try {
            var users = new DaoManager().getUserDao().getAll();
            String response = JsonResponseBuilder.mergeFromList(users);
            writer.print(response);
        } catch (DaoException e) {
            writer.print("{\"errors\":[{\"title\":\"Internal server error\"," +
                    "\"detail\":\"Could not obtain an instance of DAO for given model\"}],\"data\":[]}");
        }
    }
}
