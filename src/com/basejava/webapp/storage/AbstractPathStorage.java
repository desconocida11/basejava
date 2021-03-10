package com.basejava.webapp.storage;

import com.basejava.webapp.exception.StorageException;
import com.basejava.webapp.model.Resume;
import com.basejava.webapp.storage.serializer.Serializer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

abstract public class AbstractPathStorage extends AbstractStorage<Path> {

    private final Path directory;

    protected final Serializer serializerStrategy;

    protected AbstractPathStorage(String dir, Serializer serializerStrategy) {
        this.serializerStrategy = serializerStrategy;
        directory = Paths.get(dir);
        Objects.requireNonNull(directory, "directory must not be null");
        if (!Files.isDirectory(directory)) {
            throw new IllegalArgumentException(dir + " is not directory");
        }
        if (!Files.isWritable(directory)) {
            throw new IllegalArgumentException(dir + " is not readable/writable");
        }
    }

    @Override
    protected boolean isResumeExist(Path file) {
        return Files.exists(file, LinkOption.NOFOLLOW_LINKS);
    }

    @Override
    protected Path getSearchKey(String uuid) {
        return Paths.get(directory.toString(), uuid);
    }

    @Override
    protected void saveResume(Resume resume, Path file) {
        try {
            Files.createFile(file);
        } catch (IOException e) {
            throw new StorageException("Can't create file" + file.getFileName().toString(), file.getFileName().toString(), e);
        }
        fillUpdatedResume(file, resume);
    }

    @Override
    protected void deleteResume(Path file) {
        try {
            Files.delete(file);
        } catch (IOException e) {
            throw new StorageException("Path delete error", file.getFileName().toString());
        }
    }

    @Override
    protected Resume getResume(Path file) {
        try {
            return serializerStrategy.doRead(Files.newInputStream(file));
        } catch (IOException e) {
            throw new StorageException("Can't read file" + file.getFileName().toString(), file.getFileName().toString(), e);
        }
    }

    @Override
    protected void fillUpdatedResume(Path file, Resume resume) {
        try {
            serializerStrategy.doWrite(resume, Files.newOutputStream(file));
        } catch (IOException e) {
            throw new StorageException("Update Error", file.toString(), e);
        }
    }

    @Override
    protected List<Resume> getResumeList() {
        Path[] listPaths;
        try {
            listPaths = Files.list(directory).toArray(Path[]::new);
            return doGetResumeList(listPaths);
        } catch (IOException | ClassNotFoundException e) {
            throw new StorageException("Directory read error", null, e);
        }
    }

    protected abstract List<Resume> doGetResumeList(Path[] listPaths) throws IOException, ClassNotFoundException;

    @Override
    public void clear() {
        try {
            Files.list(directory).forEach((this::deleteResume));
        } catch (IOException e) {
            throw new StorageException("Directory clear error", null, e);
        }
    }

    @Override
    public int size() {
        try {
            return (int) Files.list(directory).count();
        } catch (IOException e) {
            throw new StorageException("Directory read error", null, e);
        }
    }
}
