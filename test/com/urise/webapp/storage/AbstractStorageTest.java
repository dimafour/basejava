package com.urise.webapp.storage;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.Extension;

import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class AbstractStorageTest {
    protected final Storage storage;
    protected final List<Resume> expectedList = new ArrayList<>();

    {
        expectedList.add(RESUME1);
        expectedList.add(RESUME2);
        expectedList.add(RESUME3);

    }

    protected final Resume[] expectedArrayForMapNameStorage = new Resume[]{RESUME3, RESUME2, RESUME1};
    static final String UUID1 = "uuid1";
    static final String FULL_NAME1 = "Peter Petrov";
    static final Resume RESUME1 = new Resume(UUID1, FULL_NAME1);
    static final String UUID2 = "uuid2";
    static final String FULL_NAME2 = "Ivan Ivanov";
    static final Resume RESUME2 = new Resume(UUID2, FULL_NAME2);
    static final String UUID3 = "uuid3";
    static final String FULL_NAME3 = "Alex Alexandrov";
    static final Resume RESUME3 = new Resume(UUID3, FULL_NAME3);
    static final String UUID4 = "uuid4";
    static final String FULL_NAME4 = "John Johnson";
    static final Resume RESUME4 = new Resume(UUID4, FULL_NAME4);
    static final String FAILURE_MESSAGE = "Storage overflow before expected";

    protected AbstractStorageTest(Storage storage) {
        this.storage = storage;
    }

    @BeforeEach
    public void setUp() {
        storage.clear();
        storage.save(RESUME3);
        storage.save(RESUME2);
        storage.save(RESUME1);
    }

    @Test
    public void size() {
        assertSize(expectedList.size());
    }

    @Test
    public void save() {
        if (storage instanceof MapNameStorage) {
            assertArrayEquals(expectedArrayForMapNameStorage, storage.getAllSorted().toArray());
            storage.save(RESUME4);
            assertSize(expectedArrayForMapNameStorage.length + 1);
            assertGet(RESUME4);
        } else {
            assertArrayEquals(expectedList.toArray(), storage.getAllSorted().toArray());
            storage.save(RESUME4);
            assertSize(expectedList.size() + 1);
            assertGet(RESUME4);
        }
    }

    @Test
    public void saveAlreadyExist() {
        assertThrows(ExistStorageException.class, () -> storage.save(RESUME1));
    }

    @Test
    public void saveOverflow() {
        if (storage instanceof AbstractArrayStorage) {
            storage.clear();
            try {
                for (int i = 0; i < AbstractArrayStorage.STORAGE_LIMIT; i++) {
                    storage.save(new Resume());
                }
            } catch (Exception e) {
                fail(FAILURE_MESSAGE);
            }
            assertThrows(StorageException.class, () -> storage.save(RESUME4));
        } else {
            assertTrue(true);
        }
    }

    @Test
    public void get() {
        for (Resume r : expectedList) {
            assertGet(r);
        }
    }

    @Test
    public void getNotExist() {
        assertThrows(NotExistStorageException.class, () -> storage.get(RESUME4.getUuid()));
    }

    @Test
    public void delete() {
        storage.delete(UUID3);
        assertSize(expectedList.size() - 1);
        assertThrows(NotExistStorageException.class, () -> storage.get(UUID3));
    }

    @Test
    public void deleteNotExist() {
        assertThrows(NotExistStorageException.class, () -> storage.delete(UUID4));
    }

    @Test
    public void getAllSorted() {
        if (storage instanceof MapNameStorage) {
            assertArrayEquals(expectedArrayForMapNameStorage, storage.getAllSorted().toArray());
        } else {
            assertArrayEquals(expectedList.toArray(), storage.getAllSorted().toArray());
        }
    }

    @Test
    public void update() {
        Resume r = new Resume(UUID3, "Alex Alexandrov");
        storage.update(r);
        assertEquals(r, storage.get(UUID3));
    }

    @Test
    public void updateNotExist() {
        assertThrows(NotExistStorageException.class, () -> storage.update(storage.get(UUID4)));
    }

    @Test
    public void clear() {
        storage.clear();
        assertSize(0);
        assertArrayEquals(new ArrayList<>().toArray(), storage.getAllSorted().toArray());
    }

    private void assertSize(int size) {
        assertEquals(size, storage.size());
    }

    private void assertGet(Resume r) {
        assertEquals(r, storage.get(r.getUuid()));
    }

}
