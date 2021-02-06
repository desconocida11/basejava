package com.basejava.webapp.exception;

public class ResumeNotExistsStorageException extends StorageException {
    public ResumeNotExistsStorageException(String uuid) {
        super("Resume " + uuid + " doesn't exist in Storage", uuid);
    }
}
