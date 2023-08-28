package com.urise.webapp.model;

import java.time.LocalDate;


public class Period {
    private final String description;
    private final LocalDate startDate;

    private final String title;
    private final LocalDate endDate;

    public Period(LocalDate startDate, LocalDate endDate, String title, String description) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return title + " * " +
                description + "\n" +
                startDate + " - " + endDate;
    }
}
