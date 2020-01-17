package com.balinski.api_project.server.handler;

import com.balinski.api_project.servlet.ActorServlet;
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
    }

}
