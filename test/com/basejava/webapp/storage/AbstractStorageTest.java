package com.basejava.webapp.storage;

import com.basejava.webapp.Config;
import com.basejava.webapp.exception.ResumeExistsStorageException;
import com.basejava.webapp.exception.ResumeNotExistsStorageException;
import com.basejava.webapp.model.Resume;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static com.basejava.webapp.ResumeTestData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

abstract class AbstractStorageTest {

    protected static final String STORAGE_DIR = Config.getInstance().getStorageDir();

    protected final Storage storage;

    public AbstractStorageTest(Storage storage) {
        this.storage = storage;
    }

    @BeforeEach
    void setUp() {
        storage.clear();
        storage.save(RESUME_1);
        storage.save(RESUME_2);
        storage.save(RESUME_3);
    }

    @Test
    void clear() {
        storage.clear();
        assertSize(0);
    }

    @Test
    void get() {
        assertEquals(RESUME_1, storage.get(UUID_1));
    }

    @Test
    void getNotExists() {
        assertThrows(ResumeNotExistsStorageException.class, () -> storage.get(UUID_4));
    }

    @Test
    void update() {
        Resume resumeToUpdate = createResume(UUID_2, "Ivan Petrov");
        storage.update(resumeToUpdate);
        assertEquals(resumeToUpdate, storage.get(UUID_2));
    }

    @Test
    void updateNoExists() {
        assertThrows(ResumeNotExistsStorageException.class, () -> storage.update(createResume(UUID_4, "Unknown Person")));
    }

    @Test
    void save() {
        Resume newResume = createResume(UUID_4, "Unknown Person");
        storage.save(newResume);
        assertSize(4);
        assertEquals(newResume, storage.get(UUID_4));
    }

    @Test
    void saveAlreadyExists() {
        assertThrows(ResumeExistsStorageException.class, () -> storage.save(RESUME_2));
    }

    @Test
    void delete() {
        storage.delete(UUID_1);
        assertSize(2);
        assertThrows(ResumeNotExistsStorageException.class, () -> storage.get(UUID_1));
    }

    @Test
    void deleteNotExists() {
        assertThrows(ResumeNotExistsStorageException.class, () -> storage.delete(UUID_4));
    }

    @Test
    void getAllSorted() {
        List<Resume> expectedResumes = Arrays.asList(RESUME_2, RESUME_1, RESUME_3);
        List<Resume> actualResumes =  storage.getAllSorted();
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
