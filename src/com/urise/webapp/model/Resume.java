package com.urise.webapp.model;

/**
 * Initial resume class
 */
public class Resume {

    private String uuid;

    public Resume() {
    }

    public Resume(String uuid) {
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    // Unique identifier

    @Override
    public String toString() {
        return uuid;
    }
}
