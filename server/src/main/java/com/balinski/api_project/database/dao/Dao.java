package com.balinski.api_project.database.dao;

import com.balinski.api_project.database.DaoManager;

import java.time.format.DateTimeFormatter;
import java.util.List;

abstract class Dao {
    protected DaoManager manager;
    protected boolean transaction;
    protected static Integer count;
    protected static final DateTimeFormatter toDateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    protected Dao(DaoManager manager, boolean transaction) {
        this.manager = manager;
        this.transaction = transaction;
        if(count == null)
            count = getCount();
    }

    public abstract Integer getCount();
    public abstract List<?> getAll();
    public abstract Object getById(int id);
    public abstract List<?> getIdBetween(int start, int stop);
    public abstract int add(Object obj);
    public abstract int addAll(List<Object> list);
}
