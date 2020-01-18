package com.balinski.api_project.database.dao;

import com.balinski.api_project.database.model.*;

import java.util.*;

abstract class Dao<T extends DatabaseModel> {
    protected boolean transaction;
    protected DaoType type;

    protected Dao(DaoType type, boolean transaction) {
        this.type = type;
        this.transaction = transaction;
    }

    public int getCount() throws DaoException {
        List<Map<String, Object>> result = DaoManager.getData(
                    String.format("SELECT COUNT(*) AS COUNT FROM %s;", type.toString())
        );

        return ((Long)result.get(0).get("COUNT")).intValue();
    }

    public T getById(int id) throws DaoException {
        List<Map<String, Object>> result = DaoManager.getData(
                    String.format("SELECT * FROM %s T WHERE T.%s_ID = %d;", type.toString(), type.toString(), id)
            );

        return result.size() > 0 ? toListOfObjects(result).get(0) : null;
    }

    public List<T> getAll() throws DaoException {
        List<Map<String, Object>> result = DaoManager.getData(
                    String.format("SELECT * FROM %s;", type.toString())
        );

        return toListOfObjects(result);
    }

    public List<T> getIdBetween(int start, int stop) throws DaoException {
        List<Map<String, Object>> result = DaoManager.getData(
                    String.format("SELECT * FROM %s T WHERE T.%s_ID BETWEEN %d AND %d;",
                            type.toString(), type.toString(), start, stop)
        );

        return toListOfObjects(result);
    }

    public int add(T obj) throws DaoException {
        if(obj == null)
            return 0;

        String sql = String.format("INSERT INTO %s VALUES (%s);", type.toString(), obj.asCsv());

        return DaoManager.modifyData(sql, transaction);
    }

    public int addAll(List<T> list) throws DaoException {
        if(list == null || list.size() == 0)
            return 0;

        StringBuilder sql = new StringBuilder(String.format("INSERT INTO %s VALUES ", type.toString()));

        for(Object obj : list) {
            DatabaseModel model = (DatabaseModel) obj;

            sql.append(String.format("(%s), ", model.asCsv()));
        }

        sql.replace(sql.lastIndexOf(", "), sql.length(), ";");

        return DaoManager.modifyData(sql.toString(), transaction);
    }

    protected List<T> toListOfObjects(List<Map<String, Object>> listOfMaps) throws DaoException {
        if(listOfMaps == null)
            return null;

        List<T> listOfObjects = new ArrayList<>(listOfMaps.size());

        ModelFactory<T> factory = new ModelFactory<>();

        for(var map : listOfMaps) {
            try {
                listOfObjects.add(factory.getModel(type, map));
            } catch (ClassNotFoundException e) {
                throw new DaoException("Could not find DAO for given class " + e.getMessage(), e);
            }
        }

        return listOfObjects;
    }
}
