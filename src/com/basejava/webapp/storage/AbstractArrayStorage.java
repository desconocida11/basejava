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
    protected boolean resumeExists(Object index) {
        return (int) index >= 0;
    }

    @Override
    protected Resume getResume(Object index) {
        return storage[(int) index];
    }

    @Override
    protected void fillUpdatedResume(Object index, Resume resume) {
        storage[(int) index] = resume;
    }

    @Override
    protected void saveResume(Resume resume, Object index) {
        if (size == STORAGE_SIZE) {
            throw new StorageException("Storage overflow", resume.getUuid());
        }
        insertResume(resume, (int) index);
        size++;
    }

    @Override
    protected void deleteResume(Object index) {
        fillDeletedResume((int) index);
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
}
