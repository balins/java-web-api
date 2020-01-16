package com.balinski.api_project.database.model;

import java.time.LocalDateTime;

public class Actor implements Jsonable {
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

    public String toJson() {
        return String.format("{\"id\":%d,\"firstName\":\"%s\",\"lastName\":\"%s\",\"lastUpdate\":\"%s\"}",
                this.id, this.firstName, this.lastName, this.lastUpdate.toString());
    }
}
