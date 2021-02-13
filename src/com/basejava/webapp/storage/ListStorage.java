package com.basejava.webapp.storage;

import com.basejava.webapp.model.Resume;

import java.util.ArrayList;

public class ListStorage extends AbstractStorage {

    protected final ArrayList<Resume> storage = new ArrayList<>();

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    protected void saveResume(Resume resume, String uuid, int index) {
        storage.add(resume);
    }

    @Override
    protected Resume getResumeByIndex(int index) {
        return storage.get(index);
    }

    @Override
    protected void deleteResume(String uuid, int index) {
        storage.remove(index);
    }

    @Override
    public Resume[] getAll() {
        return storage.toArray(new Resume[0]);
    }

    @Override
    public int size() {
        return storage.size();
    }

    @Override
    protected int getIndex(String uuid) {
        return storage.indexOf(new Resume(uuid));
    }

    @Override
    protected void fillUpdatedResume(int index, Resume resume) {
        storage.set(index, resume);
    }
}
