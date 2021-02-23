package com.basejava.webapp.storage;

import com.basejava.webapp.model.Resume;

import java.util.ArrayList;
import java.util.List;

public class ListStorage extends AbstractStorage {

    private final List<Resume> storage = new ArrayList<>();

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    protected boolean isResumeExist(Object index) {
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
//        return storage.indexOf(new Resume(uuid, fullName));
    }

    @Override
    protected void saveResume(Resume resume, Object searchKey) {
        storage.add(resume);
    }

    @Override
    protected void deleteResume(Object index) {
        storage.remove((int) index);
    }

    @Override
    protected Resume getResume(Object index) {
        return storage.get((int) index);
    }

    @Override
    public List<Resume> getAllSorted() {
        List<Resume> sortedList = storage;
        sortedList.sort(Resume.RESUME_FULL_NAME_COMPARATOR);
        return sortedList;
    }

    @Override
    public int size() {
        return storage.size();
    }

    @Override
    protected void fillUpdatedResume(Object index, Resume resume) {
        storage.set((int) index, resume);
    }
}
