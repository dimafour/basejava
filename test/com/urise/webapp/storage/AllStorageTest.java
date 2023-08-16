package com.urise.webapp.storage;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.Extension;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.runner.RunWith;

@ExtendWith(Extension.class)
@RunWith(org.junit.runners.Suite.class)
@Suite
@SelectClasses({
                ArrayStorageTest.class,
                SortedArrayStorageTest.class,
                ListStorageTest.class,
                MapUuidStorageTest.class,
                MapNameStorageTest.class
        })
public class AllStorageTest {

}