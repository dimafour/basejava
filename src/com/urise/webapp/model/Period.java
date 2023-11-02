package com.urise.webapp.model;

import com.google.gson.annotations.JsonAdapter;
import com.urise.webapp.util.LocalDateAdapter;
import com.urise.webapp.util.LocalDateJsonAdapter;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;


@XmlAccessorType(XmlAccessType.FIELD)
public class Period implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String description;
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    @JsonAdapter(LocalDateJsonAdapter.class)
    private LocalDate startDate;

    private String title;
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    @JsonAdapter(LocalDateJsonAdapter.class)
    private LocalDate endDate;

    public Period() {
    }

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
        return "\n" + title + " * " +
                description + "\n" +
                startDate + " - " + endDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Period period = (Period) o;
        return Objects.equals(description, period.description) && Objects.equals(startDate, period.startDate) && Objects.equals(title, period.title) && Objects.equals(endDate, period.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, startDate, title, endDate);
    }
}
