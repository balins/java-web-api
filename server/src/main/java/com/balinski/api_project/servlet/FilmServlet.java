package com.balinski.api_project.servlet;

import com.balinski.api_project.database.dao.DaoException;
import com.balinski.api_project.database.dao.DaoManager;
import com.balinski.api_project.database.model.Film;
import com.balinski.api_project.database.model.User;
import com.balinski.api_project.servlet.util.JsonResponseBuilder;
import com.balinski.api_project.servlet.util.UserAuthenticator;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class FilmServlet extends HttpServlet {
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setHeader("Content-Type", "application/vnd.api+json");
        PrintWriter writer = resp.getWriter();

        var enumeration = req.getParameterNames();
        var supportedParameters = Set.of("id", "page", "perPage", "title", "language",
                "minLength", "maxLength", "lengthBetween", "order", "user", "token");
        var param = Collections.list(enumeration)
                .stream()
                .distinct()
                .filter(supportedParameters::contains)
                .collect(Collectors.toMap(x -> x, req::getParameter));

        try {
            User user = UserAuthenticator.authenticateAndGet(param.get("user"), param.get("token"));
            UserAuthenticator.incrementUses(user);

            List<Film> films;
            var dao = DaoManager.getFilmDao();

            if(param.get("id") != null)
                films = dao.getById(Integer.parseInt(param.get("id")));
            else if(param.get("title") != null)
                films = dao.getByTitle(param.get("title"));
            else if(param.get("language") != null)
                films = dao.getAvailableInLanguage(param.get("language"));
            else if(param.get("minLength") != null) {
                films = dao.getLongerThan(Integer.parseInt(param.get("minLength")));
                films.sort(Comparator.comparing(Film::getLength).thenComparing(Film::getTitle));
            }
            else if(param.get("maxLength") != null){
                films = dao.getShorterThan(Integer.parseInt(param.get("maxLength")));
                films.sort(Comparator.comparing(Film::getLength).thenComparing(Film::getTitle));
            }
            else if(param.get("lengthBetween") != null) {
                String[] length = param.get("lengthBetween").split(",");
                films = dao.getWithLengthBetween(Integer.parseInt(length[0]), Integer.parseInt(length[1]));
                films.sort(Comparator.comparing(Film::getLength).thenComparing(Film::getTitle));
            } else
                films = dao.getAll();

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
