package com.balinski.api_project;

import com.balinski.api_project.database.dao.DaoManager;

import com.balinski.api_project.database.dao.DaoException;
import com.balinski.api_project.database.model.Film;
import com.balinski.api_project.server.JettyServer;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        //testDatabase();
        runApplication();
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
        try {
            List<Film> list = DaoManager.getFilmDao().getAll();
            for(var film : list)
                System.out.println(film.getTitle());
        } catch (DaoException e) {
            e.printStackTrace();
        }
    }
}
