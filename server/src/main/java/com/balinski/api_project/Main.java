package com.balinski.api_project;

import com.balinski.api_project.database.DaoManager;
import com.balinski.api_project.database.DatabaseProxy;
import com.balinski.api_project.server.JettyServer;

import java.util.List;
import java.util.Map;


public class Main {

    public static void main(String[] args) {
        testDatabase();
        //runApplication();
    }

    static void runApplication() {
        try {
            JettyServer.start(8080);
        } catch (Exception e) {
            System.err.println("A critical error occurred when trying to run the server.");
            System.err.println("The application will be stopped.");
            System.exit(-1);
        }
    }

    static void testDatabase() {
        DatabaseProxy.getDatabaseConnection();
        List<Map<String, Object>> result = new DaoManager().query("SELECT * FROM ACTOR;");

        for(Map<String, Object> item : result) {
            System.out.println(item.get("FIRST_NAME"));
        }
    }

}
