package com.basejava.webapp.storage;

import com.basejava.webapp.exception.ResumeExistsStorageException;
import com.basejava.webapp.exception.ResumeNotExistsStorageException;
import com.basejava.webapp.model.Resume;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

abstract class AbstractStorageTest {

    protected final Storage storage;
    protected static final String UUID_1 = "uuid1";
    protected static final String UUID_2 = "uuid2";
    protected static final String UUID_3 = "uuid3";
    protected static final String UUID_4 = "uuid4";

    protected static final String UUID_1_NAME = "Clara";
    protected static final String UUID_2_NAME = "Boris";
    protected static final String UUID_3_NAME = "Anna";
    protected static final String UUID_4_NAME = "Petr";

    protected static final String DUMMY = "dummy";
    protected static final String DUMMY_NAME = "dummy name";

    public AbstractStorageTest(Storage storage) {
        this.storage = storage;
    }

    @BeforeEach
    void setUp() {
        storage.clear();
        storage.save(new Resume(UUID_1, UUID_1_NAME));
        storage.save(new Resume(UUID_2, UUID_2_NAME));
        storage.save(new Resume(UUID_3, UUID_3_NAME));
    }

    @Test
    void clear() {
        storage.clear();
        assertSize(0);
    }

    @Test
    void get() {
        Resume resumeExpected = new Resume(UUID_1, UUID_1_NAME);
        assertEquals(resumeExpected, storage.get(UUID_1));
    }

    @Test
    void getNotExists() {
        assertThrows(ResumeNotExistsStorageException.class, () -> storage.get(DUMMY));
    }

    @Test
    void update() {
        Resume resumeToUpdate = new Resume(UUID_2, UUID_2_NAME);
        storage.update(resumeToUpdate);
        assertEquals(resumeToUpdate, storage.get(UUID_2));
    }

    @Test
    void updateNoExists() {
        assertThrows(ResumeNotExistsStorageException.class, () -> storage.update(new Resume(DUMMY, DUMMY_NAME)));
    }

    @Test
    void save() {
        Resume newResume = new Resume(UUID_4, UUID_4_NAME);
        storage.save(newResume);
        assertSize(4);
        assertEquals(newResume, storage.get(UUID_4));
    }

    @Test
    void saveAlreadyExists() {
        assertThrows(ResumeExistsStorageException.class, () -> storage.save(new Resume(UUID_2, UUID_2_NAME)));
    }

    @Test
    void delete() {
        storage.delete(UUID_1);
        assertSize(2);
        assertThrows(ResumeNotExistsStorageException.class, () -> storage.get(UUID_1));
    }

    @Test
    void deleteNotExists() {
        assertThrows(ResumeNotExistsStorageException.class, () -> storage.delete(DUMMY));
    }

    @Test
    void getAllSorted() {
        List<Resume> expectedResumes = new ArrayList<>();
        Resume resume3 = new Resume(UUID_3, UUID_3_NAME);
        expectedResumes.add(resume3);
        Resume resume2 = new Resume(UUID_2, UUID_2_NAME);
        expectedResumes.add(resume2);
        Resume resume1 = new Resume(UUID_1, UUID_1_NAME);
        expectedResumes.add(resume1);
        List<Resume> getResumes = storage.getAllSorted();
        assertEquals(expectedResumes, getResumes);
    }

    @Test
    void size() {
        assertSize(3);
    }

    private void assertSize(int expectedSize) {
        assertEquals(expectedSize, storage.size());
    }
}
