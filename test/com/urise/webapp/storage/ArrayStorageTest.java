package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

public class ArrayStorageTest extends AbstractArrayStorageTest {
    @Override
    protected Storage initializeStorage() {
        return new ArrayStorage();
    }

    protected Resume[] expectedArray() {
        return new Resume[]{new Resume(UUID3), new Resume(UUID2), new Resume(UUID1)};
    }
}