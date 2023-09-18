package com.urise.webapp.storage;

import com.urise.webapp.storage.serializer.ObjectSerializer;

public class PathStorageTest extends AbstractArrayStorageTest {
    protected PathStorageTest() {
        super(new PathStorage(STORAGE_DIR, new ObjectSerializer()));
    }

}