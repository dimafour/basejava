package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Arrays;


public abstract class AbstractArrayStorage implements Storage {
    final protected static int STORAGE_LIMIT = 10000;
    final protected static Resume[] storage = new Resume[STORAGE_LIMIT];
    protected int size;

    @Override
    final public int size() {
        return size;
    }

    @Override
    final public void save(Resume r) {
        int index = getIndex(r.getUuid());
        if (size == storage.length) {
            System.out.println("Error: Storage is full%n");
        } else if (index >= 0) {
            System.out.printf("Error: Resume %s is already in the storage%n", r);
        } else {
            pasteNewResume(r, index);
            size++;
        }
    }

    @Override
    final public Resume get(String uuid) {
        int index = getIndex(uuid);
        if (index != -1) {
            return storage[index];
        } else {
            System.out.printf("Error: Resume %s is not found%n", uuid);
            return null;
        }
    }

    @Override
    final public void delete(String uuid) {
        int index = getIndex(uuid);
        if (index >= 0) {
            fillDeletedResume(index);
            storage[size--] = null;
        } else {
            System.out.printf("Error: Resume %s is not found%n", uuid);
        }
    }

    @Override
    final public Resume[] getAll() {
        return Arrays.copyOf(storage, size);
    }

    @Override
    final public void update(Resume r) {
        int index = getIndex(r.getUuid());
        if (index != -1) {
            storage[index] = r;
            System.out.printf("Resume %s is updated%n", r);
        } else {
            System.out.printf("Error: Resume %s is not found%n", r);
        }
    }

    @Override
    final public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    protected abstract int getIndex(String uuid);

    protected abstract void pasteNewResume(Resume r, int index);

    protected abstract void fillDeletedResume(int index);

}
