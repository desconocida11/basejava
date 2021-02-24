package com.basejava.webapp.storage;

import com.basejava.webapp.exception.ResumeExistsStorageException;
import com.basejava.webapp.exception.ResumeNotExistsStorageException;
import com.basejava.webapp.model.Resume;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

abstract class AbstractStorageTest {

    protected final Storage storage;
    protected static final String UUID_1 = "uuid1";
    protected static final String UUID_2 = "uuid2";
    protected static final String UUID_3 = "uuid3";
    protected static final String UUID_4 = "uuid4";

    protected static final String DUMMY = "dummy";

    public AbstractStorageTest(Storage storage) {
        this.storage = storage;
    }

    @BeforeEach
    void setUp() {
        storage.clear();
        storage.save(new Resume(UUID_1, UUID_1 + " name"));
        storage.save(new Resume(UUID_2, UUID_2 + " name"));
        storage.save(new Resume(UUID_3, UUID_3 + " name"));
    }

    @Test
    void clear() {
        storage.clear();
        assertSize(0);
    }

    @Test
    void get() {
        Resume resumeExpected = new Resume(UUID_1, UUID_1 + " name");
        assertEquals(resumeExpected, storage.get(UUID_1));
    }

    @Test
    void getNotExists() {
        assertThrows(ResumeNotExistsStorageException.class, () -> storage.get(DUMMY));
    }

    @Test
    void update() {
        Resume resumeToUpdate = new Resume(UUID_2, UUID_2 + " name");
        storage.update(resumeToUpdate);
        assertEquals(resumeToUpdate, storage.get(UUID_2));
    }

    @Test
    void updateNoExists() {
        assertThrows(ResumeNotExistsStorageException.class, () -> storage.update(new Resume(DUMMY, DUMMY + " name")));
    }

    @Test
    void save() {
        Resume newResume = new Resume(UUID_4, UUID_4 + " name");
        storage.save(newResume);
        assertSize(4);
        assertEquals(newResume, storage.get(UUID_4));
    }

    @Test
    void saveAlreadyExists() {
        assertThrows(ResumeExistsStorageException.class, () -> storage.save(new Resume(UUID_2, UUID_2 + " name")));
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
        List<Resume> expectedResumes = Arrays.asList(new Resume(UUID_1, UUID_1 + " name"),
                new Resume(UUID_2, UUID_2 + " name"), new Resume(UUID_3, UUID_3 + " name"));
        List<Resume> actualResumes = storage.getAllSorted();
        assertEquals(expectedResumes, actualResumes);
    }

    @Test
    void size() {
        assertSize(3);
    }

    private void assertSize(int expectedSize) {
        assertEquals(expectedSize, storage.size());
    }
}
