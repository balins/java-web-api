package com.balinski.api_project.server;

import com.balinski.api_project.handler.MyContextHandler;
import org.eclipse.jetty.server.Server;

public class JettyServer {

    public static void start(int port) throws Exception {
        final Server server = new Server(port);

        MyContextHandler contextHandler = new MyContextHandler();
        server.setHandler(contextHandler);

        server.start();
        server.join();
    }
}
