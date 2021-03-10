package com.basejava.webapp.storage;

import com.basejava.webapp.storage.serializer.ObjectStream;

class FileStorageTest extends AbstractStorageTest {

    public FileStorageTest() {
        super(new FileStorage(STORAGE_DIR, new ObjectStream()));
    }
}