package com.basejava.webapp.storage;

import com.basejava.webapp.storage.serializer.ObjectStream;

class ObjectStreamStorageTest extends AbstractStorageTest {

    public ObjectStreamStorageTest() {
        super(new ObjectStreamStorage(STORAGE_DIR, new ObjectStream()));
    }
}