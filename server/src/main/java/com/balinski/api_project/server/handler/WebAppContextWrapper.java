package com.balinski.api_project.server.handler;

import com.balinski.api_project.servlet.ActorServlet;
import com.balinski.api_project.servlet.FilmServlet;
import com.balinski.api_project.servlet.LanguageServlet;
import com.balinski.api_project.servlet.UserServlet;
import org.eclipse.jetty.webapp.WebAppContext;

import java.io.*;

public class WebAppContextWrapper {
    private WebAppContext context;

    public WebAppContextWrapper() {
        context = new WebAppContext();
        context.setWar(new File("webapp/target/webapp.war").getAbsolutePath());
        context.setContextPath("/");

        registerServlets();
    }

    public WebAppContext getWebAppContext() {
        return context;
    }

    private void registerServlets() {
        context.addServlet(ActorServlet.class, "/actors");
        context.addServlet(FilmServlet.class, "/films");
        context.addServlet(LanguageServlet.class, "/languages");
        context.addServlet(UserServlet.class, "/users");
    }

}
