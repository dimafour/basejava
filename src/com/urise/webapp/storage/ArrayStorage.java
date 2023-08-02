package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    static final int STORAGE_LIMIT = 10000;
    private final Resume[] storage = new Resume[STORAGE_LIMIT];
    protected int size;

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
        System.out.println("Storage is cleared");
    }

    public void save(Resume r) {
        if (size == storage.length) {
            System.out.println("Error: Storage is full%n");
        } else if (getIndex(r.getUuid()) >= 0) {
            System.out.printf("Error: Resume %s is already in the storage%n", r);
        } else {
            storage[size++] = r;
        }
    }

    public Resume get(String uuid) {
        int resumeIndex = getIndex(uuid);
        if (resumeIndex != -1) {
            return storage[resumeIndex];
        } else {
            System.out.printf("Error: Resume %s is not found%n", uuid);
            return null;
        }
    }

    public void delete(String uuid) {
        int resumeIndex = getIndex(uuid);
        if (resumeIndex != -1) {
            size--;
            storage[resumeIndex] = storage[size];
            storage[size] = null;
        } else {
            System.out.printf("Error: Resume %s is not found%n", uuid);
        }
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    public Resume[] getAll() {
        return Arrays.copyOf(storage, size);
    }

    public int size() {
        return size;
    }

    public void update(Resume r, String newUuid) {
        int resumeIndex = getIndex(r.getUuid());
        if (resumeIndex != -1) {
            storage[resumeIndex] = new Resume(newUuid);
            System.out.printf("Resume %s is updated to %s.%n", r.getUuid(), newUuid);
        }
    }

    private int getIndex(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].getUuid().equals(uuid)) {
                return i;
            }
        }
        return -1;
    }

}
