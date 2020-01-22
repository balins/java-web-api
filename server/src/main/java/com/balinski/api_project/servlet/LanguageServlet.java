package com.balinski.api_project.servlet;

import com.balinski.api_project.database.dao.DaoException;
import com.balinski.api_project.database.dao.DaoManager;
import com.balinski.api_project.database.model.Language;
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

public class LanguageServlet extends HttpServlet {
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setHeader("Content-Type", "application/vnd.api+json");
        PrintWriter writer = resp.getWriter();

        var enumeration = req.getParameterNames();

        var supportedParameters = Set.of("id", "page", "perPage", "name", "order", "user", "token");
        var param = Collections.list(enumeration)
                .stream()
                .distinct()
                .filter(supportedParameters::contains)
                .collect(Collectors.toMap(x -> x, req::getParameter));

        try {
            User user = UserAuthenticator.authenticateAndGet(param.get("user"), param.get("token"));
            UserAuthenticator.incrementUses(user);

            List<Language> languages;
            var dao = DaoManager.getLanguageDao();

            if(param.get("id") != null)
                languages = dao.getById(Integer.parseInt(param.get("id")));
            else if(param.get("name") != null)
                languages = dao.getByName(param.get("name"));
            else
                languages = dao.getAll();

            if(param.get("order") != null) {
                if(param.get("order").equalsIgnoreCase("desc"))
                    languages.sort(Comparator.comparing(Language::getName).thenComparing(Language::getId).reversed());
                else
                    languages.sort(Comparator.comparing(Language::getName).thenComparing(Language::getId));
            }

            if(param.get("perPage") != null && param.get("page") != null) {
                int perPage = Integer.parseInt(param.get("perPage"));
                int page = Integer.parseInt(param.get("page"));
                int firstRecord = Math.min(perPage*page, languages.size());
                int lastRecord = Math.min(perPage * (page + 1), languages.size());
                languages = languages.subList(firstRecord, lastRecord);
            }

            String response = JsonResponseBuilder.mergeFromList(languages);
            writer.print(response);
        } catch (DaoException e) {
            System.err.println("An error occurred in LanguageServlet: " + e.getMessage());
            writer.print(JsonResponseBuilder.getErrorJson(e));
        }
    }
}
