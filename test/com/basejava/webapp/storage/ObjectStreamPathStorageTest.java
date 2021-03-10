package com.basejava.webapp.storage;

import com.basejava.webapp.storage.serializer.ObjectStream;

class ObjectStreamPathStorageTest extends AbstractStorageTest {

    public ObjectStreamPathStorageTest() {
        super(new ObjectStreamPathStorage(STORAGE_DIR, new ObjectStream()));
    }
}