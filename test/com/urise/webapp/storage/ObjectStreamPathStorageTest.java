package com.urise.webapp.storage;

public class ObjectStreamPathStorageTest extends AbstractArrayStorageTest {
    protected ObjectStreamPathStorageTest() {
        super(new ObjectStreamPathStorage(STORAGE_DIR.toString()));
    }

}