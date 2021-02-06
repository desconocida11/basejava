package com.basejava.webapp.storage;

import com.basejava.webapp.model.Resume;

import java.util.Arrays;

public class SortedArrayStorage extends AbstractArrayStorage {

    @Override
    protected void saveIndex(int index, Resume resume) {
        int resumePosition = -1 * (index + 1);
        System.arraycopy(storage, resumePosition, storage, resumePosition + 1, size + 1);
        storage[resumePosition] = resume;
    }

    @Override
    protected void deleteIndex(int index, String uuid) {
        storage[index] = null;
        int length = size - (index + 1);
        if (length > 0) {
            System.arraycopy(storage, index + 1, storage, index, length);
        }
    }

    @Override
    protected int getIndex(String uuid) {
        Resume searchKey = new Resume();
        searchKey.setUuid(uuid);
        return Arrays.binarySearch(storage, 0, size, searchKey);
    }
}
