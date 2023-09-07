package com.urise.webapp.model;

import java.io.Serial;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Company implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final String name;
    private final URL url;
    private final List<Period> periods;

    public Company(String name, URL url, Period... periods) {
        this.name = name;
        this.url = url;
        this.periods = new ArrayList<>();
        Collections.addAll(this.periods, periods);
    }

    public String getName() {
        return name;
    }

    public URL getUrl() {
        return url;
    }

    public List<Period> getPeriods() {
        return periods;
    }

    @Override
    public String toString() {
        return "\n" + name + "\n" +
                url + "\n" +
                periods;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Company company = (Company) o;
        return Objects.equals(name, company.name) && Objects.equals(url, company.url) && Objects.equals(periods, company.periods);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, url, periods);
    }
}
