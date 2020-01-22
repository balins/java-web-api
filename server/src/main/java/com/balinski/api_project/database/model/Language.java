package com.balinski.api_project.database.model;

import java.time.LocalDateTime;

public class Language extends DatabaseModel {
    final int id;
    String name;
    LocalDateTime lastUpdate;

    public Language(int id, String name, LocalDateTime lastUpdate) {
        this.id = id;
        this.name = name;
        this.lastUpdate = lastUpdate;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    @Override
    public String asJson() {
        return String.format("{\"type\":\"%s\",\"id\":\"%d\",\"attributes\":{\"name\":\"%s\",\"lastUpdate\":\"%s\"}}",
                    "languages", id, name, lastUpdate.format(toDateTime));
    }

    @Override
    public String asCsv() {
        return String.format("%d, '%s', TIMESTAMP '%s'",
                id, name, lastUpdate.format(toDateTime));
    }
}
