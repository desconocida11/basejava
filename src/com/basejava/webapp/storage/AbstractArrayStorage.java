package com.basejava.webapp.storage;

import com.basejava.webapp.exception.ResumeExistsStorageException;
import com.basejava.webapp.exception.ResumeNotExistsStorageException;
import com.basejava.webapp.exception.StorageException;
import com.basejava.webapp.model.Resume;

import java.util.Arrays;

public abstract class AbstractArrayStorage implements Storage {
    public static final int STORAGE_SIZE = 10_000;
    protected Resume[] storage = new Resume[STORAGE_SIZE];
    protected int size = 0;

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    public Resume get(String uuid) {
        int index = getIndex(uuid);
        if (index >= 0) {
            return storage[index];
        }
        throw new ResumeNotExistsStorageException(uuid);
    }

    public void update(Resume resume) {
        int index = getIndex(resume.getUuid());
        if (index < 0) {
            throw new ResumeNotExistsStorageException(resume.getUuid());
        } else {
            storage[index] = resume;
        }
    }

    public void save(Resume resume) {
        String uuid = resume.getUuid();
        int index = getIndex(uuid);
        if (index >= 0) {
            throw new ResumeExistsStorageException(uuid);
        } else if (size == STORAGE_SIZE) {
            throw new StorageException("Storage overflow", uuid);
        } else {
            insertResume(index, resume);
            size++;
        }
    }

    public void delete(String uuid) {
        int index = getIndex(uuid);
        if (index < 0) {
            throw new ResumeNotExistsStorageException(uuid);
        } else {
            fillDeletedResume(index, uuid);
            storage[size - 1] = null;
            size--;
        }
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    public Resume[] getAll() {
        return Arrays.stream(storage, 0, size).toArray(Resume[]::new);
    }

    public int size() {
        return size;
    }

    protected abstract void insertResume(int index, Resume resume);

    protected abstract void fillDeletedResume(int index, String uuid);

    protected abstract int getIndex(String uuid);
}
