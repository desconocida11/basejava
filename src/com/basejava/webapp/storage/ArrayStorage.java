package com.basejava.webapp.storage;

import com.basejava.webapp.model.Resume;

import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    private static final int STORAGE_SIZE = 10_000;
    private Resume[] storage = new Resume[STORAGE_SIZE];
    private int size = 0;

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    public void save(Resume r) {
        int index = getResumeIndex(r.toString());
        if (index == -1) {
            if (size == STORAGE_SIZE) {
                System.out.printf("Невозможно добавить резюме %s: хранилище заполнено.\n", r.toString());
            } else {
                storage[size++] = r;
            }
        } else {
            System.out.printf("Резюме %s уже существует.\n", r.toString());
        }
    }

    private int getResumeIndex(String uuid) {
        int index = -1;
        for (int i = 0; i < size; i++) {
            if (uuid.equals(storage[i].toString())) {
                index = i;
                break;
            }
        }
        return index;
    }

    public Resume get(String uuid) {
        Resume resume = null;
        int index = getResumeIndex(uuid);
        if (index != -1) {
            resume = storage[index];
        } else {
            System.out.printf("Резюме с uuid %s отсутствует в хранилище.\n", uuid);
        }
        return resume;
    }

    public void delete(String uuid) {
        int index = getResumeIndex(uuid);
        if (index != -1) {
            storage[index] = storage[size - 1];
            storage[size - 1] = null;
            size--;
        } else {
            System.out.printf("Резюме с uuid %s отсутствует в хранилище.\n", uuid);
        }
    }

    public void update(Resume r) {
        int index = getResumeIndex(r.getUuid());
        if (index != -1) {
            storage[index].setUuid(r.getUuid());
        } else {
            System.out.printf("Резюме с uuid %s отсутствует в хранилище.\n", r.toString());
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
}
