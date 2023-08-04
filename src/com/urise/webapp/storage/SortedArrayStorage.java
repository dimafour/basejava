package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Arrays;

public class SortedArrayStorage extends AbstractArrayStorage {

    private void sort(Resume[] r) {
        for (int i = 1; i < size; i++) {
            Resume resume = r[i];
            int j = Arrays.binarySearch(r, 0, i, resume);
            if (j < 0) {
                j = -(j + 1);
            }
            System.arraycopy(r, j, r, j + 1, i - j);
            r[j] = resume;
        }
    }

    @Override
    protected int getIndex(String uuid) {
        Resume searchKey = new Resume();
        searchKey.setUuid(uuid);
        return Arrays.binarySearch(storage, 0, size, searchKey);
    }

    @Override
    public void delete(String uuid) {
        super.delete(uuid);
        sort(storage);
    }

    @Override
    public void save(Resume r) {
        super.save(r);
        sort(storage);
    }

    @Override
    public void update(Resume r) {
        super.update(r);
        sort(storage);
    }
}
