package com.balinski.api_project.database.dao;

public enum DaoType {
    ACTOR("ACTOR"), FILM("FILM"), LANGUAGE("LANGUAGE"), USER("USER");

    private String tableName;

    DaoType(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public String toString() {
        return this.tableName;
    }
}
