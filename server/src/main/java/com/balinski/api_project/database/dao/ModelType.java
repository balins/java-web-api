package com.balinski.api_project.database.dao;

public enum ModelType {
    ACTOR("ACTOR"), FILM("FILM"), LANGUAGE("LANGUAGE"), USER("USER");

    private String tableName;

    ModelType(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public String toString() {
        return this.tableName;
    }
}
