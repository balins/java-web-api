package com.balinski.api_project.database.dao;

import com.balinski.api_project.database.model.Film;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class FilmDao extends Dao<Film> {
    static final DateTimeFormatter toDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public FilmDao(DaoManager manager, boolean transaction) {
        super(manager, DaoType.FILM, transaction);
    }

    public List<Film> getByTitle(String title) throws DaoException {
        List<Map<String, Object>> result = manager.getData(
                String.format("SELECT * FROM FILM F WHERE lower(F.TITLE) = '%s';", title.toLowerCase())
        );

        return toListOfObjects(result);
    }

    public List<Film> getReleasedBetween(LocalDateTime start, LocalDateTime end) throws DaoException {
        List<Map<String, Object>> result = manager.getData(
                String.format("SELECT * FROM FILM F WHERE F.RELEASE_YEAR BETWEEN TIMESTAMP '%s' AND TIMESTAMP '%s';",
                        start.format(toDate), end.format(toDate))
        );

        return toListOfObjects(result);
    }

    public List<Film> getReleasedBefore(LocalDateTime date) throws DaoException {
        List<Map<String, Object>> result = manager.getData(
                String.format("SELECT * FROM FILM F WHERE F.RELEASE_YEAR < TIMESTAMP '%s';", date.format(toDate))
        );

        return toListOfObjects(result);
    }

    public List<Film> getReleasedAfter(LocalDateTime date) throws DaoException {
        List<Map<String, Object>> result = manager.getData(
                String.format("SELECT * FROM FILM F WHERE F.RELEASE_YEAR > TIMESTAMP '%s';", date.format(toDate))
        );

        return toListOfObjects(result);
    }

    public List<Film> getAvailableInLanguage(String language) throws DaoException {
        List<Map<String, Object>> result = manager.getData(
                String.format("SELECT F.* FROM (FILM F JOIN LANGUAGE L ON F.LANGUAGE_ID = L.LANGUAGE_ID" +
                    ") WHERE lower(NAME) = '%s';", language.toLowerCase())
        );

        return toListOfObjects(result);
    }

    public List<Film> getWithRentalRateBetween(BigDecimal min, BigDecimal max) throws DaoException {
        List<Map<String, Object>> result = manager.getData(
                String.format("SELECT * FROM FILM F WHERE F.RENTAL_RATE BETWEEN %s AND %s;",
                        min.toPlainString(), max.toPlainString())
        );

        return toListOfObjects(result);
    }

    public List<Film> getWithLowerRentalRateThan(BigDecimal rate) throws DaoException {
        List<Map<String, Object>> result = manager.getData(
                String.format("SELECT * FROM FILM F WHERE F.RENTAL_RATE < %s;", rate.toPlainString())
        );

        return toListOfObjects(result);
    }

    public List<Film> getWithGreaterRentalRateThan(BigDecimal rate) throws DaoException {
        List<Map<String, Object>> result = manager.getData(
                String.format("SELECT * FROM FILM F WHERE F.RENTAL_RATE > %s;", rate.toPlainString())
        );

        return toListOfObjects(result);
    }

    public List<Film> getWithRentalDurationBetween(int min, int max) throws DaoException {
        List<Map<String, Object>> result = manager.getData(
                String.format("SELECT * FROM FILM F WHERE F.RENTAL_DURATION BETWEEN %d AND %d;", min, max)
        );

        return toListOfObjects(result);
    }

    public List<Film> getWithShorterRentalDurationThan(int duration) throws DaoException {
        List<Map<String, Object>> result = manager.getData(
                String.format("SELECT * FROM FILM F WHERE F.RENTAL_DURATION < %d;", duration)
        );

        return toListOfObjects(result);
    }

    public List<Film> getWithGreaterRentalDurationThan(int duration) throws DaoException {
        List<Map<String, Object>> result = manager.getData(
                String.format("SELECT * FROM FILM F WHERE F.RENTAL_DURATION > %d;", duration)
        );

        return toListOfObjects(result);
    }

    public List<Film> getWithLengthBetween(int min, int max) throws DaoException {
        List<Map<String, Object>> result = manager.getData(
                String.format("SELECT * FROM FILM F WHERE F.LENGTH BETWEEN %d AND %d;", min, max)
        );

        return toListOfObjects(result);
    }

    public List<Film> getShorterThan(int minutes) throws DaoException {
        List<Map<String, Object>> result = manager.getData(
                String.format("SELECT * FROM FILM F WHERE F.LENGTH < %d;", minutes)
        );

        return toListOfObjects(result);
    }

    public List<Film> getLongerThan(int minutes) throws DaoException {
        List<Map<String, Object>> result = manager.getData(
                String.format("SELECT * FROM FILM F WHERE F.LENGTH > %d;", minutes)
        );

        return toListOfObjects(result);
    }
}