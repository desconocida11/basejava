package com.basejava.webapp.storage;

import com.basejava.webapp.model.Resume;
import com.basejava.webapp.storage.serializer.Serializer;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ObjectStreamPathStorage extends AbstractPathStorage {
    protected ObjectStreamPathStorage(String directory, Serializer serializerStrategy) {
        super(directory, serializerStrategy);
    }

    @Override
    protected List<Resume> doGetResumeList(Path[] listPaths) {
        List<Resume> out = new ArrayList<>();
        for (Path path : listPaths) {
            out.add(getResume(path));
        }
        return out;
    }
}
