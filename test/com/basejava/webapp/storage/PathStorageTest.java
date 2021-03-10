package com.basejava.webapp.storage;

import com.basejava.webapp.storage.serializer.ObjectStream;

class PathStorageTest extends AbstractStorageTest {

    public PathStorageTest() {
        super(new PathStorage(STORAGE_DIR, new ObjectStream()));
    }
}