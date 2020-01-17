package com.balinski.api_project.database.dao;

import com.balinski.api_project.database.model.*;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

abstract class Dao {
    protected DaoManager manager;
    protected boolean transaction;
    protected ModelType type;
    protected static final DateTimeFormatter toDateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    protected Dao(DaoManager manager, ModelType type, boolean transaction) {
        this.manager = manager;
        this.type = type;
        this.transaction = transaction;
    }

    public int getCount() {
        List<Map<String, Object>> result = manager.queryGetData(
                String.format("SELECT COUNT(*) AS COUNT FROM %s;", type.toString())
        );

        return ((Long)result.get(0).get("COUNT")).intValue();
    }

    public Object getById(int id) {
        List<Map<String, Object>> result = manager.queryGetData(
                String.format("SELECT * FROM %s T WHERE T.%s_ID = %d;", type.toString(), type.toString(), id)
        );

        return result.size() > 0 ? toListOfObjects(result).get(0) : null;
    }

    public List<? super DatabaseModel> getAll() {
        List<Map<String, Object>> result = manager.queryGetData(
                String.format("SELECT * FROM %s;", type.toString())
        );

        return toListOfObjects(result);
    }

    public List<? super DatabaseModel> getIdBetween(int start, int stop) {
        List<Map<String, Object>> result = manager.queryGetData(
                String.format("SELECT * FROM %s T WHERE T.%s_ID BETWEEN %d AND %d;",
                        type.toString(), type.toString(), start, stop)
        );

        return toListOfObjects(result);
    }

    public int add(Object obj) {
        if(!(obj instanceof DatabaseModel))
            return 0;

        DatabaseModel model = (DatabaseModel) obj;

        String sql = String.format("INSERT INTO %s VALUES %s;", type.toString(), model.asTuple());

        return manager.queryModify(sql, transaction);
    }

    public int addAll(List<? super DatabaseModel> list) {
        if(list == null || list.size() == 0)
            return 0;

        StringBuilder sql = new StringBuilder(String.format("INSERT INTO %s VALUES ", type.toString()));

        for(Object obj : list) {
            DatabaseModel model = (DatabaseModel) obj;

            sql.append(String.format("%s, ", model.asTuple()));
        }

        sql.replace(sql.lastIndexOf(", "), sql.length(), ";");

        return manager.queryModify(sql.toString(), transaction);
    }

    protected List<? super DatabaseModel> toListOfObjects(List<Map<String, Object>> listOfMaps) {
        if(listOfMaps == null)
            return null;

        List<? super DatabaseModel> listOfObjects = new ArrayList<>(listOfMaps.size());

        for(var map : listOfMaps)
            listOfObjects.add(ModelFactory.getModel(type, map));

        return listOfObjects;
    }
}
