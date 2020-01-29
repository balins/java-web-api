package com.balinski.api_project.servlet;

import com.balinski.api_project.database.dao.DaoException;
import com.balinski.api_project.database.dao.DaoManager;
import com.balinski.api_project.database.model.Film;
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

public class FilmServlet extends HttpServlet {
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setHeader("Content-Type", "application/vnd.api+json");
        PrintWriter writer = resp.getWriter();

        var enumeration = req.getParameterNames();
        var supportedParameters = Set.of("id", "page", "perPage", "title", "language",
                "minLength", "maxLength", "order", "user", "token");
        var param = Collections.list(enumeration)
                .stream()
                .distinct()
                .filter(supportedParameters::contains)
                .collect(Collectors.toMap(x -> x, req::getParameter));

        try {
            User user = UserAuthenticator.authenticateAndGet(param.get("user"), param.get("token"));
            UserAuthenticator.incrementUses(user);

            List<Film> films = new LinkedList<>();
            var dao = DaoManager.getFilmDao();

            if(param.get("id") != null){
                int[] ids = StringToArrayConverter.toIntArray(param.get("id"));
                films.addAll(dao.getManyByIds(ids));
            } else if(param.get("title") != null){
                for(var title : param.get("title").split(","))
                    films.addAll(dao.getByTitle(title));
            } else if(param.get("language") != null){
                for(var language : param.get("language").split(","))
                    films.addAll(dao.getAvailableInLanguage(language));
            } else
                films = dao.getAll();

            if(param.get("minLength") != null || param.get("maxLength") != null) {
                if(param.get("minLength") != null) {
                    films = films.stream()
                            .filter(film -> film.getLength() >= Integer.parseInt(param.get("minLength")))
                            .collect(Collectors.toList());
                }
                if(param.get("maxLength") != null){
                    films = films.stream()
                            .filter(film -> film.getLength() <= Integer.parseInt(param.get("maxLength")))
                            .collect(Collectors.toList());
                }
                films.sort(Comparator.comparing(Film::getLength).thenComparing(Film::getTitle));
            }

            if(param.get("order") != null) {
                if(param.get("order").equalsIgnoreCase("desc"))
                    films.sort(Comparator.comparing(Film::getTitle).thenComparing(Film::getReleaseYear).reversed());
                else
                    films.sort(Comparator.comparing(Film::getTitle).thenComparing(Film::getReleaseYear));
            }

            if(param.get("perPage") != null && param.get("page") != null) {
                int perPage = Integer.parseInt(param.get("perPage"));
                int page = Integer.parseInt(param.get("page"));
                int firstRecord = Math.min(perPage*page, films.size());
                int lastRecord = Math.min(perPage * (page + 1), films.size());
                films = films.subList(firstRecord, lastRecord);
            }

            String response = JsonResponseBuilder.mergeFromList(films);
            writer.print(response);
        } catch (DaoException e) {
            System.err.println("An error occurred in FilmServlet: " + e.getMessage());
            writer.print(JsonResponseBuilder.getErrorJson(e));
        }
    }
}
