package com.urise.webapp.storage;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
        AbstractStorageTest.class,
        ArrayStorageTest.class,
        SortedArrayStorageTest.class,
        ListStorageTest.class,
        MapUuidStorageTest.class,
        MapResumeStorageTest.class,
        PathStorageTest.class,
        FileStorageTest.class,
        XmlPathStorageTest.class,
        JsonPathStorageTest.class
})
public class AllStorageTest {

}