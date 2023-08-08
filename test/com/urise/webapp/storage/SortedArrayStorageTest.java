package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

public class SortedArrayStorageTest extends AbstractArrayStorageTest {
    @Override
    protected Storage initializeStorage() {
        return new SortedArrayStorage();
    }

    protected Resume[] expectedArray() {
        return new Resume[]{new Resume(UUID1), new Resume(UUID2), new Resume(UUID3)};
    }

}