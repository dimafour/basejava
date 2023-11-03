package com.urise.webapp.web;

import com.urise.webapp.Config;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.*;
import com.urise.webapp.storage.Storage;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.DateTimeException;
import java.time.LocalDate;
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
            String[] values = request.getParameterValues(st.name());
            if (values != null && values[0] != null && !values[0].trim().isEmpty()) {
                switch (st) {
                    case PERSONAL, OBJECTIVE -> r.addSectionContent(st, new TextSection(values[0]));
                    case ACHIEVEMENT, QUALIFICATIONS -> {
                        r.addSectionContent(st, new ListSection(List.of(values[0].split("\r\n"))));
                    }
                    case EDUCATION, EXPERIENCE -> {
                        List<Company> companyList = new ArrayList<>();
                        String[] urls = request.getParameterValues("url" + st.name());
                        for (int i = 0; i < values.length; i++) {
                            String name = values[i];
                            if (name != null && !name.trim().isEmpty()) {
                                List<Period> periods = new ArrayList<>();
                                String[] startDates = request.getParameterValues("startDate" + st.name() + i);
                                String[] endDates = request.getParameterValues("endDate" + st.name() + i);
                                String[] titles = request.getParameterValues("title" + st.name() + i);
                                String[] descriptions = request.getParameterValues("description" + st.name() + i);
                                if (titles != null && !titles[0].trim().isEmpty()) {
                                    for (int j = 0; j < titles.length; j++) {
                                        LocalDate startDate;
                                        LocalDate endDate;
                                        try {
                                            startDate = LocalDate.parse(startDates[j]);
                                            endDate = LocalDate.parse(endDates[j]);
                                        } catch (DateTimeException e) {
                                            startDate = LocalDate.now().minusDays(1);
                                            endDate = LocalDate.now();
                                        }
                                        if (startDate.isBefore(endDate)) {
                                            periods.add(new Period(startDate, endDate, titles[j], descriptions[j]));
                                        }
                                    }
                                }
                                URL url;
                                try {
                                    url = new URL(urls[i]);
                                } catch (MalformedURLException e) {
                                    url = null;
                                }
                                companyList.add(new Company(name, url, periods));
                            }
                        }
                        r.addSectionContent(st, new CompanySection(companyList));
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
