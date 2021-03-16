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
            forEachThrowable(dataOutputStream, resume.getAllContacts().entrySet(), entry -> {
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
                        dataOutputStream.writeUTF(((SingleLineSection) entry.getValue()).getValue());
                        break;
                    case ACHIEVEMENT:
                    case QUALIFICATIONS:
                        List<String> outputList = ((BulletedListSection) entry.getValue()).getValue();
                        forEachThrowable(dataOutputStream, outputList, dataOutputStream::writeUTF);
                        break;
                    case EXPERIENCE:
                    case EDUCATION:
                        List<Organization> values = ((OrganizationSection) entry.getValue()).getValue();
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
            doLoop(sizeContacts, () -> resume.addContact(ContactType.valueOf(dataInputStream.readUTF()),
                    dataInputStream.readUTF()));
            doLoop(dataInputStream.readInt(), () -> {
                SectionType nameSection = SectionType.valueOf(dataInputStream.readUTF());
                switch (nameSection) {
                    case PERSONAL:
                    case OBJECTIVE:
                        resume.addSection(nameSection,
                                new SingleLineSection(dataInputStream.readUTF()));
                        break;
                    case ACHIEVEMENT:
                    case QUALIFICATIONS:
                        List<String> content = new ArrayList<>();
                        doLoop(dataInputStream.readInt(), () -> content.add(dataInputStream.readUTF()));
                        resume.addSection(nameSection, new BulletedListSection(content));
                        break;
                    case EXPERIENCE:
                    case EDUCATION:
                        List<Organization> valuesOrg = new ArrayList<>();
                        doLoop(dataInputStream.readInt(), () -> valuesOrg.add(readOrganization(dataInputStream)));
                        resume.addSection(nameSection, new OrganizationSection(valuesOrg));
                        break;
                }
            });
            return resume;
        }
    }

    private Organization readOrganization(DataInputStream dataInputStream) throws IOException {
        Link link = readLink(dataInputStream);
        int sizePeriods = dataInputStream.readInt();
        List<Organization.Experience> periods = new ArrayList<>();
        doLoop(sizePeriods, () -> periods.add(readExperience(dataInputStream)));
        return new Organization(link, periods);
    }

    private Link readLink(DataInputStream dataInputStream) throws IOException {
        String name = dataInputStream.readUTF();
        String url = dataInputStream.readUTF();
        return url.equals(" ") ? new Link(name) : new Link(name, url);
    }

    private Organization.Experience readExperience(DataInputStream dataInputStream) throws IOException {
        LocalDate startDate = readDate(dataInputStream);
        LocalDate endDate = readDate(dataInputStream);
        String title = dataInputStream.readUTF();
        return new Organization.Experience(startDate, endDate, title);
    }

    private void writeDate(DataOutputStream dataOutputStream, LocalDate localDate) throws IOException {
        dataOutputStream.writeUTF(localDate.format(FORMATTER));
    }

    private LocalDate readDate(DataInputStream dataInputStream) throws IOException {
        return LocalDate.from(FORMATTER.parse(dataInputStream.readUTF()));
    }

    private void writeExperience(DataOutputStream dataOutputStream, Organization.Experience experience) throws IOException {
        writeDate(dataOutputStream, experience.getStartDate());
        writeDate(dataOutputStream, experience.getEndDate());
        dataOutputStream.writeUTF(experience.getTitle());
    }

    @FunctionalInterface
    public interface ThrowingConsumer<T> {
        void accept(T t) throws IOException;
    }

    @FunctionalInterface
    public interface ThrowingRunnable {
        void run() throws IOException;
    }

    private void doLoop(int size, ThrowingRunnable action) throws IOException {
        for (int j = 0; j < size; j++) {
            action.run();
        }
    }

    private <T> void forEachThrowable(DataOutputStream dataOutputStream, Collection<T> outputList, ThrowingConsumer<T> action) throws IOException {
        Objects.requireNonNull(action);
        dataOutputStream.writeInt(outputList.size());
        for (T t : outputList) {
            action.accept(t);
        }
    }
}
