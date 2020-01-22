package com.balinski.api_project.database.model;

import java.time.LocalDateTime;

public class User extends DatabaseModel {
    final int id;
    final String role;
    final String name;
    String token;
    int used;
    final int limit;
    final LocalDateTime dateRegistered;
    LocalDateTime lastUpdate;


    public User(int id, String role, String name, String token, int used, int limit,
                LocalDateTime dateRegistered, LocalDateTime lastUpdate) {
        this.id = id;
        this.role = role;
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

    public String getRole() {
        return role;
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

    public void setToken(String token) {
        this.token = token;
    }

    public void setUsed(int used) {
        this.used = used;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    @Override
    public String asJson(){
        return String.format("{\"type\":\"%s\",\"id\":\"%d\",\"attributes\":" +
                        "{\"role\":\"%s\",\"name\":\"%s\",\"token\":\"%s\",\"used\":\"%d\",\"limit\":\"%d\"," +
                        "\"dateRegistered\":\"%s\",\"lastUpdate\":\"%s\"}}",
                "users", id, role, name, token, used, limit, dateRegistered.format(toDateTime), lastUpdate.format(toDateTime));
    }

    @Override
    public String asCsv() {
        return String.format("%d, '%s', '%s', '%s', %d, %d, TIMESTAMP '%s', TIMESTAMP '%s'",
                id, role, name, token, used, limit, dateRegistered.format(toDateTime), lastUpdate.format(toDateTime));
    }
}
