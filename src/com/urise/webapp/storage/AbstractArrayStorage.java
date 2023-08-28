package com.urise.webapp.storage;

import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;

import java.util.*;


public abstract class AbstractArrayStorage extends AbstractStorage<Integer> {
    final static int STORAGE_LIMIT = 10000;
    protected Resume[] storage = new Resume[STORAGE_LIMIT];
    protected int size;

    @Override
    public final int size() {
        return size;
    }

    @Override
    public final void doSave(Resume r, Integer index) {
        if (size == storage.length) {
            throw new StorageException("Storage is full", r.getUuid());
        } else {
            pasteNewResume(r, index);
            size++;
        }
    }

    @Override
    public final Resume doGet(Integer index) {
        return storage[index];
    }

    @Override
    public final void doDelete(Integer index) {
        fillDeletedResume(index);
        storage[--size] = null;
    }

    @Override
    public final List<Resume> doGetAll() {
        List<Resume> list = new ArrayList<>();
        Collections.addAll(list, Arrays.copyOfRange(storage, 0, size));
        return list;
    }

    @Override
    public final void doUpdate(Resume r, Integer index) {
        storage[index] = r;

    }

    @Override
    public final void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    protected boolean isExist(Integer index) {
        return index >= 0;
    }

    protected abstract void pasteNewResume(Resume r, int index);

    protected abstract void fillDeletedResume(int index);

    protected abstract Integer getSearchKey(String uuid);

}
