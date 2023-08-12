package com.urise.webapp.storage;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.Resume;

import java.util.ArrayList;


public class ListStorage extends AbstractStorage {
    ArrayList<Resume> storage = new ArrayList<>();

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public void save(Resume r) {
        if (!storage.contains(r)) {
            storage.add(r);
        } else throw new ExistStorageException(r.getUuid());
    }

    @Override
    public Resume get(String uuid) {
        return storage.get(getIndex(uuid));
    }

    @Override
    public void delete(String uuid) {
        storage.remove(getIndex(uuid));
    }

    @Override
    public Resume[] getAll() {
        return storage.toArray(new Resume[0]);
    }

    @Override
    public int size() {
        return storage.size();
    }

    @Override
    public void update(Resume r) {
        storage.set(getIndex(r.getUuid()), r);
    }

    protected int getIndex(String uuid) {
        Resume r = new Resume(uuid);
        int index = storage.indexOf(r);
        if (index != -1) {
            return index;
        } else {
            throw new NotExistStorageException(uuid);
        }
    }

}
