package com.basejava.webapp.storage;

import com.basejava.webapp.Config;
import com.basejava.webapp.exception.ResumeNotExistsStorageException;
import com.basejava.webapp.model.Resume;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static com.basejava.webapp.ResumeTestData.createPlainResume;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SqlStorageTest {

    private final Storage storage;
    protected static final String UUID_1 = "uuid1";
    protected static final String UUID_2 = "uuid2";
    protected static final String UUID_3 = "uuid3";
    protected static final String UUID_4 = "uuid4";

    public SqlStorageTest() {
        storage = new SqlStorage(Config.getInstance().getProperty("db.url"),
                Config.getInstance().getProperty("db.user"),
                Config.getInstance().getProperty("db.password"));
    }

    @BeforeEach
    void setUp() {
        storage.clear();
        storage.save(createPlainResume(UUID_1, "John Dorian"));
        storage.save(createPlainResume(UUID_2, "Ivan Petrov"));
        storage.save(createPlainResume(UUID_3, "Petr Ivanov"));
    }

    @Test
    void clear() {
        storage.clear();
        assertSize(0);
    }

    @Test
    void get() {
        Resume resumeExpected = createPlainResume(UUID_1, "John Dorian");
        assertEquals(resumeExpected, storage.get(UUID_1));
    }

    @Test
    void save() {
        Resume newResume = createPlainResume(UUID_4, "Unknown Person");
        storage.save(newResume);
        assertSize(4);
        assertEquals(newResume, storage.get(UUID_4));
    }

    @Test
    void update() {
        Resume resumeToUpdate = createPlainResume(UUID_2, "Ivan Petrov");
        storage.update(resumeToUpdate);
        assertEquals(resumeToUpdate, storage.get(UUID_2));
    }

    @Test
    void getAllSorted() {
        List<Resume> expectedResumes = Arrays.asList(createPlainResume(UUID_2, "Ivan Petrov"),
                createPlainResume(UUID_1, "John Dorian"), createPlainResume(UUID_3, "Petr Ivanov"));
        List<Resume> actualResumes = storage.getAllSorted();
        assertEquals(expectedResumes, actualResumes);
    }

    @Test
    void delete() {
        storage.delete(UUID_1);
        assertSize(2);
        assertThrows(ResumeNotExistsStorageException.class, () -> storage.get(UUID_1));
    }

    private void assertSize(int expectedSize) {
        assertEquals(expectedSize, storage.size());
    }
}