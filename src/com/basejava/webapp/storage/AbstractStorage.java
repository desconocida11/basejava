package com.basejava.webapp.storage;

import com.basejava.webapp.exception.ResumeExistsStorageException;
import com.basejava.webapp.exception.ResumeNotExistsStorageException;
import com.basejava.webapp.model.Resume;

import java.util.Comparator;
import java.util.List;

public abstract class AbstractStorage<SK> implements Storage {

    protected static final Comparator<Resume> RESUME_FULL_NAME_COMPARATOR = Comparator.comparing(Resume::getFullName).thenComparing(Resume::getUuid);

    protected abstract boolean isResumeExist(SK searchKey);

    protected abstract SK getSearchKey(String uuid);

    protected abstract void saveResume(Resume resume, SK searchKey);

    protected abstract void deleteResume(SK searchKey);

    protected abstract Resume getResume(SK searchKey);

    protected abstract void fillUpdatedResume(SK searchKey, Resume resume);

    protected abstract List<Resume> getResumeList();

    public void save(Resume resume) {
        String uuid = resume.getUuid();
        SK searchKey = getNotExistedSearchKey(uuid);
        saveResume(resume, searchKey);
    }

    public Resume get(String uuid) {
        SK searchKey = getExistedSearchKey(uuid);
        return getResume(searchKey);
    }

    @Override
    public void delete(String uuid) {
        SK searchKey = getExistedSearchKey(uuid);
        deleteResume(searchKey);
    }

    public void update(Resume resume) {
        String uuid = resume.getUuid();
        SK searchKey = getExistedSearchKey(uuid);
        fillUpdatedResume(searchKey, resume);
    }

    private SK getExistedSearchKey(String uuid) {
        SK searchKey = getSearchKey(uuid);
        if (!isResumeExist(searchKey)) {
            throw new ResumeNotExistsStorageException(uuid);
        }
        return searchKey;
    }

    private SK getNotExistedSearchKey(String uuid) {
        SK searchKey = getSearchKey(uuid);
        if (isResumeExist(searchKey)) {
            throw new ResumeExistsStorageException(uuid);
        }
        return searchKey;
    }

    public List<Resume> getAllSorted() {
        List<Resume> resumeList = getResumeList();
        resumeList.sort(RESUME_FULL_NAME_COMPARATOR);
        return resumeList;
    }
}