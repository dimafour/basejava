package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

public class ListStorageTest extends AbstractArrayStorageTest {
    protected ListStorageTest() {
        super(new ListStorage());
    }

    @Override
    protected Resume[] expectedArray() {
        return new Resume[]{RESUME3, RESUME2, RESUME1};
    }
}
