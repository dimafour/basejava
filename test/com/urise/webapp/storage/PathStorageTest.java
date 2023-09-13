package com.urise.webapp.storage;

public class PathStorageTest extends AbstractArrayStorageTest {
    protected PathStorageTest() {
        super(new PathStorage(STORAGE_DIR));
    }

}