package com.basejava.webapp.storage;

import com.basejava.webapp.model.Resume;

import java.util.ArrayList;
import java.util.List;

public class ListStorage extends AbstractStorage<Integer> {

    private final List<Resume> storage = new ArrayList<>();

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    protected boolean isResumeExist(Integer index) {
        return (int) index >= 0;
    }

    @Override
    protected Integer getSearchKey(String uuid) {
        for (int i = 0; i < storage.size(); i++) {
            if (storage.get(i).getUuid().equals(uuid)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    protected void saveResume(Resume resume, Integer searchKey) {
        storage.add(resume);
    }

    @Override
    protected void deleteResume(Integer index) {
        storage.remove((int) index);
    }

    @Override
    protected Resume getResume(Integer index) {
        return storage.get((int) index);
    }

    @Override
    public List<Resume> getResumeList() {
        return storage;
    }

    @Override
    public int size() {
        return storage.size();
    }

    @Override
    protected void fillUpdatedResume(Integer index, Resume resume) {
        storage.set((int) index, resume);
    }
}
