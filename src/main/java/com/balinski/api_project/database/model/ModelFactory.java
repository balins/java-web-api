package com.balinski.api_project.database.model;

import com.balinski.api_project.database.dao.DaoType;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Map;

public class ModelFactory<T extends DatabaseModel> {
    public T getModel(DaoType type, Map<String, Object> map) throws ClassNotFoundException {
        switch(type) {
            case ACTOR:
                return actorFromMap(map);
            case FILM:
                return filmFromMap(map);
            case LANGUAGE:
                return languageFromMap(map);
            case USER:
                return userFromMap(map);
            default:
                throw new ClassNotFoundException("Could not find DAO for class " + type.toString());
        }
    }

    private T actorFromMap(Map<String, Object> map) {
        return (T) new Actor((int)map.get("ACTOR_ID"), (String)map.get("FIRST_NAME"),
                (String)map.get("LAST_NAME"), ((Timestamp)map.get("LAST_UPDATE")).toLocalDateTime());
    }

    private T filmFromMap(Map<String, Object> map) {
        return (T) new Film((int)map.get("FILM_ID"), (String)map.get("TITLE"),
                (String)map.get("DESCRIPTION"), ((Date)map.get("RELEASE_YEAR")).toLocalDate(),
                (int)map.get("LANGUAGE_ID"), (int)map.get("RENTAL_DURATION"), (BigDecimal)map.get("RENTAL_RATE"),
                (int)map.get("LENGTH"), ((Timestamp)map.get("LAST_UPDATE")).toLocalDateTime());
    }

    private T languageFromMap(Map<String, Object> map) {
        return (T) new Language((int)map.get("LANGUAGE_ID"), (String)map.get("NAME"),
                ((Timestamp)map.get("LAST_UPDATE")).toLocalDateTime());
    }

    private T userFromMap(Map<String, Object> map) {
        return (T) new User((int)map.get("USER_ID"), (String)map.get("ROLE"), (String)map.get("NAME"),
                (String)map.get("TOKEN"), (int)map.get("REQUESTS_SENT"), (int)map.get("USE_LIMIT"),
                ((Timestamp)map.get("DATE_REGISTERED")).toLocalDateTime(),
                ((Timestamp)map.get("LAST_UPDATE")).toLocalDateTime());
    }

}
