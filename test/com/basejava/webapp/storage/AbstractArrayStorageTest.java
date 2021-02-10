package com.basejava.webapp.storage;

import com.basejava.webapp.exception.ResumeExistsStorageException;
import com.basejava.webapp.exception.ResumeNotExistsStorageException;
import com.basejava.webapp.exception.StorageException;
import com.basejava.webapp.model.Resume;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.basejava.webapp.storage.AbstractArrayStorage.STORAGE_SIZE;
import static org.junit.jupiter.api.Assertions.*;

abstract class AbstractArrayStorageTest {

    private final Storage storage;
    protected static final String UUID_1 = "uuid1";
    protected static final String UUID_2 = "uuid2";
    protected static final String UUID_3 = "uuid3";
    protected static final String UUID_4 = "uuid4";
    protected static final String DUMMY = "dummy";

    public AbstractArrayStorageTest(Storage storage) {
        this.storage = storage;
    }

    @BeforeEach
    void setUp() {
        storage.clear();
        storage.save(new Resume(UUID_1));
        storage.save(new Resume(UUID_2));
        storage.save(new Resume(UUID_3));
    }

    @Test
    void clear() {
        storage.clear();
        assertEquals(0, storage.size());
    }

    @Test
    void get() {
        Resume resumeExpected = new Resume(UUID_1);
        assertEquals(resumeExpected, storage.get(UUID_1));
    }

    @Test
    void getNotExists() {
        assertThrows(ResumeNotExistsStorageException.class, () -> storage.get(DUMMY));
    }

    @Test
    void update() {
        Resume resumeToUpdate = new Resume(UUID_2);
        storage.update(resumeToUpdate);
        assertEquals(resumeToUpdate, storage.get(UUID_2));
    }

    @Test
    void updateNoExists() {
        assertThrows(ResumeNotExistsStorageException.class, () -> storage.update(new Resume(DUMMY)));
    }

    @Test
    void save() {
        Resume newResume = new Resume(UUID_4);
        storage.save(newResume);
        assertEquals(4, storage.size());
        assertEquals(newResume, storage.get(UUID_4));
    }

    @Test
    void storageOverflow() {
        try {
            for (int i = storage.size(); i < STORAGE_SIZE; i++) {
                storage.save(new Resume());
            }
        } catch (StorageException e) {
            Assertions.fail("Premature storage overflow");
        }
        assertThrows(StorageException.class, () -> storage.save(new Resume()));
    }

    @Test
    void saveAlreadyExists() {
        assertThrows(ResumeExistsStorageException.class, () -> storage.save(new Resume(UUID_2)));
    }

    @Test
    void delete() {
        storage.delete(UUID_1);
        assertEquals(2, storage.size());
        assertThrows(ResumeNotExistsStorageException.class, () -> storage.get(UUID_1));
    }

    @Test
    void deleteNotExists() {
        assertThrows(ResumeNotExistsStorageException.class, () -> storage.delete(DUMMY));
    }

    @Test
    void getAll() {
        Resume[] expectedResumes = new Resume[3];
        expectedResumes[0] = new Resume(UUID_1);
        expectedResumes[1] = new Resume(UUID_2);
        expectedResumes[2] = new Resume(UUID_3);
        Resume[] getResumes = storage.getAll();
        assertArrayEquals(expectedResumes, getResumes);
    }

    @Test
    void size() {
        assertEquals(3, storage.size());
    }

}