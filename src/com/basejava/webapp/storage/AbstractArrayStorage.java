package com.basejava.webapp.storage;

import com.basejava.webapp.exception.StorageException;
import com.basejava.webapp.model.Resume;

import java.util.Arrays;

public abstract class AbstractArrayStorage extends AbstractStorage {
    protected static final int STORAGE_SIZE = 10_000;
    protected Resume[] storage = new Resume[STORAGE_SIZE];
    protected int size = 0;

    @Override
    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    @Override
    protected boolean resumeExists(Object searchKey) {
        int index = (int) searchKey;
        return index >= 0;
    }

    @Override
    protected Resume getResume(Object searchKey) {
        return storage[(int) searchKey];
    }

    @Override
    protected void fillUpdatedResume(Object searchKey, Resume resume) {
        storage[(int) searchKey] = resume;
    }

    @Override
    protected Object getResumeKey(String uuid) {
        return getIndex(uuid);
    }

    @Override
    protected void saveResume(Resume resume, Object searchKey) {
        if (size == STORAGE_SIZE) {
            throw new StorageException("Storage overflow", resume.getUuid());
        }
        insertResume(resume, (int) searchKey);
        size++;
    }

    @Override
    protected void deleteResume(Object searchKey) {
        fillDeletedResume((int) searchKey);
        storage[size - 1] = null;
        size--;
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    @Override
    public Resume[] getAll() {
        return Arrays.stream(storage, 0, size).toArray(Resume[]::new);
    }

    @Override
    public int size() {
        return size;
    }

    protected abstract void insertResume(Resume resume, int index);

    protected abstract void fillDeletedResume(int index);

    protected abstract int getIndex(String uuid);
}
