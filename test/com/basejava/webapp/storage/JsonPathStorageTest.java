package com.basejava.webapp.storage;

import com.basejava.webapp.storage.serializer.JsonStreamSerializer;

public class JsonPathStorageTest extends AbstractStorageTest {

    public JsonPathStorageTest() {
        super(new PathStorage(STORAGE_DIR, new JsonStreamSerializer()));
    }
}