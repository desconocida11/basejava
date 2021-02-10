package com.basejava.webapp.storage;

import com.basejava.webapp.model.Resume;

import java.util.Arrays;

public class SortedArrayStorage extends AbstractArrayStorage {

    @Override
    protected void insertResume(int index, Resume resume) {
        int resumePosition = -index - 1;
        System.arraycopy(storage, resumePosition, storage, resumePosition + 1, size - resumePosition);
        storage[resumePosition] = resume;
    }

    @Override
    protected void fillDeletedResume(int index, String uuid) {
        int length = size - index - 1;
        if (length > 0) {
            System.arraycopy(storage, index + 1, storage, index, length);
        }
    }

    @Override
    protected int getIndex(String uuid) {
        Resume searchKey = new Resume(uuid);
        return Arrays.binarySearch(storage, 0, size, searchKey);
    }
}
