package com.balinski.api_project.servlet;

import com.balinski.api_project.database.dao.DaoManager;
import com.balinski.api_project.database.dao.DaoException;
import com.balinski.api_project.database.model.Actor;
import com.balinski.api_project.servlet.util.JsonResponseBuilder;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;


public class ActorServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setHeader("Content-Type", "application/vnd.api+json");
        PrintWriter writer = resp.getWriter();

        try {
            List<Actor> actors = new DaoManager().getActorDao().getAll();
            String response = JsonResponseBuilder.mergeFromList(actors);
            writer.print(response);
        } catch (DaoException e) {
            System.err.println("An error occured in ActorServlet: " + e.getMessage());
            writer.print(JsonResponseBuilder.getErrorJson(e));
        }
    }
}
