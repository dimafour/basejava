package com.urise.webapp.storage;

import com.urise.webapp.Config;

public class SqlStorageTest extends AbstractStorageTest {
    protected SqlStorageTest() {
        super(new SqlStorage(Config.getInstance().getDbURL(),
                Config.getInstance().getDbUser(),
                Config.getInstance().getDbPassword()));
    }
}
