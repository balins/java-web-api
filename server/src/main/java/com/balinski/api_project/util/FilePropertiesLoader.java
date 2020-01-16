package com.balinski.api_project.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class FilePropertiesLoader {
    public static Properties load(String path) throws IOException {
        String absolutePath = new File(path).getAbsolutePath();

        Properties props;
        try (InputStream input = new FileInputStream(absolutePath)) {
            props = new Properties();
            props.load(input);
        } catch (IOException e) {
            throw new IOException("Could not find or load database config file.", e);
        }

        return props;
    }
}
