package com.balinski.api_project.handler;

import com.balinski.api_project.database.DaoManager;
import com.balinski.api_project.servlet.SimpleServlet;
import org.eclipse.jetty.webapp.WebAppContext;

import java.io.*;

public class MyContextHandler extends WebAppContext {

    public MyContextHandler() {
        this.setWar(new File("webapp/target/webapp.war").getAbsolutePath());
        this.setContextPath("/");

        DaoManager dao = new DaoManager();
        this.setAttribute("dao", dao);

        registerServlets();
    }

    private void registerServlets() {
        this.addServlet(SimpleServlet.class, "/simple");
    }

}
