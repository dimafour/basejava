package com.urise.webapp.storage;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class AbstractStorageTest {
    protected final Storage storage;
    protected final Resume[] expectedArray = expectedArray();
    static final String UUID1 = "uuid1";
    static final Resume RESUME1 = new Resume(UUID1);
    static final String UUID2 = "uuid2";
    static final Resume RESUME2 = new Resume(UUID2);
    static final String UUID3 = "uuid3";
    static final Resume RESUME3 = new Resume(UUID3);
    static final String UUID4 = "uuid4";
    static final Resume RESUME4 = new Resume(UUID4);
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
        assertSize(expectedArray.length);
    }

    @Test
    public void save() {
        assertArrayEquals(expectedArray, storage.getAll());
        storage.save(RESUME4);
        assertSize(expectedArray.length + 1);
        assertGet(RESUME4);
    }

    @Test
    public void saveAlreadyExist() {
        assertThrows(ExistStorageException.class, () -> storage.save(RESUME1));
    }

    @Test
    public void saveOverflow() {
        if (storage instanceof ListStorage || storage instanceof MapStorage) {
            assertTrue(true);
        } else {
            storage.clear();
            try {
                for (int i = 0; i < AbstractArrayStorage.STORAGE_LIMIT; i++) {
                    storage.save(new Resume());
                }
            } catch (Exception e) {
                fail(FAILURE_MESSAGE);
            }
            assertThrows(StorageException.class, () -> storage.save(RESUME4));
        }
    }

    @Test
    public void get() {
        for (Resume r : expectedArray) {
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
        assertSize(expectedArray.length - 1);
        assertThrows(NotExistStorageException.class, () -> storage.get(UUID3));
    }

    @Test
    public void deleteNotExist() {
        assertThrows(NotExistStorageException.class, () -> storage.delete(UUID4));
    }

    @Test
    public void getAll() {
        assertArrayEquals(expectedArray, storage.getAll());
    }

    @Test
    public void update() {
        Resume r = new Resume(UUID3);
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
        assertArrayEquals(new Resume[0], storage.getAll());
    }

    private void assertSize(int size) {
        assertEquals(size, storage.size());
    }

    private void assertGet(Resume r) {
        assertEquals(r, storage.get(r.getUuid()));
    }

    protected abstract Resume[] expectedArray();
}
