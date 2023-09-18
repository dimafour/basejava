package com.urise.webapp.storage;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.urise.webapp.model.ResumeTestData.createResume;
import static org.junit.jupiter.api.Assertions.*;

public abstract class AbstractStorageTest {
    protected static final String STORAGE_DIR = "C:\\Users\\dimaf\\IdeaProjects\\basejava\\src\\com\\urise\\webapp\\storage\\Saved";

    protected final Storage storage;
    protected final List<Resume> expectedList = new ArrayList<>();
    protected final Resume[] expectedArrayForMapResumeStorage = new Resume[]{RESUME1, RESUME2, RESUME3};
    static final String UUID1 = "uuid1";
    static final String FULL_NAME1 = "Alex Alexandrov";
    static final String UUID2 = "uuid2";
    static final String FULL_NAME2 = "Ivan Ivanov";
    static final String UUID3 = "uuid3";
    static final String FULL_NAME3 = "Peter Petrov";
    static final String UUID4 = "uuid4";
    static final String FULL_NAME4 = "John Johnson";
    static final String FAILURE_MESSAGE = "Storage overflow before expected";
    static Resume RESUME1;
    static final Resume RESUME2;
    static final Resume RESUME3;
    static final Resume RESUME4;

    static {
        try {
            RESUME1 = createResume(UUID1, FULL_NAME1);
            RESUME2 = createResume(UUID2, FULL_NAME2);
            RESUME3 = createResume(UUID3, FULL_NAME3);
            RESUME4 = createResume(UUID4, FULL_NAME4);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    {
        expectedList.add(RESUME1);
        expectedList.add(RESUME2);
        expectedList.add(RESUME3);
    }

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
        storage.save(RESUME4);
        expectedList.add(RESUME4);
        expectedList.sort(Comparator.comparing(Resume::getFullName).thenComparing(Resume::getUuid));
        assertArrayEquals(expectedList.toArray(), storage.getAllSorted().toArray());
        assertSize(expectedList.size());
        assertGet(RESUME4);
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
        if (storage instanceof MapResumeStorage) {
            assertArrayEquals(expectedArrayForMapResumeStorage, storage.getAllSorted().toArray());
        } else {
            assertArrayEquals(expectedList.toArray(), storage.getAllSorted().toArray());
        }
    }

    @Test
    public void update() {
        Resume r;
        try {
            r = createResume(UUID3, "Peter Petrov");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
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
