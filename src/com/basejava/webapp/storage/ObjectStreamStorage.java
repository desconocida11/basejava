package com.basejava.webapp.storage;

import com.basejava.webapp.model.Resume;
import com.basejava.webapp.storage.serializer.Serializer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ObjectStreamStorage extends AbstractFileStorage {
    protected ObjectStreamStorage(String dir, Serializer serializerStrategy) {
        super(dir, serializerStrategy);
    }

    @Override
    protected List<Resume> doGetResumeList(File[] listFiles) {
        List<Resume> resumeList = new ArrayList<>();
        for (File f : listFiles) {
            resumeList.add(getResume(f));
        }
        return resumeList;
    }
}
