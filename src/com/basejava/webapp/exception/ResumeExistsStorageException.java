package com.basejava.webapp.exception;

public class ResumeExistsStorageException extends StorageException {
    public ResumeExistsStorageException(String uuid) {
        super("Resume " + uuid + " already exists in Storage", uuid);
    }
}
