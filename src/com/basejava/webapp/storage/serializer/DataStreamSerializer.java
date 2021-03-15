package com.basejava.webapp.storage.serializer;

import com.basejava.webapp.model.*;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DataStreamSerializer implements Serializer {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-d");

    @Override
    public void doWrite(Resume resume, OutputStream outputStream) throws IOException {
        try (DataOutputStream dataOutputStream = new DataOutputStream(outputStream)) {
            dataOutputStream.writeUTF(resume.getUuid());
            dataOutputStream.writeUTF(resume.getFullName());
            final Map<ContactType, String> allContacts = resume.getAllContacts();
            dataOutputStream.writeInt(allContacts.size());
            for (Map.Entry<ContactType, String> entry : allContacts.entrySet()) {
                dataOutputStream.writeUTF(entry.getKey().name());
                dataOutputStream.writeUTF(entry.getValue());
            }
            Set<Map.Entry<SectionType, AbstractSection>> allSections = resume.getAllSections().entrySet();
            dataOutputStream.writeInt(allSections.size());
            for (Map.Entry<SectionType, AbstractSection> entry : allSections) {
                dataOutputStream.writeUTF(entry.getKey().name());
                switch (entry.getKey()) {
                    case PERSONAL:
                    case OBJECTIVE:
                        SingleLineSection singleLineSection = (SingleLineSection) entry.getValue();
                        dataOutputStream.writeUTF(singleLineSection.toString());
                        break;
                    case ACHIEVEMENT:
                    case QUALIFICATIONS:
                        BulletedListSection bulletedListSection = (BulletedListSection) entry.getValue();
                        writeList(dataOutputStream, bulletedListSection.getValue());
                        break;
                    case EXPERIENCE:
                    case EDUCATION:
                        OrganizationSection organizationSection = (OrganizationSection) entry.getValue();
                        List<Organization> values = organizationSection.getValue();
                        dataOutputStream.writeInt(values.size());
                        for (Organization value : values) {
                            dataOutputStream.writeUTF(value.getOrganization().getName());
                            String url = value.getOrganization().getUrl();
                            dataOutputStream.writeUTF(url != null ? url : " ");
                            List<Organization.Experience> experiencePeriods = value.getPeriods();
                            dataOutputStream.writeInt(experiencePeriods.size());
                            for (Organization.Experience experience : experiencePeriods) {
                                writeExperience(dataOutputStream, experience);
                            }
                        }
                        break;
                }
            }
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
                String nameSection = dataInputStream.readUTF();
                switch (SectionType.valueOf(nameSection)) {
                    case PERSONAL:
                    case OBJECTIVE:
                        resume.addSection(SectionType.valueOf(nameSection),
                                new SingleLineSection(dataInputStream.readUTF()));
                        break;
                    case ACHIEVEMENT:
                    case QUALIFICATIONS:
                        List<String> content = readList(dataInputStream.readInt(), dataInputStream);
                        resume.addSection(SectionType.valueOf(nameSection), new BulletedListSection(content));
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
                        resume.addSection(SectionType.valueOf(nameSection), new OrganizationSection(valuesOrg));
                        break;
                }
            }
            return resume;
        }
    }

    private Link readLink(DataInputStream dataInputStream) throws IOException {
        String name = dataInputStream.readUTF();
        String url = dataInputStream.readUTF();
        if (!url.equals(" ")) {
            return new Link(name, url);
        } else {
            return new Link(name);
        }
    }

    private void writeExperience(DataOutputStream dataOutputStream, Organization.Experience experience) throws IOException {
        dataOutputStream.writeUTF(experience.getStartDate().format(FORMATTER));
        dataOutputStream.writeUTF(experience.getEndDate().format(FORMATTER));
        dataOutputStream.writeUTF(experience.getTitle());
    }

    private <T> void writeList(DataOutputStream dataOutputStream, List<T> outputList) throws IOException {
        dataOutputStream.writeInt(outputList.size());
        for (T value : outputList) {
            dataOutputStream.writeUTF((String) value);
        }
    }

    private Organization.Experience readExperience(DataInputStream dataInputStream) throws IOException {
        LocalDate startDate = LocalDate.from(FORMATTER.parse(dataInputStream.readUTF()));
        LocalDate endDate = LocalDate.from(FORMATTER.parse(dataInputStream.readUTF()));
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
