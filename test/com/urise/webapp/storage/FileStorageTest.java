package com.urise.webapp.storage;

import com.urise.webapp.storage.serializer.ObjectSerializer;

public class FileStorageTest extends AbstractArrayStorageTest {
    protected FileStorageTest() {
        super(new FileStorage(STORAGE_DIR, new ObjectSerializer()));
    }

}