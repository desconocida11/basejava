package com.basejava.webapp.storage;

import com.basejava.webapp.exception.ResumeExistsStorageException;
import com.basejava.webapp.exception.ResumeNotExistsStorageException;
import com.basejava.webapp.model.Resume;

public abstract class AbstractStorage implements Storage {

    public void save(Resume resume) {
        String uuid = resume.getUuid();
        int index = getIndex(uuid);
        if (index >= 0) {
            throw new ResumeExistsStorageException(uuid);
        }
        saveResume(resume, index);
    }

    protected abstract void saveResume(Resume resume, int index);

    public Resume get(String uuid) {
        int index = getIndex(uuid);
        if (index >= 0) {
            return getResumeByIndex(index);
        }
        throw new ResumeNotExistsStorageException(uuid);
    }

    @Override
    public void delete(String uuid) {
        int index = getIndex(uuid);
        if (index < 0) {
            throw new ResumeNotExistsStorageException(uuid);
        }
        deleteResume(index);
    }

    protected abstract void deleteResume(int index);

    public void update(Resume resume) {
        int index = getIndex(resume.getUuid());
        if (index < 0) {
            throw new ResumeNotExistsStorageException(resume.getUuid());
        }
        fillUpdatedResume(index, resume);
    }

    protected abstract Resume getResumeByIndex(int index);

    protected abstract int getIndex(String uuid);

    protected abstract void fillUpdatedResume(int index, Resume resume);
}
