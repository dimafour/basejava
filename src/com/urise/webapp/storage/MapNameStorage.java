package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MapNameStorage extends AbstractMapStorage {

    @Override
    protected Object getSearchKey(String fullName) {
        return fullName;
    }

    public List<Resume> getAllSorted() {
        List<Resume> storageSorted = new ArrayList<>(storage.values());
        storageSorted.sort(Comparator.comparing(Resume::getFullName).thenComparing(Resume::getFullName));
        return storageSorted;
    }
}
