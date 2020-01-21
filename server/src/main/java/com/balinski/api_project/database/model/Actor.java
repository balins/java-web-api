package com.balinski.api_project.database.model;

import java.time.LocalDateTime;

public class Actor extends DatabaseModel {
    final int id;
    String firstName;
    String lastName;
    LocalDateTime lastUpdate;

    public Actor(int id, String firstName, String lastName, LocalDateTime lastUpdate) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.lastUpdate = lastUpdate;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    @Override
    public String asJson() {
        return String.format("{\"type\":\"%s\",\"id\":\"%d\",\"attributes\":" +
                        "{\"firstName\":\"%s\",\"lastName\":\"%s\",\"lastUpdate\":\"%s\"}}",
                "actors", id, firstName, lastName, lastUpdate.format(toDateTime));
    }

    @Override
    public String asCsv() {
        return String.format("%d, '%s', '%s', TIMESTAMP '%s'",
                id, firstName, lastName, lastUpdate.format(toDateTime));
    }
}
