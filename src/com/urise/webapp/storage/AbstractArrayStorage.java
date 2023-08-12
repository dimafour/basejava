package com.urise.webapp.storage;

import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;

import java.util.Arrays;


public abstract class AbstractArrayStorage extends AbstractStorage {
    final static int STORAGE_LIMIT = 10000;
    protected Resume[] storage = new Resume[STORAGE_LIMIT];
    protected int size;

    @Override
    public final int size() {
        return size;
    }

    @Override
    public final void doSave(Resume r, Object index) {
        if (size == storage.length) {
            throw new StorageException("Storage is full", r.getUuid());
        } else {
            pasteNewResume(r, (Integer) index);
            size++;
        }
    }

    @Override
    public final Resume doGet(Object index) {
        return storage[(int) index];
    }

    @Override
    public final void doDelete(Object index) {
        fillDeletedResume((int) index);
        storage[--size] = null;
    }

    @Override
    public final Resume[] getAll() {
        return Arrays.copyOf(storage, size);
    }

    @Override
    public final void doUpdate(Resume r, Object index) {
        storage[(int) index] = r;

    }

    @Override
    public final void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    protected boolean isExist(Object index) {
        return (int) index >= 0;
    }

    protected abstract void pasteNewResume(Resume r, int index);

    protected abstract void fillDeletedResume(int index);

    protected abstract Integer getSearchKey(String uuid);

}
