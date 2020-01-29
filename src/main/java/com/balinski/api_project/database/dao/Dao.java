package com.balinski.api_project.database.dao;

import com.balinski.api_project.database.model.*;

import java.util.*;

abstract class Dao<T extends DatabaseModel> {
    protected DaoType type;

    protected Dao(DaoType type) {
        this.type = type;
    }

    public int getCount() throws DaoException {
        List<Map<String, Object>> result = DaoManager.getData(
                    String.format("SELECT COUNT(*) AS COUNT FROM %s;", type.toString())
        );

        return ((Long)result.get(0).get("COUNT")).intValue();
    }

    public int getMaxId() throws DaoException {
        List<Map<String, Object>> result = DaoManager.getData(
                String.format("SELECT MAX(%s_ID) AS ID FROM %s;", type.toString(), type.toString())
        );

        return result.size() > 0 ? (int)result.get(0).get("ID") : 0;
    }

    public T getById(int id) throws DaoException {
        List<Map<String, Object>> result = DaoManager.getData(
                    String.format("SELECT * FROM %s T WHERE T.%s_ID = %d;", type.toString(), type.toString(), id)
            );

        return result.size() > 0 ? toListOfObjects(result).get(0) : null;
    }

    public List<T> getManyByIds(int[] ids) throws DaoException {
        StringBuilder sb = new StringBuilder();

        for(var id : ids)
            sb.append(id).append(", ");

        sb.delete(sb.length()-2, sb.length());

        List<Map<String, Object>> result = DaoManager.getData(
                String.format("SELECT * FROM %s T WHERE T.%s_ID IN (%s);", type.toString(), type.toString(), sb.toString())
        );

        return toListOfObjects(result);
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

    public void add(List<T> list, boolean transaction) throws DaoException {
        if(list == null || list.size() == 0)
            return;

        StringBuilder sql = new StringBuilder(String.format("INSERT INTO %s VALUES ", type.toString()));

        for(Object obj : list) {
            DatabaseModel model = (DatabaseModel) obj;

            sql.append(String.format("(%s), ", model.asCsv()));
        }

        sql.replace(sql.lastIndexOf(", "), sql.length(), ";");

        DaoManager.modifyData(sql.toString(), transaction);
    }

    public List<T> delete(int id) throws DaoException {
        List<Map<String, Object>> result = DaoManager.modifyData(
                String.format("DELETE FROM %s WHERE %s_ID=%d;", type.toString(), type.toString(), id),
                false);

        return toListOfObjects(result);
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
