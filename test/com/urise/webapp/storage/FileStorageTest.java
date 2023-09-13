package com.urise.webapp.storage;

public class FileStorageTest extends AbstractArrayStorageTest {
    protected FileStorageTest() {
        super(new FileStorage(STORAGE_DIR));
    }

}