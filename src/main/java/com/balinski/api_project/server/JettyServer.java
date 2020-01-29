package com.balinski.api_project.server;

import com.balinski.api_project.database.DatabaseProxy;
import com.balinski.api_project.servlet.*;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

import java.util.Properties;

public class JettyServer {
    private static WebAppContext context = new WebAppContext();

    public static void start(int port, Properties dbProps) {
        final Server server = new Server(port);
        context.setContextPath("/");
        context.setDescriptor(JettyServer.class.getResource("/WEB-INF/web.xml").toString());
        context.setResourceBase(".");
        registerServlets();
        server.setHandler(context);

        DatabaseProxy.init(dbProps);

        try {
            server.start();
        } catch (Exception e) {
            throw new RuntimeException("Could not start the server.", e);
        }
    }


    private static void registerServlets() {
        context.addServlet(ActorServlet.class, "/actors/*");
        context.addServlet(FilmServlet.class, "/films/*");
        context.addServlet(LanguageServlet.class, "/languages/*");
        context.addServlet(UserServlet.class, "/users/*");
        context.addServlet(AdminServlet.class, "/admin/*");
    }

}
