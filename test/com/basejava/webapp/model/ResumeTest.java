package com.basejava.webapp.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Map;

class ResumeTest {

    private final Resume resume = new Resume("John Doe");
    private final AbstractSection singleLineSection = new SingleLineSection("Jr SW Engineer");
    private final AbstractSection bulletedListSection = buildBulletedListSection();
    private final AbstractSection datedSection = buildDatedSection();

    private AbstractSection buildBulletedListSection() {
        AbstractSection bulletedListSection = new BulletedListSection();
        bulletedListSection.addValue("Java");
        bulletedListSection.addValue("PostgreSQL");
        bulletedListSection.addValue("Python: Django");
        return bulletedListSection;
    }

    private AbstractSection buildDatedSection() {
        AbstractSection datedSection = new DatedSection();
        datedSection.addValue(new Content(LocalDate.parse("2017-07-01"), LocalDate.now(), "LLC Any", "Jr SW Engineer"));
        datedSection.addValue(new Content(LocalDate.parse("2015-01-01"), LocalDate.parse("2017-07-01"), "University", "assistant"));
        return datedSection;
    }

    @BeforeEach
    void setUp() {
        resume.addContact(ContactType.EMAIL, "john.doe@gmail.com");
        resume.addContact(ContactType.GITHUB, "https://github.com/johndoe/basejava.git");
        resume.addContact(ContactType.PHONE, "+79001234567");
        resume.addSection(SectionType.OBJECTIVE, singleLineSection);
        resume.addSection(SectionType.QUALIFICATIONS, bulletedListSection);
        resume.addSection(SectionType.EXPERIENCE, datedSection);

        for (Map.Entry<ContactType, String> entry : resume.getAllContacts().entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }

        for (Map.Entry<SectionType, AbstractSection> entry : resume.getAllSections().entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    @Test
    void getExistedSection() {
        AbstractSection singleLineSection = new SingleLineSection("Jr SW Engineer");
        Assertions.assertEquals(singleLineSection, resume.getSectionByType(SectionType.OBJECTIVE));
    }

}