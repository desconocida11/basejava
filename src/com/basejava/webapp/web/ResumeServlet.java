package com.basejava.webapp.web;

import com.basejava.webapp.Config;
import com.basejava.webapp.model.Resume;
import com.basejava.webapp.storage.Storage;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ResumeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType("text/html; charset=" + StandardCharsets.UTF_8.name());
        String name = request.getParameter("name");
        response.getWriter().write(name == null ? "Hello, Resumes!" : "Hello, " + name + "!");
        response.getWriter().write("<table>");
        final Storage ARRAY_STORAGE = Config.getStorage();
        for (Resume resume : ARRAY_STORAGE.getAllSorted()) {
            response.getWriter().write("<tr>");
            response.getWriter().write("<td>" + resume.getUuid() + "</td>" + "<td>" + resume.getFullName() + "</td>");
            response.getWriter().write("</tr>");
        }
        response.getWriter().write("</table>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
