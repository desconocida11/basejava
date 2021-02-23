package com.basejava.webapp.model;

import java.util.Comparator;
import java.util.Objects;
import java.util.UUID;

/**
 * Initial resume class
 */
public class Resume {

    public static final ResumeFullNameComparator RESUME_FULL_NAME_COMPARATOR = new ResumeFullNameComparator();
    public static final ResumeUuidComparator RESUME_UUID_COMPARATOR = new ResumeUuidComparator();

    private static class ResumeFullNameComparator implements Comparator<Resume> {
        @Override
        public int compare(Resume resume, Resume t1) {
            return resume.getFullName().compareTo(t1.getFullName());
        }
    }

    private static class ResumeUuidComparator implements Comparator<Resume> {
        @Override
        public int compare(Resume resume, Resume t1) {
            return t1.getUuid().compareTo(resume.getUuid());
        }
    }

    // Unique identifier
    private final String uuid;

    private final String fullName;

    public String getFullName() {
        return fullName;
    }

    public Resume(String uuid, String fullName) {
        this.uuid = uuid;
        this.fullName = fullName;
    }

    public Resume(String fullName) {
        this(UUID.randomUUID().toString(), fullName);
    }

    public String getUuid() {
        return uuid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Resume resume = (Resume) o;
        return uuid.equals(resume.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }

    @Override
    public String toString() {
        return uuid;
    }

}
