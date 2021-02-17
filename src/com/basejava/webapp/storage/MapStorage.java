package com.basejava.webapp.storage;

import com.basejava.webapp.model.Resume;

import java.util.HashMap;
import java.util.Map;

public class MapStorage extends AbstractStorage {

    private final Map<String, Resume> storage = new HashMap<>();

    @Override
    protected boolean resumeExists(Object searchKey) {
        return searchKey != null;
    }

    @Override
    protected Object getResumeKey(String uuid) {
        return storage.containsKey(uuid) ? uuid : null;
    }

    @Override
    protected void saveResume(Resume resume, Object searchKey) {
        String uuid;
        if (searchKey == null) {
            uuid = resume.getUuid();
        } else {
            uuid = (String) searchKey;
        }
        storage.put(uuid, resume);
    }

    @Override
    protected void deleteResume(Object searchKey) {
        storage.remove(searchKey);
    }

    @Override
    protected Resume getResume(Object searchKey) {
        return storage.get(searchKey);
    }

    @Override
    protected int getIndex(String uuid) {
        return storage.get(uuid) != null ? 0 : -1;
    }

    @Override
    protected void fillUpdatedResume(Object searchKey, Resume resume) {
        storage.replace((String) searchKey, resume);
    }

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public Resume[] getAll() {
        return storage.values().toArray(new Resume[0]);
    }

    @Override
    public int size() {
        return storage.size();
    }
}
