package com.basejava.webapp.storage;

import com.basejava.webapp.exception.StorageException;
import com.basejava.webapp.model.Resume;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public abstract class AbstractFileStorage extends AbstractStorage<File> {

    private final File directory;

    protected AbstractFileStorage(File directory) {
        Objects.requireNonNull(directory, "directory must not be null");
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not directory");
        }
        if (!directory.canRead() || !directory.canWrite()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not readable/writable");
        }
        this.directory = directory;
    }

    @Override
    protected boolean isResumeExist(File file) {
        return file.exists();
    }

    @Override
    protected File getSearchKey(String uuid) {
        return new File(directory, uuid);
    }

    @Override
    protected void saveResume(Resume resume, File file) {
        try {
            boolean newFile = file.createNewFile();
            doWrite(resume, file);
        } catch (IOException e) {
            throw new StorageException("IO Error", file.getName(), e);
        }
    }

    protected abstract void doWrite(Resume resume, File file) throws IOException;

    @Override
    protected void deleteResume(File file) {
        if (!file.delete()) {
            throw new StorageException("File delete error", file.getName());
        }
    }

    @Override
    protected Resume getResume(File file) {
        return doGetResumeByName(file.getName());
    }

    protected abstract Resume doGetResumeByName(String name);

    @Override
    protected void fillUpdatedResume(File file, Resume resume) {
        try {
            doWrite(resume, file);
        } catch (IOException e) {
            throw new StorageException("Update Error", file.getName(), e);
        }
    }

    @Override
    protected List<Resume> getResumeList() {
        File[] listFiles = directory.listFiles();
        return listFiles == null ? null : doGetResumeList(listFiles);
    }

    protected abstract List<Resume> doGetResumeList(File[] listFiles);

    @Override
    public void clear() {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File f : files) {
                if (!f.delete()) {
                    throw new StorageException("Clear storage error", f.getName());
                }
            }
        }
    }

    @Override
    public int size() {
        final String[] list = directory.list();
        if (list != null) {
            return list.length;
        }
        return 0;
    }
}
