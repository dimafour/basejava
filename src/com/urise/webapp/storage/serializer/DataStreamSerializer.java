package com.urise.webapp.storage.serializer;

import com.urise.webapp.model.*;

import java.io.*;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class DataStreamSerializer implements Serializer {

    @Override
    public void doWrite(Resume r, OutputStream os) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(os)) {
            dos.writeUTF(r.getUuid());
            dos.writeUTF(r.getFullName());
            doWriteCollection(dos, r.getContacts().entrySet(), entry -> {
                dos.writeUTF(entry.getKey().name());
                dos.writeUTF(entry.getValue());
            });
            doWriteCollection(dos, r.getSections().entrySet(), entry -> {
                dos.writeUTF(entry.getKey().name());
                doWriteSection(entry, dos);
            });
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


    private void doWriteSection(Map.Entry<SectionType, Section> entry, DataOutputStream dos) throws IOException {
        SectionType key = entry.getKey();
        switch (key) {
            case ACHIEVEMENT, QUALIFICATIONS -> {
                ListSection ls = (ListSection) entry.getValue();
                doWriteCollection(dos, ls.getFields(), dos::writeUTF);
            }
            case PERSONAL, OBJECTIVE -> {
                TextSection ts = (TextSection) entry.getValue();
                dos.writeUTF(ts.getText());
            }
            case EXPERIENCE, EDUCATION -> {
                CompanySection cs = (CompanySection) entry.getValue();
                doWriteCollection(dos, cs.getCompanyList(), company -> {
                    dos.writeUTF(company.getName());
                    dos.writeUTF(company.getUrl().toString());
                    doWriteCollection(dos, company.getPeriods(), period -> {
                        doWriteLocalDate(dos, period.getStartDate());
                        doWriteLocalDate(dos, period.getEndDate());
                        dos.writeUTF(period.getTitle());
                        dos.writeUTF(period.getDescription());
                    });
                });
            }
        }
    }

    private void doReadSection(Resume r, DataInputStream dis) throws IOException {
        SectionType s = SectionType.valueOf(dis.readUTF());
        switch (s) {
            case PERSONAL, OBJECTIVE -> r.addSectionContent(s, new TextSection(dis.readUTF()));
            case ACHIEVEMENT, QUALIFICATIONS -> {
                int sectionSize = dis.readInt();
                List<String> list = new ArrayList<>();
                for (int i = 0; i < sectionSize; i++) {
                    list.add(dis.readUTF());
                }
                r.addSectionContent(s, new ListSection(list));
            }
            case EXPERIENCE, EDUCATION -> {
                int sectionSize = dis.readInt();
                ArrayList<Company> companies = new ArrayList<>();
                for (int i = 0; i < sectionSize; i++) {
                    Company company = new Company(dis.readUTF(), new URL(dis.readUTF()));
                    int periodsSize = dis.readInt();
                    List<Period> periods = new ArrayList<>();
                    for (int j = 0; j < periodsSize; j++) {
                        periods.add(new Period(doReadLocalDate(dis), doReadLocalDate(dis), dis.readUTF(), dis.readUTF()));
                    }
                    company.setPeriods(periods);
                    companies.add(company);
                }
                r.addSectionContent(s, new CompanySection(companies));
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

    private interface writeWithException<E> {
        void write(E item) throws IOException;
    }

    private <E> void doWriteCollection(DataOutputStream dos, Collection<E> collection, writeWithException<E> wwe) throws IOException {
        dos.writeInt(collection.size());
        for (E item : collection) {
            wwe.write(item);
        }
    }
}
