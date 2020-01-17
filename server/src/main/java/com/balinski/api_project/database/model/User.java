package com.balinski.api_project.database.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class User implements DatabaseModel {
    final int id;
    final String name;
    final String token;
    int used;
    final int limit;
    final LocalDateTime dateRegistered;
    LocalDateTime lastUpdate;

    public User(int id, String name, String token, int used, int limit,
                LocalDateTime dateRegistered, LocalDateTime lastUpdate) {
        this.id = id;
        this.name = name;
        this.token = token;
        this.used = used;
        this.limit = limit;
        this.dateRegistered = dateRegistered;
        this.lastUpdate = lastUpdate;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getToken() {
        return token;
    }

    public int getUsed() {
        return used;
    }

    public int getLimit() {
        return limit;
    }

    public LocalDateTime getDateRegistered() {
        return dateRegistered;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setUsed(int used) {
        this.used = used;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    @Override
    public String asTuple() {
        return String.format("('%s', '%s', %d, %d, TIMESTAMP '%s', TIMESTAMP '%s')",
                name, token, used, limit,
                dateRegistered.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                lastUpdate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }
}
