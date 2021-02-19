package com.basejava.webapp.storage;

import com.basejava.webapp.exception.ResumeExistsStorageException;
import com.basejava.webapp.exception.ResumeNotExistsStorageException;
import com.basejava.webapp.model.Resume;

public abstract class AbstractStorage implements Storage {

    protected abstract boolean resumeExists(Object searchKey);

    protected abstract Object getSearchKey(String uuid);

    protected abstract void saveResume(Resume resume, Object searchKey);

    protected abstract void deleteResume(Object searchKey);

    protected abstract Resume getResume(Object searchKey);

    protected abstract void fillUpdatedResume(Object searchKey, Resume resume);

    public void save(Resume resume) {
        String uuid = resume.getUuid();
        Object searchKey = getSearchKey(uuid);
        if (resumeExists(searchKey)) {
            throw new ResumeExistsStorageException(uuid);
        }
        saveResume(resume, searchKey);
    }

    public Resume get(String uuid) {
        Object searchKey = getSearchKey(uuid);
        notExistException(uuid, searchKey);
        return getResume(searchKey);
    }

    @Override
    public void delete(String uuid) {
        Object searchKey = getSearchKey(uuid);
        notExistException(uuid, searchKey);
        deleteResume(searchKey);
    }

    public void update(Resume resume) {
        String uuid = resume.getUuid();
        Object searchKey = getSearchKey(uuid);
        notExistException(uuid, searchKey);
        fillUpdatedResume(searchKey, resume);
    }

    private void notExistException(String uuid, Object searchKey) {
        if (!resumeExists(searchKey)) {
            throw new ResumeNotExistsStorageException(uuid);
        }
    }

}
