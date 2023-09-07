package com.urise.webapp.storage;

public class ObjectStreamStorageTest extends AbstractArrayStorageTest {
    protected ObjectStreamStorageTest() {
        super(new ObjectStreamStorage(STORAGE_DIR));
    }

}