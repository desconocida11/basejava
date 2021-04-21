package com.basejava.webapp.web;

import com.basejava.webapp.Config;
import com.basejava.webapp.model.*;
import com.basejava.webapp.storage.Storage;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ResumeServlet extends HttpServlet {

    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-d");

    private Storage storage;

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        storage = Config.getStorage();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uuid = request.getParameter("uuid");
        String action = request.getParameter("action");
        if (action == null) {
            request.setAttribute("resumes", storage.getAllSorted());
            request.getRequestDispatcher("/WEB-INF/jsp/list.jsp").forward(request, response);
            return;
        }
        Resume r;
        switch (action) {
            case "delete":
                storage.delete(uuid);
                response.sendRedirect("resume");
                return;
            case "view":
            case "edit":
                if (uuid != null) {
                    r = storage.get(uuid);
                } else {
                    r = new Resume();
                }
                break;
            default:
                throw new IllegalArgumentException("Action " + action + " is illegal");
        }
        request.setAttribute("resume", r);

        request.getRequestDispatcher("view".equals(action) ? "/WEB-INF/jsp/view.jsp" : "/WEB-INF/jsp/edit.jsp")
                .forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding(StandardCharsets.UTF_8.name());
        String fullName = request.getParameter("fullName");
        Resume resume;
        String uuid = request.getParameter("uuid");
        if (uuid == null || uuid.trim().length() == 0) {
            resume = new Resume(fullName);
        } else {
            resume = storage.get(uuid);
            resume.setFullName(fullName);
        }
        for (ContactType type : ContactType.values()) {
            String value = request.getParameter(type.name());
            if (value != null && value.trim().length() != 0) {
                resume.addContact(type, value);
            } else {
                resume.getAllContacts().remove(type);
            }
        }
        Map<String, String[]> values = request.getParameterMap();
        for (SectionType type : SectionType.values()) {
            boolean typeExists = false;
            String typeName = type.name();
            String value = request.getParameter(typeName);
            List<Organization> organizations = null;
            if (value != null && value.trim().length() != 0) {
                typeExists = true;
            } else {
                organizations = new ArrayList<>();
                for (Map.Entry<String, String[]> entry : values.entrySet()) {
                    if (entry.getKey().startsWith(typeName)) {
                        String[] entryValue = entry.getValue();
                        Link link;
                        if (entryValue[1].equals("")) {
                            link = new Link(entryValue[0]);
                        } else {
                            link = new Link(entryValue[0], entryValue[1]);
                        }
                        List<Organization.Experience> periods = new ArrayList<>();
                        for (int i = 0; i < entryValue.length - 2; i += 3) {
                            LocalDate startDate = parseInputDate(entryValue[2 + i]);
                            LocalDate endDate = parseInputDate(entryValue[3 + i]);
                            String title = entryValue[4 + i];
                            if (startDate != null && endDate != null && title != null && title.trim().length() > 0) {
                                periods.add(new Organization.Experience(startDate, endDate, title));
                            }
                        }
                        if (!periods.isEmpty()) {
                            organizations.add(new Organization(link, periods));
                        }
                    }
                }
                if (!organizations.isEmpty()) {
                    typeExists = true;
                }
            }
            if (typeExists) {
                switch (type) {
                    case PERSONAL:
                    case OBJECTIVE:
                        resume.addSection(type, new SingleLineSection(value));
                        break;
                    case ACHIEVEMENT:
                    case QUALIFICATIONS:
                        String[] section = Arrays.stream(value.trim().split("\n")).filter(s -> s.trim().length() > 0).toArray(String[]::new);
                        resume.addSection(type, new BulletedListSection(section));
                        break;
                    case EXPERIENCE:
                    case EDUCATION:
                        resume.addSection(type, new OrganizationSection(organizations));
                        break;
                }
            } else {
                resume.getAllSections().remove(type);
            }
        }
        if (uuid == null || uuid.trim().length() == 0) {
            storage.save(resume);
        } else {
            storage.update(resume);
        }
        response.sendRedirect("resume");
    }

    private LocalDate parseInputDate(String input) {
        LocalDate parsedDate;
        try {
            parsedDate = LocalDate.parse((input + "-01"), formatter);
        } catch (DateTimeParseException exception) {
            return null;
        }
        return parsedDate;
    }
}
