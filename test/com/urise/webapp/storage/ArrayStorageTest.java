package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

public class ArrayStorageTest extends AbstractArrayStorageTest {

    protected ArrayStorageTest() {
        super(new ArrayStorage());
    }

    protected Resume[] expectedArray() {
        return new Resume[]{RESUME3, RESUME2, RESUME1};
    }
}