package com.urise.webapp.model;

import java.util.*;

/**
 * Initial resume class
 */
public class Resume implements Comparable<Resume> {

    private final String uuid;
    private final String fullName;
    private final Map<ContactType, String> contacts = new EnumMap<>(ContactType.class);
    private final Map<SectionType, Section> sections = new EnumMap<>(SectionType.class);
    protected static final String DEFAULT_NAME = "Name is absent";

    public Resume() {
        this(UUID.randomUUID().toString(), DEFAULT_NAME);
    }

    public Resume(String fullName) {
        this.uuid = UUID.randomUUID().toString();
        this.fullName = fullName;
    }

    public Resume(String uuid, String fullName) {
        this.uuid = uuid;
        this.fullName = fullName;
    }

    public String getUuid() {
        return this.uuid;
    }

    public String getFullName() {
        return fullName;
    }

    @Override
    public String toString() {
        return uuid + ": " + fullName + "\n";
    }


    @Override
    public int compareTo(Resume r) {
        return uuid.compareTo(r.uuid);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Resume resume = (Resume) o;

        return uuid.equals(resume.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, fullName);
    }

    public void addContact(ContactType type, String profile) {
        contacts.put(type, profile);
    }

    public void addSectionContent(SectionType type, Section section) {
        sections.put(type, section);
    }

    public String allInformation() {
        StringBuilder sb = new StringBuilder(this.toString());
        for (ContactType type : contacts.keySet()) {
            sb.append(type);
            sb.append(": ");
            sb.append(contacts.get(type));
            sb.append("\n");
        }
        for (SectionType type : sections.keySet()) {
            sb.append(type);
            sb.append(": ");
            sb.append(sections.get(type));
            sb.append("\n");
        }
        return sb.toString();
    }

}
