package com.basejava.webapp.util;

import com.basejava.webapp.model.AbstractSection;
import com.basejava.webapp.model.Resume;
import com.basejava.webapp.model.SectionType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.basejava.webapp.ResumeTestData.RESUME_2;
import static com.basejava.webapp.ResumeTestData.RESUME_3;
import static org.junit.jupiter.api.Assertions.assertEquals;

class JsonParserTest {

    @Test
    void testResume() {
        String json = JsonParser.write(RESUME_2);
        System.out.println(json);
        Resume resume = JsonParser.read(json, Resume.class);
        assertEquals(RESUME_2, resume);
    }

    @Test
    void write() {
        AbstractSection section1 = RESUME_3.getSectionByType(SectionType.EXPERIENCE);
        String jsonSection = JsonParser.write(section1, AbstractSection.class);
        System.out.println(jsonSection);
        AbstractSection section2 = JsonParser.read(jsonSection, AbstractSection.class);
        Assertions.assertEquals(section1, section2);
    }
}