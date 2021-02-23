package com.basejava.webapp.storage;

import com.basejava.webapp.exception.StorageException;
import com.basejava.webapp.model.Resume;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.basejava.webapp.storage.AbstractArrayStorage.STORAGE_SIZE;
import static org.junit.jupiter.api.Assertions.assertThrows;

abstract class AbstractArrayStorageTest extends AbstractStorageTest {

    public AbstractArrayStorageTest(Storage storage) {
        super(storage);
    }

    @Test
    void storageOverflow() {
        try {
            for (int i = storage.size(); i < STORAGE_SIZE; i++) {
                storage.save(new Resume(DUMMY_NAME));
            }
        } catch (StorageException e) {
            Assertions.fail("Premature storage overflow");
        }
        assertThrows(StorageException.class, () -> storage.save(new Resume(DUMMY_NAME)));
    }

}