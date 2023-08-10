package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

public class SortedArrayStorageTest extends AbstractArrayStorageTest {

    protected SortedArrayStorageTest() {
        super(new SortedArrayStorage());
    }

    protected Resume[] expectedArray() {
        return new Resume[]{RESUME1, RESUME2, RESUME3};
    }

}