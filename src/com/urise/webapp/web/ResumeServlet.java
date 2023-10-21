package com.urise.webapp.web;

import com.urise.webapp.Config;
import com.urise.webapp.model.Resume;
import com.urise.webapp.storage.SqlStorage;
import com.urise.webapp.storage.Storage;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Writer;
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
        Writer writer = response.getWriter();
        List<Resume> resumes = storage.getAllSorted();
        writer.write("<style> table, th, td {border:1px solid black;} </style> " +
                "<table> " +
                "<tr> " +
                "<th>UUID</th> " +
                "<th>Full Name</th> " +
                "</tr>");
        for (Resume r : resumes) {
            writer.write("<tr> <td>" + r.getUuid() + "</td>");
            writer.write("<td>" + r.getFullName() + "</td> </tr>");
        }
        writer.write("</table>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

}
