package com.urise.webapp.web;

import com.urise.webapp.Config;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.*;
import com.urise.webapp.storage.Storage;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ResumeServlet extends HttpServlet {
    private Storage storage;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        storage = Config.getInstance().getStorage();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uuid = request.getParameter("uuid");
        String action = request.getParameter("action");
        if (action == null) {
            request.setAttribute("resumes", storage.getAllSorted());
            request.getRequestDispatcher("/WEB-INF/jsp/resumes.jsp").forward(request, response);
            return;
        }
        Resume r;
        switch (action) {
            case "delete" -> {
                storage.delete(uuid);
                response.sendRedirect("resume");
                return;
            }
            case "clear" -> {
                storage.clear();
                response.sendRedirect("resume");
                return;
            }
            case "view", "edit" -> {
                r = storage.get(uuid);
            }
            case "add" -> {
                r = Resume.TEMPLATE;
            }
            default -> throw new IllegalArgumentException("Action " + action + " is illegal");
        }
        request.setAttribute("resume", r);
        request.getRequestDispatcher(
                ("view".equals(action) ? "/WEB-INF/jsp/view.jsp" : "/WEB-INF/jsp/edit.jsp")
        ).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uuid = request.getParameter("uuid");
        String fullName = request.getParameter("fullName");
        Resume r;
        if (uuid == null || uuid.isEmpty()) {
            r = new Resume(fullName);
        } else {
            r = storage.get(uuid);
            r.setFullName(fullName);
        }
        for (ContactType ct : ContactType.values()) {
            String value = request.getParameter(ct.name());
            if (value != null && !value.trim().isEmpty()) {
                r.addContact(ct, value);
            } else {
                r.getContacts().remove(ct);
            }
        }
        for (SectionType st : SectionType.values()) {
            String value = request.getParameter(st.getTitle());
            if (value != null && !value.trim().isEmpty()) {
                switch (st) {
                    case PERSONAL, OBJECTIVE -> r.addSectionContent(st, new TextSection(value));
                    case ACHIEVEMENT, QUALIFICATIONS -> {
                        r.addSectionContent(st, new ListSection(List.of(value.split("\r\n"))));
                    }
                    default -> throw new NotExistStorageException("Section not exists");
                }
            } else {
                r.getSections().remove(st);
            }
        }
        try {
            storage.update(r);
        } catch (NotExistStorageException e) {
            storage.save(r);
        }

        response.sendRedirect("resume");
    }

}
