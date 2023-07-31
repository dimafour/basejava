package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    Resume[] storage = new Resume[10000];
    int size;

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
        System.out.println("Storage is cleared");
    }

    public void save(Resume r) {
        if (findResumeInStorage(r.getUuid()) == -1) {
            if (isFull()) {
                System.out.println("Error: Storage is full%n");
            } else {
                storage[size++] = r;
            }
        } else {
            System.out.printf("Error: Resume %s is already in the storage%n", r);
        }
    }

    public Resume get(String uuid) {
        int resumeIndex = findResumeInStorage(uuid);
        if (resumeIndex != -1) {
            return storage[resumeIndex];
        } else {
            System.out.printf("Error: Resume %s is not found%n", uuid);
            return null;
        }
    }

    public void delete(String uuid) {
        int resumeIndex = findResumeInStorage(uuid);
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
        int resumeIndex = findResumeInStorage(r.getUuid());
        if (resumeIndex != -1) {
            storage[resumeIndex] = new Resume(newUuid);
            System.out.printf("Resume %s is updated to %s.%n", r.getUuid(), newUuid);
        }
    }

    private int findResumeInStorage(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].getUuid().equals(uuid)) return i;
        }
        return -1;
    }

    private boolean isFull() {
        return size == storage.length;
    }

}
