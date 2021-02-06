package com.basejava.webapp.storage;

import com.basejava.webapp.model.Resume;

import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public abstract class AbstractArrayStorage implements Storage {
    protected static final int STORAGE_SIZE = 10_000;
    protected Resume[] storage = new Resume[STORAGE_SIZE];
    protected int size = 0;

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    public Resume get(String uuid) {
        int index = getIndex(uuid);
        if (index != -1) {
            return storage[index];
        }
        System.out.printf("Резюме с uuid %s отсутствует в хранилище.\n", uuid);
        return null;
    }

    public void update(Resume resume) {
        int index = getIndex(resume.getUuid());
        if (index == -1) {
            System.out.printf("Резюме с uuid %s отсутствует в хранилище.\n", resume.getUuid());
        } else {
            storage[index] = resume;
        }
    }

    public void save(Resume resume) {
        String uuid = resume.getUuid();
        int index = getIndex(uuid);
        if (index >= 0) {
            System.out.printf("Резюме %s уже существует.\n", uuid);
        } else if (size == STORAGE_SIZE) {
            System.out.printf("Невозможно добавить резюме %s: хранилище заполнено.\n", uuid);
        } else {
            saveIndex(index, resume);
            size++;
        }
    }

    protected abstract void saveIndex(int index, Resume resume);

    public void delete(String uuid) {
        int index = getIndex(uuid);
        if (index < 0) {
            System.out.printf("Резюме с uuid %s отсутствует в хранилище.\n", uuid);
        } else {
            deleteIndex(index, uuid);
            storage[size - 1] = null;
            size--;
        }
    }

    protected abstract void deleteIndex(int index, String uuid);

    protected abstract int getIndex(String uuid);

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    public Resume[] getAll() {
        return Arrays.stream(storage, 0, size).toArray(Resume[]::new);
    }

    public int size() {
        return size;
    }
}
