package com.basejava.webapp.storage;

import com.basejava.webapp.Config;

class SqlStorageTest extends AbstractStorageTest {

    public SqlStorageTest() {
        super(Config.getStorage());
    }
}