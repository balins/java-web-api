package com.balinski.api_project.database.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Film extends DatabaseModel {
    final int id;
    String title;
    String description;
    LocalDate releaseYear;
    int languageId;
    int rentalDuration;
    BigDecimal rentalRate;
    int length;
    LocalDateTime lastUpdate;

    public Film(int id, String title, String description, LocalDate releaseYear,
                int languageId, int rentalDuration, BigDecimal rentalRate, int length, LocalDateTime lastUpdate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.releaseYear = releaseYear;
        this.languageId = languageId;
        this.rentalDuration = rentalDuration;
        this.rentalRate = rentalRate;
        this.length = length;
        this.lastUpdate = lastUpdate;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getReleaseYear() {
        return releaseYear;
    }

    public int getLanguageId() {
        return languageId;
    }

    public int getRentalDuration() {
        return rentalDuration;
    }

    public BigDecimal getRentalRate() {
        return rentalRate;
    }

    public int getLength() {
        return length;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setReleaseYear(LocalDate releaseYear) {
        this.releaseYear = releaseYear;
    }

    public void setLanguageId(int languageId) {
        this.languageId = languageId;
    }

    public void setRentalDuration(int rentalDuration) {
        this.rentalDuration = rentalDuration;
    }

    public void setRentalRate(BigDecimal rentalRate) {
        this.rentalRate = rentalRate;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    @Override
    public String asJson() {
        return String.format("{\"type\":\"%s\",\"id\":\"%d\",\"attributes\":" +
                        "{\"title\":\"%s\",\"description\":\"%s\",\"releaseYear\":\"%s\",\"rentalDuration\":\"%d\"," +
                        "\"rentalRate\":\"%s\",\"length\":\"%d\",\"lastUpdate\":\"%s\"},\"relationships\":" +
                        "{\"language\":\"data\":{\"type\":\"languages\",\"id\":\"%d\"}}}",
                "films", id, title, description, releaseYear.format(toDate), rentalDuration,
                rentalRate.toPlainString(), length, lastUpdate.format(toDateTime), languageId);
    }

    @Override
    public String asCsv() {
        return String.format("'%s', '%s', TIMESTAMP '%s', %d, %d, %s, %d, TIMESTAMP '%s'",
                title, description, releaseYear.format(toDate),
                languageId, rentalDuration, rentalRate.toPlainString(), length,
                lastUpdate.format(toDateTime));
    }
}
