package com.basejava.webapp.storage;

import com.basejava.webapp.model.Resume;

import java.util.Arrays;

public class SortedArrayStorage extends AbstractArrayStorage {

    @Override
    public void save(Resume resume) {
        String uuid = resume.getUuid();
        int index = getIndex(uuid);
        if (index >= 0) {
            System.out.printf("Резюме %s уже существует.\n", uuid);
        } else if (size == STORAGE_SIZE) {
            System.out.printf("Невозможно добавить резюме %s: хранилище заполнено.\n", uuid);
        } else {
            int resumePosition = -1 * (index + 1);
            System.arraycopy(storage, resumePosition, storage, resumePosition + 1, size + 1);
            storage[resumePosition] = resume;
            size++;
        }
    }

    @Override
    public void delete(String uuid) {
        int index = getIndex(uuid);
        if (index < 0) {
            System.out.printf("Резюме с uuid %s отсутствует в хранилище.\n", uuid);
        } else {
            storage[index] = null;
            int length = size - (index + 1);
            if (length >= 0) {
                System.arraycopy(storage, index + 1, storage, index, length);
            }
            storage[size - 1] = null;
            size--;
        }
    }

    @Override
    protected int getIndex(String uuid) {
        Resume searchKey = new Resume();
        searchKey.setUuid(uuid);
        return Arrays.binarySearch(storage, 0, size, searchKey);
    }
}
