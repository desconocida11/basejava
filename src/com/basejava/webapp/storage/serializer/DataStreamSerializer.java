package com.basejava.webapp.storage.serializer;

import com.basejava.webapp.model.*;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class DataStreamSerializer implements Serializer {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-d");

    @Override
    public void doWrite(Resume resume, OutputStream outputStream) throws IOException {
        try (DataOutputStream dataOutputStream = new DataOutputStream(outputStream)) {
            dataOutputStream.writeUTF(resume.getUuid());
            dataOutputStream.writeUTF(resume.getFullName());
            final Map<ContactType, String> allContacts = resume.getAllContacts();
            forEachThrowable(dataOutputStream, allContacts.entrySet(), entry -> {
                dataOutputStream.writeUTF(entry.getKey().name());
                dataOutputStream.writeUTF(entry.getValue());
            });
            Set<Map.Entry<SectionType, AbstractSection>> allSections = resume.getAllSections().entrySet();
            forEachThrowable(dataOutputStream, allSections, entry -> {
                SectionType entryKey = entry.getKey();
                dataOutputStream.writeUTF(entryKey.name());
                switch (entryKey) {
                    case PERSONAL:
                    case OBJECTIVE:
                        SingleLineSection singleLineSection = (SingleLineSection) entry.getValue();
                        dataOutputStream.writeUTF(singleLineSection.getValue());
                        break;
                    case ACHIEVEMENT:
                    case QUALIFICATIONS:
                        BulletedListSection bulletedListSection = (BulletedListSection) entry.getValue();
                        List<String> outputList = bulletedListSection.getValue();
                        forEachThrowable(dataOutputStream, outputList, dataOutputStream::writeUTF);
                        break;
                    case EXPERIENCE:
                    case EDUCATION:
                        OrganizationSection organizationSection = (OrganizationSection) entry.getValue();
                        List<Organization> values = organizationSection.getValue();

                        forEachThrowable(dataOutputStream, values, organization -> {
                            Link valueOrganization = organization.getOrganization();
                            dataOutputStream.writeUTF(valueOrganization.getName());
                            String url = valueOrganization.getUrl();
                            dataOutputStream.writeUTF(url != null ? url : " ");
                            List<Organization.Experience> experiencePeriods = organization.getPeriods();
                            forEachThrowable(dataOutputStream, experiencePeriods, experience -> writeExperience(dataOutputStream, experience));
                        });
                        break;
                }
            });
        }
    }

    @Override
    public Resume doRead(InputStream inputStream) throws IOException {
        try (DataInputStream dataInputStream = new DataInputStream(inputStream)) {
            String uuid = dataInputStream.readUTF();
            String fullName = dataInputStream.readUTF();
            Resume resume = new Resume(uuid, fullName);
            int sizeContacts = dataInputStream.readInt();
            for (int i = 0; i < sizeContacts; i++) {
                resume.addContact(ContactType.valueOf(dataInputStream.readUTF()),
                        dataInputStream.readUTF());
            }
            int sizeSections = dataInputStream.readInt();
            for (int i = 0; i < sizeSections; i++) {
                SectionType nameSection = SectionType.valueOf(dataInputStream.readUTF());
                switch (nameSection) {
                    case PERSONAL:
                    case OBJECTIVE:
                        resume.addSection(nameSection,
                                new SingleLineSection(dataInputStream.readUTF()));
                        break;
                    case ACHIEVEMENT:
                    case QUALIFICATIONS:
                        List<String> content = readList(dataInputStream.readInt(), dataInputStream);
                        resume.addSection(nameSection, new BulletedListSection(content));
                        break;
                    case EXPERIENCE:
                    case EDUCATION:
                        int sizeOrganizations = dataInputStream.readInt();
                        List<Organization> valuesOrg = new ArrayList<>();
                        for (int j = 0; j < sizeOrganizations; j++) {
                            Link link = readLink(dataInputStream);
                            int sizePeriods = dataInputStream.readInt();
                            List<Organization.Experience> periods = new ArrayList<>();
                            for (int k = 0; k < sizePeriods; k++) {
                                periods.add(readExperience(dataInputStream));
                            }
                            valuesOrg.add(new Organization(link, periods));
                        }
                        resume.addSection(nameSection, new OrganizationSection(valuesOrg));
                        break;
                }
            }
            return resume;
        }
    }

    private Link readLink(DataInputStream dataInputStream) throws IOException {
        String name = dataInputStream.readUTF();
        String url = dataInputStream.readUTF();
        return url.equals(" ") ? new Link(name) : new Link(name, url);
    }

    private String formatDate(LocalDate localDate) {
        return localDate.format(FORMATTER);
    }

    private LocalDate getDateFromString(String date) {
        return LocalDate.from(FORMATTER.parse(date));
    }

    private void writeExperience(DataOutputStream dataOutputStream, Organization.Experience experience) throws IOException {
        dataOutputStream.writeUTF(formatDate(experience.getStartDate()));
        dataOutputStream.writeUTF(formatDate(experience.getEndDate()));
        dataOutputStream.writeUTF(experience.getTitle());
    }

    @FunctionalInterface
    public interface ThrowingConsumer<T> {
        void accept(T t) throws IOException;
    }

    private <T> void forEachThrowable(DataOutputStream dataOutputStream, Collection<T> outputList, ThrowingConsumer<T> action) throws IOException {
        Objects.requireNonNull(action);
        dataOutputStream.writeInt(outputList.size());
        for (T t : outputList) {
            action.accept(t);
        }
    }

    private Organization.Experience readExperience(DataInputStream dataInputStream) throws IOException {
        LocalDate startDate = getDateFromString(dataInputStream.readUTF());
        LocalDate endDate = getDateFromString(dataInputStream.readUTF());
        String title = dataInputStream.readUTF();
        return new Organization.Experience(startDate, endDate, title);
    }

    private <T> List<T> readList(int size, DataInputStream dataInputStream) throws IOException {
        List<T> content = new ArrayList<>();
        for (int j = 0; j < size; j++) {
            content.add((T) dataInputStream.readUTF());
        }
        return content;
    }
}
