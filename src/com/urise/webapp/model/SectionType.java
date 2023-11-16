package com.urise.webapp.model;

import java.util.List;

public enum SectionType {
    PERSONAL("Личные качества"),
    OBJECTIVE("Позиция"),
    ACHIEVEMENT("Достижения"),
    QUALIFICATIONS("Квалификация"),
    EXPERIENCE("Опыт работы"),
    EDUCATION("Образование");

    private final String title;

    SectionType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public static List<SectionType> companyValues() {
        return List.of(EXPERIENCE, EDUCATION);
    }

    public static List<SectionType> textListValues() {
        return List.of(PERSONAL, OBJECTIVE, ACHIEVEMENT, QUALIFICATIONS);
    }

    @Override
    public String toString() {
        return title;
    }
}
