package com.balinski.api_project.database.model;

import java.time.format.DateTimeFormatter;

public abstract class DatabaseModel {
    DateTimeFormatter toDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    DateTimeFormatter toDateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public abstract String asCsv();
    public abstract String asJson();
}
