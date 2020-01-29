package com.balinski.api_project.servlet;

import com.balinski.api_project.database.dao.DaoManager;
import com.balinski.api_project.database.dao.DaoException;
import com.balinski.api_project.database.model.Actor;
import com.balinski.api_project.database.model.User;
import com.balinski.api_project.servlet.util.JsonResponseBuilder;
import com.balinski.api_project.servlet.util.StringToArrayConverter;
import com.balinski.api_project.servlet.util.UserAuthenticator;

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

        var supportedParameters = Set.of("id", "page", "perPage", "firstName", "lastName", "order", "user", "token");
        var param = Collections.list(enumeration)
                .stream()
                .distinct()
                .filter(supportedParameters::contains)
                .collect(Collectors.toMap(x -> x, req::getParameter));

        try {
            User user = UserAuthenticator.authenticateAndGet(param.get("user"), param.get("token"));
            UserAuthenticator.incrementUses(user);

            List<Actor> actors = new LinkedList<>();
            var dao = DaoManager.getActorDao();

            if(param.get("id") != null) {
                int[] ids = StringToArrayConverter.toIntArray(param.get("id"));
                actors.addAll(dao.getManyByIds(ids));
            } else if(param.get("firstName") != null) {
                for(var firstName : param.get("firstName").split(","))
                    actors.addAll(dao.getByFirstName(firstName));
            } else if(param.get("lastName") != null) {
                for(var lastName : param.get("lastName").split(","))
                    actors.addAll(dao.getByLastName(lastName));
            } else {
                actors = dao.getAll();
            }

            if(param.get("order") != null) {
                if(param.get("order").equalsIgnoreCase("desc"))
                    actors.sort(Comparator.comparing(Actor::getLastName).thenComparing(Actor::getFirstName).reversed());
                else
                    actors.sort(Comparator.comparing(Actor::getLastName).thenComparing(Actor::getFirstName));
            }

            if(param.get("perPage") != null && param.get("page") != null) {
                int perPage = Integer.parseInt(param.get("perPage"));
                int page = Integer.parseInt(param.get("page"));
                int firstRecord = Math.min(perPage*page, actors.size());
                int lastRecord = Math.min(perPage * (page + 1), actors.size());
                actors = actors.subList(firstRecord, lastRecord);
            }

            String response = JsonResponseBuilder.mergeFromList(actors);
            writer.print(response);
        } catch (DaoException e) {
            writer.print(JsonResponseBuilder.getErrorJson(e));
        }
    }
}
