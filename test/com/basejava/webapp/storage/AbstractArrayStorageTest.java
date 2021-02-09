package com.basejava.webapp.storage;

import com.basejava.webapp.exception.ResumeExistsStorageException;
import com.basejava.webapp.exception.ResumeNotExistsStorageException;
import com.basejava.webapp.exception.StorageException;
import com.basejava.webapp.model.Resume;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static com.basejava.webapp.storage.AbstractArrayStorage.STORAGE_SIZE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

abstract class AbstractArrayStorageTest {

    private final Storage storage;
    protected static final String UUID_1 = "uuid1";
    protected static final String UUID_2 = "uuid2";
    protected static final String UUID_3 = "uuid3";
    protected static final String UUID_4 = "uuid4";
    protected static final String UUID_5 = "uuid5";

    public AbstractArrayStorageTest(Storage storage) {
        this.storage = storage;
    }

    @BeforeEach
    void setUp() {
        storage.clear();
        storage.save(new Resume(UUID_1));
        storage.save(new Resume(UUID_2));
        storage.save(new Resume(UUID_3));
        storage.save(new Resume(UUID_5));
        storage.save(new Resume(UUID_4));
    }

    @Test
    void clear() {
        storage.clear();
        assertEquals(0, storage.size());
    }

    @Test
    void get() {
        Resume resumeExpected = new Resume("uuid1");
        assertEquals(resumeExpected, storage.get("uuid1"));
    }

    @Test
    void getNotExists() {
        assertThrows(ResumeNotExistsStorageException.class, () -> storage.get("dummy"));
    }

    @Test
    void update() {
        Resume resumeToUpdate = new Resume("uuid2");
        storage.update(resumeToUpdate);
        assertEquals(resumeToUpdate, storage.get("uuid2"));
    }

    @Test
    void updateNoExists() {
        assertThrows(ResumeNotExistsStorageException.class, () -> storage.update(new Resume("uuid99")));
    }

    @Test
    void save() {
        Resume newResume = new Resume("uuid6");
        storage.save(newResume);
        assertEquals(newResume, storage.get("uuid6"));
    }

    @Test
    void storageOverflow() {
        try {
            int i = storage.size();
            while ( i < STORAGE_SIZE) {
                storage.save(new Resume());
                i++;
            }
        } catch (StorageException e) {
            Assertions.fail("Premature storage overflow");
        }
        assertThrows(StorageException.class, () -> storage.save(new Resume()));
    }

    @Test
    void saveAlreadyExists() {
        assertThrows(ResumeExistsStorageException.class, () -> storage.save(new Resume("uuid2")));
    }

    @Test
    void delete() {
        storage.delete("uuid1");
        assertThrows(ResumeNotExistsStorageException.class, () -> storage.get("uuid1"));
    }

    @Test
    void deleteNotExists() {
        assertThrows(ResumeNotExistsStorageException.class, () -> storage.delete("uuid99"));
    }

    @Test
    void getAll() {
        Resume[] expectedResumes = new Resume[5];
        expectedResumes[0] = new Resume(UUID_1);
        expectedResumes[1] = new Resume(UUID_2);
        expectedResumes[2] = new Resume(UUID_3);
        expectedResumes[3] = new Resume(UUID_4);
        expectedResumes[4] = new Resume(UUID_5);
        Resume[] getResumes = storage.getAll();
        Arrays.sort(getResumes);
        Assertions.assertArrayEquals(expectedResumes, getResumes);
    }

    @Test
    void size() {
        assertEquals(5, storage.size());
    }

}