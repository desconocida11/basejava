package com.basejava.webapp.storage;

import com.basejava.webapp.exception.ResumeExistsStorageException;
import com.basejava.webapp.exception.ResumeNotExistsStorageException;
import com.basejava.webapp.model.Resume;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static com.basejava.webapp.ResumeTestData.createResume;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

abstract class AbstractStorageTest {

    protected static final String STORAGE_DIR = ".\\storage";

    protected final Storage storage;
    protected static final String UUID_1 = "uuid1";
    protected static final String UUID_2 = "uuid2";
    protected static final String UUID_3 = "uuid3";
    protected static final String UUID_4 = "uuid4";

    public AbstractStorageTest(Storage storage) {
        this.storage = storage;
    }

    @BeforeEach
    void setUp() {
        storage.clear();
        storage.save(createResume(UUID_1, "John Dorian"));
        storage.save(createResume(UUID_2, "Ivan Petrov"));
        storage.save(createResume(UUID_3, "Petr Ivanov"));
    }

    @Test
    void clear() {
        storage.clear();
        assertSize(0);
    }

    @Test
    void get() {
        Resume resumeExpected = createResume(UUID_1, "John Dorian");
        assertEquals(resumeExpected, storage.get(UUID_1));
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
        assertThrows(ResumeExistsStorageException.class, () -> storage.save(createResume(UUID_2, "Ivan Petrov")));
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
        List<Resume> expectedResumes = Arrays.asList(createResume(UUID_2, "Ivan Petrov"),
                createResume(UUID_1, "John Dorian"), createResume(UUID_3, "Petr Ivanov"));
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
