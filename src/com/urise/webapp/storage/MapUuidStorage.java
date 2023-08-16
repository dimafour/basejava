package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MapUuidStorage extends AbstractMapStorage {
    @Override
    protected Object getSearchKey(String uuid) {
        return uuid;
    }

    public List<Resume> getAllSorted() {
        List<Resume> storageSorted = new ArrayList<>(storage.values());
        storageSorted.sort(Comparator.comparing(Resume::getUuid));
        return storageSorted;
    }
}
