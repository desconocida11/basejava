package com.basejava.webapp.storage;

import com.basejava.webapp.exception.ResumeExistsStorageException;
import com.basejava.webapp.exception.ResumeNotExistsStorageException;
import com.basejava.webapp.model.Resume;

public abstract class AbstractStorage implements Storage {

    public void save(Resume resume) {
        String uuid = resume.getUuid();
        Object searchKey = getResumeKey(uuid);
        if (resumeExists(searchKey)) {
            throw new ResumeExistsStorageException(uuid);
        }
        saveResume(resume, searchKey);
    }

    protected abstract boolean resumeExists(Object searchKey);

    protected abstract Object getResumeKey(String uuid);

    protected abstract void saveResume(Resume resume, Object searchKey);

    public Resume get(String uuid) {
        Object searchKey = getResumeKey(uuid);
        if (resumeExists(searchKey)) {
            return getResume(searchKey);
        }
        throw new ResumeNotExistsStorageException(uuid);
    }

    @Override
    public void delete(String uuid) {
        Object searchKey = getResumeKey(uuid);
        if (!resumeExists(searchKey)) {
            throw new ResumeNotExistsStorageException(uuid);
        }
        deleteResume(searchKey);
    }

    protected abstract void deleteResume(Object searchKey);

    public void update(Resume resume) {
        String uuid = resume.getUuid();
        Object searchKey = getResumeKey(uuid);
        if (!resumeExists(searchKey)) {
            throw new ResumeNotExistsStorageException(uuid);
        }
        fillUpdatedResume(searchKey, resume);
    }

    protected abstract Resume getResume(Object searchKey);

    protected abstract int getIndex(String uuid);

    protected abstract void fillUpdatedResume(Object searchKey, Resume resume);
}
