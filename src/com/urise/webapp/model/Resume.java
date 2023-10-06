package com.urise.webapp.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

/**
 * Initial resume class
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Resume implements Comparable<Resume>, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    private final String uuid;
    private final String fullName;
    private final Map<ContactType, String> contacts = new EnumMap<>(ContactType.class);
    private final Map<SectionType, Section> sections = new EnumMap<>(SectionType.class);
    protected static String DEFAULT_NAME = "Name is absent";

    public Resume() {
        this.uuid = UUID.randomUUID().toString();
        this.fullName = DEFAULT_NAME;
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

    public Map<ContactType, String> getContacts() {
        return contacts;
    }

    public Map<SectionType, Section> getSections() {
        return sections;
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
        return Objects.equals(uuid, resume.uuid)
                && Objects.equals(fullName, resume.fullName)
                && Objects.equals(contacts, resume.contacts)
                && Objects.equals(sections, resume.sections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, fullName, contacts, sections);
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
            sb.append("\n\n");
        }
        return sb.toString();
    }

}
