package com.urise.webapp.storage.serializer;

import com.urise.webapp.model.*;

import java.io.*;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataStreamSerializer implements Serializer {

    @Override
    public void doWrite(Resume r, OutputStream os) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(os)) {
            dos.writeUTF(r.getUuid());
            dos.writeUTF(r.getFullName());
            doWriteContacts(r, dos);
            doWriteSections(r, dos);
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    @Override
    public Resume doRead(InputStream is) throws IOException {
        try (DataInputStream dis = new DataInputStream(is)) {
            Resume resume = new Resume(dis.readUTF(), dis.readUTF());
            int contactsSize = dis.readInt();
            for (int i = 0; i < contactsSize; i++) {
                resume.addContact(ContactType.valueOf(dis.readUTF()), dis.readUTF());
            }
            int sectionsSize = dis.readInt();
            for (int i = 0; i < sectionsSize; i++) {
                doReadSection(resume, dis);
            }
            return resume;
        }
    }

    private void doWriteContacts(Resume r, DataOutputStream dos) throws IOException {
        Map<ContactType, String> contacts = r.getContacts();
        dos.writeInt(contacts.size());
        for (Map.Entry<ContactType, String> entry : contacts.entrySet()) {
            dos.writeUTF(entry.getKey().name());
            dos.writeUTF(entry.getValue());
        }
    }

    private void doWriteSections(Resume r, DataOutputStream dos) throws IOException {
        Map<SectionType, Section> sections = r.getSections();
        dos.writeInt(sections.size());
        for (Map.Entry<SectionType, Section> entry : sections.entrySet()) {
            dos.writeUTF(entry.getKey().name());
            doWriteSpecificSection(entry, dos);
        }
    }

    private void doWriteSpecificSection(Map.Entry<SectionType, Section> entry, DataOutputStream dos) throws IOException {
        switch (entry.getKey())
        {
            case ACHIEVEMENT, QUALIFICATIONS -> {
                ListSection ls = (ListSection) entry.getValue();
                dos.writeInt(ls.getFields().size());
                ls.getFields().forEach(str -> {
                    try {
                        dos.writeUTF(str);
                    } catch (IOException e) {
                        throw new RuntimeException("Writing error", e);
                    }
                });
            }
            case PERSONAL, OBJECTIVE -> {
                TextSection ts = (TextSection) entry.getValue();
                dos.writeUTF(ts.getText());
            }
            case EXPERIENCE, EDUCATION -> {
                CompanySection cs = (CompanySection) entry.getValue();
                dos.writeInt(cs.getCompanyList().size());
                cs.getCompanyList().forEach(company -> {
                    try {
                        dos.writeUTF(company.getName());
                        dos.writeUTF(company.getUrl().toString());
                        dos.writeInt(company.getPeriods().size());
                        company.getPeriods().forEach(period -> {
                            try {
                                doWriteLocalDate(dos, period.getStartDate());
                                doWriteLocalDate(dos, period.getEndDate());
                                dos.writeUTF(period.getTitle());
                                dos.writeUTF(period.getDescription());
                            } catch (IOException e) {
                                throw new RuntimeException("Writing error", e);
                            }
                        });
                    } catch (IOException e) {
                        throw new RuntimeException("Writing error", e);
                    }
                });
            }
        }
    }

    private void doReadSection(Resume r, DataInputStream dis) throws IOException {
        String sectionName = dis.readUTF();
        switch (sectionName) {
            case "PERSONAL", "OBJECTIVE" ->
                    r.addSectionContent(SectionType.valueOf(sectionName), new TextSection(dis.readUTF()));

            case "ACHIEVEMENT", "QUALIFICATIONS" -> {
                int sectionSize = dis.readInt();
                List<String> list = new ArrayList<>();
                for (int i = 0; i < sectionSize; i++) {
                    list.add(dis.readUTF());
                }
                r.addSectionContent(SectionType.valueOf(sectionName), new ListSection(list));
            }
            case "EXPERIENCE", "EDUCATION" -> {
                int sectionSize = dis.readInt();
                ArrayList<Company> companies = new ArrayList<>();
                for (int i = 0; i < sectionSize; i++) {
                    Company company = new Company(dis.readUTF(), new URL(dis.readUTF()));
                    int periodsSize = dis.readInt();
                    ArrayList<Period> periods = new ArrayList<>();
                    for (int j = 0; j < periodsSize; j++) {
                        periods.add(new Period(doReadLocalDate(dis), doReadLocalDate(dis), dis.readUTF(), dis.readUTF()));
                    }
                    company.setPeriods(periods);
                    companies.add(company);
                }
                r.addSectionContent(SectionType.valueOf(sectionName), new CompanySection(companies));
            }
        }
    }

    private void doWriteLocalDate(DataOutputStream dos, LocalDate ld) throws IOException {
        dos.writeInt(ld.getYear());
        dos.writeInt(ld.getMonth().getValue());
        dos.writeInt(ld.getDayOfMonth());
    }

    private LocalDate doReadLocalDate(DataInputStream dis) throws IOException {
        return LocalDate.of(dis.readInt(), dis.readInt(), dis.readInt());
    }

}
