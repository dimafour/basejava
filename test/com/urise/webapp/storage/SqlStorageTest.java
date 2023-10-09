package com.urise.webapp.storage;

import com.urise.webapp.Config;

public class SqlStorageTest extends AbstractStorageTest {
    protected SqlStorageTest() {
        super(Config.getInstance().getStorage());
    }
}
