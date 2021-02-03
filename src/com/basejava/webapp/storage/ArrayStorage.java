package com.basejava.webapp.storage;

import com.basejava.webapp.model.Resume;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage extends AbstractArrayStorage {

    public void save(Resume resume) {
        String uuid = resume.getUuid();
        if (getIndex(uuid) != -1) {
            System.out.printf("Резюме %s уже существует.\n", uuid);
        } else if (size == STORAGE_SIZE) {
            System.out.printf("Невозможно добавить резюме %s: хранилище заполнено.\n", uuid);
        } else {
            storage[size++] = resume;
        }
    }

    public void delete(String uuid) {
        int index = getIndex(uuid);
        if (index == -1) {
            System.out.printf("Резюме с uuid %s отсутствует в хранилище.\n", uuid);
        } else {
            storage[index] = storage[size - 1];
            storage[size - 1] = null;
            size--;
        }
    }

    protected int getIndex(String uuid) {
        for (int i = 0; i < size; i++) {
            if (uuid.equals(storage[i].getUuid())) {
                return i;
            }
        }
        return -1;
    }

}
