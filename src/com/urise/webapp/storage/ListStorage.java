package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class ListStorage extends AbstractStorage {
    private final List<Resume> storage = new ArrayList<>();

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public void doSave(Resume r, Object searchKey) {
        storage.add(r);
    }

    @Override
    public Resume doGet(Object searchKey) {
        return storage.get((int) searchKey);
    }

    @Override
    public void doDelete(Object searchKey) {
        storage.remove((int) searchKey);
    }

    @Override
    protected boolean isExist(Object searchKey) {
        return (int) searchKey >= 0;
    }

    @Override
    public List<Resume> getAllSorted() {
        List<Resume> storageSorted = new ArrayList<>(storage);
        storageSorted.sort(Comparator.comparing(Resume::getUuid));
        return storageSorted;
    }

    @Override
    public int size() {
        return storage.size();
    }

    @Override
    public void doUpdate(Resume r, Object searchKey) {
        storage.set((int) searchKey, r);
    }

    protected Integer getSearchKey(String uuid) {
        for (int i = 0; i < storage.size(); i++) {
            if (storage.get(i).getUuid().equals(uuid)) {
                return i;
            }
        }
        return -1;
    }

}
