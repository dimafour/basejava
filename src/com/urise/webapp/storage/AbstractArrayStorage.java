package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Arrays;


public abstract class AbstractArrayStorage implements Storage {
    protected static final int STORAGE_LIMIT = 10000;
    protected static final Resume[] storage = new Resume[STORAGE_LIMIT];
    protected int size;

    @Override
    public int size() {
        return size;
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

    public Resume[] getAll() {
        return Arrays.copyOf(storage, size);
    }


    public void update(Resume r) {
        int index = getIndex(r.getUuid());
        if (index != -1) {
            storage[index] = r;
            System.out.printf("Resume %s is updated%n", r);
        } else {
            System.out.printf("Error: Resume %s is not found%n", r);
        }
    }

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    protected abstract int getIndex(String uuid);

}
