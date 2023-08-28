package com.urise.webapp.model;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Company {
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
}
