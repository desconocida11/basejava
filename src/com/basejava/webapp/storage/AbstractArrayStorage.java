package com.basejava.webapp.storage;

import com.basejava.webapp.exception.StorageException;
import com.basejava.webapp.model.Resume;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractArrayStorage extends AbstractStorage<Integer> {
    protected static final int STORAGE_SIZE = 10_000;
    protected Resume[] storage = new Resume[STORAGE_SIZE];
    protected int size = 0;

    @Override
    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    @Override
    protected boolean isResumeExist(Integer index) {
        return (int) index >= 0;
    }

    @Override
    protected Resume getResume(Integer index) {
        return storage[(int) index];
    }

    @Override
    protected void fillUpdatedResume(Integer index, Resume resume) {
        storage[(int) index] = resume;
    }

    @Override
    protected void saveResume(Resume resume, Integer index) {
        if (size == STORAGE_SIZE) {
            throw new StorageException("Storage overflow", resume.getUuid());
        }
        insertResume(resume, (int) index);
        size++;
    }

    @Override
    protected void deleteResume(Integer index) {
        fillDeletedResume((int) index);
        storage[size - 1] = null;
        size--;
    }

    @Override
    public List<Resume> getResumeList() {
        return Arrays.stream(storage, 0, size).collect(Collectors.toList());
    }

    @Override
    public int size() {
        return size;
    }

    protected abstract void insertResume(Resume resume, int index);

    protected abstract void fillDeletedResume(int index);
}
