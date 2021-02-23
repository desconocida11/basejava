package com.basejava.webapp.storage;

import com.basejava.webapp.model.Resume;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MapStorage extends AbstractStorage {

    private final Map<String, Resume> storage = new HashMap<>();

    @Override
    protected boolean isResumeExist(Object searchKey) {
        return searchKey != null;
    }

    @Override
    protected String getSearchKey(String uuid) {
        return storage.containsKey(uuid) ? uuid : null;
    }

    @Override
    protected void saveResume(Resume resume, Object searchKey) {
        String uuid = resume.getUuid();
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
    protected void fillUpdatedResume(Object searchKey, Resume resume) {
        storage.replace((String) searchKey, resume);
    }

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public List<Resume> getAllSorted() {
        return storage.values().stream().sorted(Resume.RESUME_FULL_NAME_COMPARATOR).collect(Collectors.toList());
    }

    @Override
    public int size() {
        return storage.size();
    }
}
