package com.basejava.webapp.storage;

import com.basejava.webapp.model.Resume;

/**
 * Array based storage interface for Resumes
 */
public interface Storage {
    void clear();

    void save(Resume resume);

    Resume get(String uuid);

    void delete(String uuid);

    void update(Resume resume);

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    Resume[] getAll();

    int size();
}