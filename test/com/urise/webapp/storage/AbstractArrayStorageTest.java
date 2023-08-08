package com.urise.webapp.storage;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public abstract class AbstractArrayStorageTest {
    protected Storage testStorage = initializeStorage();
    protected Resume[] expectedArray = expectedArray();

    protected static final String UUID1 = "uuid1";
    protected static final String UUID2 = "uuid2";
    protected static final String UUID3 = "uuid3";

    @BeforeEach
    public void setUp() {
        testStorage.clear();
        testStorage.save(new Resume(UUID3));
        testStorage.save(new Resume(UUID2));
        testStorage.save(new Resume(UUID1));
    }

    @Test
    public void size() {
        Assertions.assertEquals(3, testStorage.size());
    }

    @Test
    public void save() {
        Assertions.assertArrayEquals(expectedArray, testStorage.getAll());
    }

    @Test
    public void saveAlreadyExist() {
        Resume r = new Resume("uuid2");
        Assertions.assertThrows(ExistStorageException.class, () -> testStorage.save(r));
    }

    @Test
    public void saveOverflow() {
        try {
            for (int i = 0; i < 9997; i++) {
                testStorage.save(new Resume());
            }
        } catch (Exception e) {
            Assertions.fail("Storage overflow before expected");
        }
        Assertions.assertThrows(StorageException.class, () -> testStorage.save(new Resume()));
    }

    @Test
    public void get() {
        if (testStorage instanceof ArrayStorage) {
            Assertions.assertEquals(expectedArray[0], testStorage.get("uuid3"));
        } else {
            Assertions.assertEquals(expectedArray[2], testStorage.get("uuid3"));
        }
    }

    @Test
    public void getNotExist() {
        Assertions.assertThrows(NotExistStorageException.class, () -> testStorage.get("dummy"));
    }

    @Test
    public void delete() {
        testStorage.delete("uuid3");
        Assertions.assertThrows(NotExistStorageException.class, () -> testStorage.get("uuid3"));
    }

    @Test
    public void deleteNotExist() {
        Assertions.assertThrows(NotExistStorageException.class, () -> testStorage.delete("dummy"));
    }

    @Test
    public void getAll() {
        Assertions.assertArrayEquals(expectedArray, testStorage.getAll());
    }

    @Test
    public void update() {
        Resume r = testStorage.get("uuid3");
        testStorage.update(r);
        Assertions.assertEquals(r, testStorage.get("uuid3"));
    }

    @Test
    public void updateNotExist() {
        Assertions.assertThrows(NotExistStorageException.class, () -> {
            Resume r = testStorage.get("dummy");
            testStorage.update(r);
        });
    }

    @Test
    public void clear() {
        testStorage.clear();
        Assertions.assertEquals(0, testStorage.getAll().length);
    }

    protected abstract Storage initializeStorage();

    protected abstract Resume[] expectedArray();

}