package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

public class ArrayStorage extends AbstractArrayStorage {

    @Override
    protected Integer getSearchKey(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].getUuid().equals(uuid)) {
                return i;
            }
        }
        return -1;
    }
    @Override
    protected void pasteNewResume(Resume r, int index) {
        storage[size] = r;

    }
    @Override
    protected void fillDeletedResume(int index) {
        storage[index] = storage[size - 1];
    }

}
