package com.balinski.api_project.database.dao;


import com.balinski.api_project.database.DaoManager;
import com.balinski.api_project.database.model.Language;

import java.sql.Timestamp;
import java.util.*;

public class LanguageDao extends Dao {
    public LanguageDao(DaoManager manager, boolean transaction) {
        super(manager, transaction);
    }

    public Language getById(int id) {
        List<Map<String, Object>> result = manager.queryGetData(
                "SELECT * FROM LANGUAGE WHERE LANGUAGE_ID = " + id + ";"
        );

        return result.size() > 0 ? toListOfLanguages(result).get(0) : null;
    }

    public List<Language> getByName(String name) {
        List<Map<String, Object>> result = manager.queryGetData(
                "SELECT * FROM LANGUAGE L WHERE lower(L.NAME) = '" + name.toLowerCase() + "';"
        );

        return toListOfLanguages(result);
    }

    @Override
    public Integer getCount() {
        List<Map<String, Object>> result = manager.queryGetData(
                "SELECT COUNT(*) AS COUNT FROM LANGUAGE;"
        );

        return ((Long)result.get(0).get("COUNT")).intValue();
    }

    @Override
    public List<Language> getAll() {
        List<Map<String, Object>> result = manager.queryGetData(
                "SELECT * FROM LANGUAGE;"
        );

        return toListOfLanguages(result);
    }

    @Override
    public List<Language> getIdBetween(int start, int stop) {
        List<Map<String, Object>> result = manager.queryGetData(
                "SELECT * FROM LANGUAGE WHERE LANGUAGE_ID BETWEEN " + start + " AND " + stop + ";"
        );

        return toListOfLanguages(result);
    }

    @Override
    public int add(Object obj) {
        if(!(obj instanceof Language))
            return 0;

        Language language = (Language) obj;

        String sql = String.format(
                "INSERT INTO ACTOR VALUES (%d, '%s', TIMESTAMP '%s');",
                language.getId(), language.getName(), language.getLastUpdate().format(toDateTime)
        );

        int rowsAdded = manager.queryModify(sql, transaction);
        count += rowsAdded;

        return rowsAdded;
    }

    @Override
    public int addAll(List<Object> list) {
        if(list == null || list.size() == 0)
            return 0;

        StringBuilder sql = new StringBuilder("INSERT INTO LANGUAGE VALUES ");

        for(Object obj : list) {
            Language language = (Language) obj;

            sql.append(String.format("(%d, '%s', TIMESTAMP '%s'), ",
                    language.getId(), language.getName(), language.getLastUpdate().format(toDateTime)));
        }

        sql.replace(sql.lastIndexOf(", "), sql.length(), ";");

        int rowsAdded = manager.queryModify(sql.toString(), transaction);
        count += rowsAdded;

        return rowsAdded;
    }

    public List<Language> toListOfLanguages(List<Map<String, Object>> listOfMaps) {
        if(listOfMaps == null)
            return null;
        List<Language> listOfLanguages = new ArrayList<>(listOfMaps.size());

        for(var language : listOfMaps) {
            listOfLanguages.add(new Language((int)language.get("LANGUAGE_ID"), (String)language.get("NAME"),
                    ((Timestamp)language.get("LAST_UPDATE")).toLocalDateTime()));
        }

        return listOfLanguages;
    }

}
