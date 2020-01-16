package com.balinski.api_project.database.model;


import java.time.LocalDateTime;

public class Language implements Jsonable {
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

    public String toJson() {
        return String.format("{\"id\":%d,\"name\":\"%s\",\"lastUpdate\":\"%s\"}",
                    this.id, this.name, this.lastUpdate.toString());
    }
}
