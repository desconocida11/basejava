package com.basejava.webapp.storage;

import com.basejava.webapp.model.Resume;

import java.util.HashMap;
import java.util.Map;

public class MapStorage extends AbstractStorage {

    private final Map<String, Resume> storage = new HashMap<>();

    @Override
    protected void saveResume(Resume resume, int index) {
        storage.put(resume.getUuid(), resume);
    }

    @Override
    protected void deleteResume(String uuid, int index) {
        storage.remove(uuid);
    }

    @Override
    protected Resume getResume(String uuid, int index) {
        return storage.get(uuid);
    }

    @Override
    protected int getIndex(String uuid) {
        return storage.get(uuid) != null ? 0 : -1;
    }

    @Override
    protected void fillUpdatedResume(int index, Resume resume) {
        String uuid = resume.getUuid();
        storage.replace(uuid, resume);
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
