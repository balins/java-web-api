package com.balinski.api_project;
import com.balinski.api_project.database.DatabaseProxy;
import com.balinski.api_project.server.JettyServer;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Main {

    public static void testDB() throws SQLException {

    }

    public static void main(String[] args) throws Exception {
        testDB();
        JettyServer.start(8080);
    }
}
