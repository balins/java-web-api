package com.balinski.api_project;

import com.balinski.api_project.server.JettyServer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {
        String sPort = null, sProps = null;

        for(int i = 0; i < args.length-1; i+=2) {
            var key = args[i];
            switch(key) {
                case "-port":
                    sPort = args[i+1];
                    break;
                case "-dbProps":
                    sProps = args[i+1];
                    break;
                default:
                    System.err.println("Unsupported argument: \"" + args[i+1] + "\"");
            }
        }

        if(sPort == null || sProps == null) {
            System.err.println("You have to provide all required arguments:\n" +
                    "\t-port [1024..49151]\n" +
                    "\t-dbProps path-to-db-properties\n");
            System.err.println("Shutting down the application...");
            System.exit(-1);
        }

        int port = 0;
        Properties dbProps = null;

        try {
            port = parsePort(sPort);
            dbProps = loadDbProps(sProps);
        } catch (IllegalArgumentException | IOException e) {
            System.err.println(e.getMessage());
            System.err.println("Shutting down the application...");
            System.exit(-1);
        }

        runApplication(port, dbProps);
    }

    static void runApplication(int port, Properties dbProps) {
        try {
            JettyServer.start(port, dbProps);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("A critical error occurred when trying to run the server.");
            System.err.println("The application will be stopped.");
            System.exit(-1);
        }
    }

    private static int parsePort(String sPort) throws IllegalArgumentException {
        int port;
        try {
            port = Integer.parseInt(sPort);
            if(port < 1024 || port > 49151) {
                throw new IllegalArgumentException("Port number is invalid. " +
                        "You have to use a port in the range between 1024 and 49151. Got " + sPort);
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Port number is invalid. " +
                    "You have to provide an integer value in the range between 1024 and 49151. Got " + sPort);
        }

        return port;
    }

    private static Properties loadDbProps(String pathToProps) throws IOException {
        String absolutePath = new File(pathToProps).getAbsolutePath();
        Properties props;

        try (InputStream input = new FileInputStream(absolutePath)) {
            props = new Properties();
            props.load(input);
        } catch (IOException e) {
            throw new IOException("Could not find database properties file under the given path: " + absolutePath);
        }

        return props;
    }
}
