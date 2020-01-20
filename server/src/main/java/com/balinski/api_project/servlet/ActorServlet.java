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
import java.util.*;
import java.util.stream.Collectors;


public class ActorServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setHeader("Content-Type", "application/vnd.api+json");
        PrintWriter writer = resp.getWriter();

        var enumeration = req.getParameterNames();

        var supportedParameters = Set.of("id", "page", "perPage", "firstName", "lastName", "order");
        var param = Collections.list(enumeration)
                .stream()
                .distinct()
                .filter(supportedParameters::contains)
                .collect(Collectors.toMap(x -> x, req::getParameter));

        List<Actor> actors;
        var dao = DaoManager.getActorDao();

        try {
            if(param.get("id") != null)
                actors = dao.getById(Integer.parseInt(param.get("id")));
            else if(param.get("firstName") != null)
                actors = dao.getByFirstName(param.get("firstName"));
            else if(param.get("lastName") != null)
                actors = dao.getByLastName(param.get("lastName"));
            else if(param.get("perPage") != null && param.get("page") != null) {
                int perPage = Integer.parseInt(param.get("perPage"));
                int page = Integer.parseInt(param.get("page"));
                actors = dao.getIdBetween(perPage*page+1, perPage*(page+1));
            } else {
                actors = dao.getAll();
            }

            if(param.get("order") != null) {
                if(param.get("order").equalsIgnoreCase("desc"))
                    actors.sort(Comparator.comparing(Actor::getLastName).thenComparing(Actor::getFirstName).reversed());
                else
                    actors.sort(Comparator.comparing(Actor::getLastName).thenComparing(Actor::getFirstName));
            }

            String response = JsonResponseBuilder.mergeFromList(actors);
            writer.print(response);
        } catch (DaoException e) {
            System.err.println("An error occurred in ActorServlet: " + e.getMessage());
            writer.print(JsonResponseBuilder.getErrorJson(e));
            e.printStackTrace();
        }
    }
}
