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
    protected Resume getResumeByIndex(int index) {
        return storage[index];
    }

    @Override
    protected void fillUpdatedResume(int index, Resume resume) {
        storage[index] = resume;
    }

    @Override
    protected void saveResume(Resume resume, int index) {
        if (size == STORAGE_SIZE) {
            throw new StorageException("Storage overflow", resume.getUuid());
        }
        insertResume(index, resume);
        size++;
    }

    @Override
    protected void deleteResume(int index) {
        fillDeletedResume(index, storage[index].getUuid());
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

    protected abstract void insertResume(int index, Resume resume);

    protected abstract void fillDeletedResume(int index, String uuid);

    protected abstract int getIndex(String uuid);
}
