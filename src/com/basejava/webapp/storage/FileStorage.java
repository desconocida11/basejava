package com.basejava.webapp.storage;

import com.basejava.webapp.exception.StorageException;
import com.basejava.webapp.model.Resume;
import com.basejava.webapp.storage.serializer.Serializer;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FileStorage extends AbstractStorage<File> {

    private final File directory;

    protected final Serializer serializerStrategy;

    protected FileStorage(String dir, Serializer serializerStrategy) {
        this.serializerStrategy = serializerStrategy;
        this.directory = new File(dir);
        Objects.requireNonNull(directory, "directory must not be null");
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not directory");
        }
        if (!directory.canRead() || !directory.canWrite()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not readable/writable");
        }
    }

    @Override
    protected boolean isResumeExist(File file) {
        return file.exists();
    }

    @Override
    protected File getSearchKey(String uuid) {
        return new File(directory, uuid);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    protected void saveResume(Resume resume, File file) {
        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new StorageException("Can't create file" + file.getAbsolutePath(), file.getName(), e);
        }
        fillUpdatedResume(file, resume);
    }

    @Override
    protected void deleteResume(File file) {
        if (!file.delete()) {
            throw new StorageException("File delete error", file.getName());
        }
    }

    @Override
    protected Resume getResume(File file) {
        try {
            return serializerStrategy.doRead(new BufferedInputStream(new FileInputStream(file)));
        } catch (IOException e) {
            throw new StorageException("Can't read file" + file.getAbsolutePath(), file.getName(), e);
        }
    }

    @Override
    protected void fillUpdatedResume(File file, Resume resume) {
        try {
            serializerStrategy.doWrite(resume, new BufferedOutputStream(new FileOutputStream(file)));
        } catch (IOException e) {
            throw new StorageException("Update Error", file.getName(), e);
        }
    }

    @Override
    protected List<Resume> getResumeList() {
        File[] listFiles = directory.listFiles();
        if (listFiles == null) {
            throw new StorageException("Directory read error", null);
        }
        return doGetResumeList(listFiles);
    }


    protected List<Resume> doGetResumeList(File[] listFiles) {
        List<Resume> resumeList = new ArrayList<>();
        for (File f : listFiles) {
            resumeList.add(getResume(f));
        }
        return resumeList;
    }

    @Override
    public void clear() {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File f : files) {
                deleteResume(f);
            }
        }
    }

    @Override
    public int size() {
        final String[] list = directory.list();
        if (list == null) {
            throw new StorageException("Directory read error", null);
        }
        return list.length;
    }
}
