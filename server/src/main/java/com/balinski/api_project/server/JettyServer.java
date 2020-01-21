package com.balinski.api_project.server;

import com.balinski.api_project.servlet.*;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;

public class JettyServer {
    private static ServletContextHandler context = new ServletContextHandler();

    public static void start(int port) {
        final Server server = new Server(port);
        context.setContextPath("/");
        registerServlets();
        server.setHandler(context);

        try {
            server.start();
        } catch (Exception e) {
            throw new RuntimeException("Could not start the server.", e);
        }
    }


    private static void registerServlets() {
        context.addServlet(ActorServlet.class, "/actors");
        context.addServlet(FilmServlet.class, "/films/*");
        context.addServlet(LanguageServlet.class, "/languages/*");
        context.addServlet(UserServlet.class, "/users/*");
        context.addServlet(AddUserServlet.class, "/adduser/*");
    }

}
