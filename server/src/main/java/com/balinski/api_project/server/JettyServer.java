package com.balinski.api_project.server;

import com.balinski.api_project.server.handler.WebAppContextWrapper;
import org.eclipse.jetty.server.Server;

public class JettyServer {

    public static void start(int port) {
        final Server server = new Server(port);

        WebAppContextWrapper contextHandler = new WebAppContextWrapper();
        server.setHandler(contextHandler.getWebAppContext());

        try {
            server.start();
        } catch (Exception e) {
            throw new RuntimeException("Could not start the server.", e);
        }
    }
}
