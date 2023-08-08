package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Arrays;

public class SortedArrayStorage extends AbstractArrayStorage {
    public SortedArrayStorage() {
        super();
    }

    @Override
    protected int getIndex(String uuid) {
        Resume searchKey = new Resume(uuid);
        return Arrays.binarySearch(storage, 0, size, searchKey);
    }

    @Override
    protected void pasteNewResume(Resume r, int index) {
        int newIndex = -(index + 1);
        System.arraycopy(storage, newIndex, storage, newIndex + 1, size - newIndex);
        storage[newIndex] = r;
    }

    @Override
    protected void fillDeletedResume(int index) {
        int moveCount = size - (index + 1);
        System.arraycopy(storage, index + 1, storage, index, moveCount);
    }


}
