package com.balinski.api_project.server;

import com.balinski.api_project.handler.MyContextHandler;
import org.eclipse.jetty.server.Server;

public class JettyServer {

    public static void start(int port) {
        final Server server = new Server(port);

        MyContextHandler contextHandler = new MyContextHandler();
        server.setHandler(contextHandler);

        try {
            server.start();
            server.join();
        } catch (Exception e) {
            System.err.println("Server could not be started.");
            System.err.println(e.getMessage());
            System.err.println("Shutting the application...");
            System.exit(-1);
        }
    }
}
