package com.basejava.webapp.storage;

import com.basejava.webapp.exception.ResumeExistsStorageException;
import com.basejava.webapp.exception.ResumeNotExistsStorageException;
import com.basejava.webapp.model.Resume;

public abstract class AbstractStorage implements Storage {

    protected abstract boolean isResumeExist(Object searchKey);

    protected abstract Object getSearchKey(String uuid);

    protected abstract void saveResume(Resume resume, Object searchKey);

    protected abstract void deleteResume(Object searchKey);

    protected abstract Resume getResume(Object searchKey);

    protected abstract void fillUpdatedResume(Object searchKey, Resume resume);

    public void save(Resume resume) {
        String uuid = resume.getUuid();
        Object searchKey = getNotExistedSearchKey(uuid);
        saveResume(resume, searchKey);
    }

    public Resume get(String uuid) {
        Object searchKey = getExistedSearchKey(uuid);
        return getResume(searchKey);
    }

    @Override
    public void delete(String uuid) {
        Object searchKey = getExistedSearchKey(uuid);
        deleteResume(searchKey);
    }

    public void update(Resume resume) {
        String uuid = resume.getUuid();
        Object searchKey = getExistedSearchKey(uuid);
        fillUpdatedResume(searchKey, resume);
    }

    private Object getExistedSearchKey(String uuid) {
        Object searchKey = getSearchKey(uuid);
        if (!isResumeExist(searchKey)) {
            throw new ResumeNotExistsStorageException(uuid);
        }
        return searchKey;
    }

    private Object getNotExistedSearchKey(String uuid) {
        Object searchKey = getSearchKey(uuid);
        if (isResumeExist(searchKey)) {
            throw new ResumeExistsStorageException(uuid);
        }
        return searchKey;
    }
}
