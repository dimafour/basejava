package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

public class MapStorageTest extends AbstractStorageTest {


    protected MapStorageTest() {
        super(new MapStorage());
    }

    @Override
    protected Resume[] expectedArray() {
        return new Resume[]{RESUME2, RESUME1, RESUME3};
    }

}
