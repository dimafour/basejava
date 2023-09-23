package com.urise.webapp.storage.serializer;

import com.urise.webapp.model.*;

import java.io.*;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;

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
            doReadCollection(dis, () -> {
                ContactType ct = ContactType.valueOf(dis.readUTF());
                resume.addContact(ct, dis.readUTF());
            });
            doReadCollection(dis, () -> {
                SectionType st = SectionType.valueOf(dis.readUTF());
                resume.addSectionContent(st, doReadSection(st, dis));
            });
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

    private Section doReadSection(SectionType st, DataInputStream dis) throws IOException {
        switch (st) {
            case PERSONAL, OBJECTIVE -> {
                return new TextSection(dis.readUTF());
            }
            case ACHIEVEMENT, QUALIFICATIONS -> {
                List<String> list = new ArrayList<>();
                doReadCollection(dis, () -> list.add(dis.readUTF()));
                return new ListSection(list);
            }
            case EXPERIENCE, EDUCATION -> {
                List<Company> companies = new ArrayList<>();
                doReadCollection(dis, () ->
                        {
                            Company company = new Company(dis.readUTF(), new URL(dis.readUTF()));
                            List<Period> periods = new ArrayList<>();
                            doReadCollection(dis, () -> periods.add(new Period(doReadLocalDate(dis), doReadLocalDate(dis), dis.readUTF(), dis.readUTF())));
                            company.setPeriods(periods);
                            companies.add(company);
                        }
                );
                return new CompanySection(companies);
            }
            default -> throw new IOException("Reading Error");
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

    private interface WriteWithException<E> {
        void write(E item) throws IOException;
    }

    private interface ElementAdder {
        void add() throws IOException;
    }

    private <E> void doWriteCollection(DataOutputStream dos, Collection<E> collection, WriteWithException<E> wwe) throws IOException {
        dos.writeInt(collection.size());
        for (E item : collection) {
            wwe.write(item);
        }
    }

    private void doReadCollection(DataInputStream dis, ElementAdder ea) throws IOException {
        int size = dis.readInt();
        for (int i = 0; i < size; i++) {
            ea.add();
        }
    }
}
