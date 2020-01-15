package com.balinski.api_project;

import com.balinski.api_project.server.JettyServer;

public class Main {

    public static void main(String[] args) {
        JettyServer.start(8080);
    }
    
}
