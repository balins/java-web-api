package com.balinski.api_project.database.dao;

import com.balinski.api_project.database.DaoManager;
import com.balinski.api_project.database.model.Film;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FilmDao extends Dao {
    static final DateTimeFormatter toDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public FilmDao(DaoManager manager, boolean transaction) {
        super(manager, transaction);
    }

    public List<Film> getByTitle(String title) {
        List<Map<String, Object>> result = manager.queryGetData(
                "SELECT * FROM FILM F WHERE lower(F.TITLE) = '" + title.toLowerCase() + "';");

        return toListOfFilms(result);
    }

    public List<Film> getReleasedBetween(LocalDateTime start, LocalDateTime end) {
        List<Map<String, Object>> result = manager.queryGetData(
                "SELECT * FROM FILM F WHERE F.RELEASE_YEAR BETWEEN TIMESTAMP '" + start.format(toDate) + "' " +
                        "AND TIMESTAMP '" + end.format(toDate) + "';");

        return toListOfFilms(result);
    }

    public List<Film> getReleasedBefore(LocalDateTime date) {
        List<Map<String, Object>> result = manager.queryGetData(
                "SELECT * FROM FILM F WHERE F.RELEASE_YEAR < TIMESTAMP '" + date.format(toDate) + "';"
        );

        return toListOfFilms(result);
    }

    public List<Film> getReleasedAfter(LocalDateTime date) {
        List<Map<String, Object>> result = manager.queryGetData(
                "SELECT * FROM FILM F WHERE F.RELEASE_YEAR > TIMESTAMP '" + date.format(toDate) + "';"
        );

        return toListOfFilms(result);
    }

    public List<Film> getAvailableInLanguage(String language) {
        List<Map<String, Object>> result = manager.queryGetData(
                "SELECT F.* FROM (" +
                        "FILM F JOIN LANGUAGE L ON F.LANGUAGE_ID = L.LANGUAGE_ID" +
                    ") WHERE lower(NAME) = '"
                        + language.toLowerCase() + "';"
        );

        return toListOfFilms(result);
    }

    public List<Film> getWithRentalRateBetween(float min, float max) {
        List<Map<String, Object>> result = manager.queryGetData(
                "SELECT * FROM FILM F WHERE F.RENTAL_RATE BETWEEN " + min + " AND " + max + ";"
        );

        return toListOfFilms(result);
    }

    public List<Film> getWithLowerRentalRateThan(float rate) {
        List<Map<String, Object>> result = manager.queryGetData(
                "SELECT * FROM FILM F WHERE F.RENTAL_RATE < " + rate + ";"
        );

        return toListOfFilms(result);
    }

    public List<Film> getWithGreaterRentalRateThan(float rate) {
        List<Map<String, Object>> result = manager.queryGetData(
                "SELECT * FROM FILM F WHERE F.RENTAL_RATE > " + rate + ";"
        );

        return toListOfFilms(result);
    }

    public List<Film> getWithRentalDurationBetween(int min, int max) {
        List<Map<String, Object>> result = manager.queryGetData(
                "SELECT * FROM FILM F WHERE F.RENTAL_DURATION BETWEEN " + min + " AND " + max + ";"
        );

        return toListOfFilms(result);
    }

    public List<Film> getWithShorterRentalDurationThan(int duration) {
        List<Map<String, Object>> result = manager.queryGetData(
                "SELECT * FROM FILM F WHERE F.RENTAL_DURATION < " + duration + ";"
        );

        return toListOfFilms(result);
    }

    public List<Film> getWithGreaterRentalDurationThan(int duration) {
        List<Map<String, Object>> result = manager.queryGetData(
                "SELECT * FROM FILM F WHERE F.RENTAL_DURATION > " + duration + ";"
        );

        return toListOfFilms(result);
    }

    public List<Film> getWithLengthBetween(int min, int max) {
        List<Map<String, Object>> result = manager.queryGetData(
                "SELECT * FROM FILM F WHERE F.LENGTH BETWEEN " + min + " AND " + max + ";"
        );

        return toListOfFilms(result);
    }

    public List<Film> getShorterThan(int minutes) {
        List<Map<String, Object>> result = manager.queryGetData(
                "SELECT * FROM FILM F WHERE F.LENGTH < " + minutes + ";"
        );

        return toListOfFilms(result);
    }

    public List<Film> getLongerThan(int minutes) {
        List<Map<String, Object>> result = manager.queryGetData(
                "SELECT * FROM FILM F WHERE F.LENGTH > " + minutes + ";"
        );

        return toListOfFilms(result);
    }

    @Override
    public Integer getCount() {
        List<Map<String, Object>> result = manager.queryGetData(
                "SELECT COUNT(*) AS COUNT FROM FILM;"
        );

        return ((Long)result.get(0).get("COUNT")).intValue();
    }

    @Override
    public List<Film> getAll() {
        List<Map<String, Object>> result = manager.queryGetData(
                "SELECT * FROM FILM;"
        );

        return toListOfFilms(result);
    }

    @Override
    public Object getById(int id) {
        List<Map<String, Object>> result = manager.queryGetData(
                "SELECT * FROM FILM F WHERE F.FILM_ID = " + id + ";"
        );

        return toListOfFilms(result).get(0);
    }

    @Override
    public List<Film> getIdBetween(int start, int stop) {
        List<Map<String, Object>> result = manager.queryGetData(
                "SELECT * FROM FILM F WHERE F.FILM_ID BETWEEN " + start + " AND " + stop + ";"
        );

        return toListOfFilms(result);
    }

    @Override
    public int add(Object obj) {
        if(!(obj instanceof Film))
            return 0;

        Film film = (Film) obj;

        String sql = String.format("INSERT INTO FILM VALUES (%d, '%s', '%s', TIMESTAMP '%s', %d, null," +
                        " %d, %s, %d, null, null, null, TIMESTAMP '%s');",
                    film.getId(), film.getTitle(), film.getDescription(), film.getReleaseYear().format(toDate),
                    film.getLanguageId(), film.getRentalDuration(), film.getRentalRate().toPlainString(),
                    film.getLength(), film.getLastUpdate().format(toDateTime));

        int rowsAdded = manager.queryModify(sql, transaction);
        count += rowsAdded;

        return rowsAdded;
    }

    @Override
    public int addAll(List<Object> list) {
        if(list == null || list.size() == 0)
            return 0;

        StringBuilder sql = new StringBuilder("INSERT INTO FILM VALUES ");

        for(Object obj : list) {
            Film film = (Film) obj;

            sql.append(String.format("(%d, '%s', '%s', TIMESTAMP '%s', %d, null," +
                            " %d, %s, %d, null, null, null, TIMESTAMP '%s'), ",
                    film.getId(), film.getTitle(), film.getDescription(), film.getReleaseYear().format(toDate),
                    film.getLanguageId(), film.getRentalDuration(), film.getRentalRate().toPlainString(),
                    film.getLength(), film.getLastUpdate().format(toDateTime)));
        }

        sql.replace(sql.lastIndexOf(", "), sql.length(), ";");

        int rowsAdded = manager.queryModify(sql.toString(), transaction);
        count += rowsAdded;

        return rowsAdded;
    }

    public List<Film> toListOfFilms(List<Map<String, Object>> listOfMaps) {
        if(listOfMaps == null)
            return null;

        List<Film> listOfFilms = new ArrayList<>(listOfMaps.size());

        for(var film : listOfMaps) {
            listOfFilms.add(new Film((int)film.get("FILM_ID"), (String)film.get("TITLE"),
                    (String)film.get("DESCRIPTION"), ((Date)film.get("RELEASE_YEAR")).toLocalDate(),
                    (int)film.get("LANGUAGE_ID"), (int)film.get("RENTAL_DURATION"), (BigDecimal)film.get("RENTAL_RATE"),
                    (int)film.get("LENGTH"), ((Timestamp)film.get("LAST_UPDATE")).toLocalDateTime()));
        }

        return listOfFilms;
    }
}
