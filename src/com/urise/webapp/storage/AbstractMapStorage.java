package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.*;

public abstract class AbstractMapStorage<SK> extends AbstractStorage<SK> {
    protected final Map<String, Resume> storage = new HashMap<>();
    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public int size() {
        return storage.size();
    }

}
