package com.urise.webapp.model;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.urise.webapp.model.ContactType.*;
import static com.urise.webapp.model.SectionType.*;

public class ResumeTestData {


    public static void main(String[] args) throws MalformedURLException {
        Resume r = createResume("uuid0", "Григорий Кислин");
        System.out.println(r.allInformation());
    }

    public static Resume createResume(String uuid, String fullName) throws MalformedURLException {
        Resume r = new Resume(uuid, fullName);
        r.addContact(MOBILE, "+7(921) 855-0482");
        r.addContact(LINKEDIN, "https://www.linkedin.com/in/gkislin");
        r.addContact(GITHUB, "https://github.com/gkislin");
        r.addContact(HOMEPAGE, "http://gkislin.ru/");
        r.addContact(STACKOVERFLOW, "https://stackoverflow.com/users/548473/grigory-kislin");
        r.addContact(SKYPE, "grigory.kislin");
        r.addContact(MAIL, "gkislin@yandex.ru");

        TextSection objective = new TextSection("Ведущий стажировок и корпоративного обучения по Java Web и Enterprise технологиям");
        TextSection personal = new TextSection("Аналитический склад ума, сильная логика, креативность, инициативность. Пурист кода и архитектуры.");

        ArrayList<String> info = new ArrayList<>();
        info.add("Организация команды и успешная реализация Java проектов для сторонних заказчиков: приложения автопарк на стеке Spring Cloud/микросервисы, система мониторинга показателей спортсменов на Spring Boot, участие в проекте МЭШ на Play-2, многомодульный Spring Boot + Vaadin проект для комплексных DIY смет");
        info.add("С 2013 года: разработка проектов \"Разработка Web приложения\",\"Java Enterprise\", \"Многомодульный maven. Многопоточность. XML (JAXB/StAX). Веб сервисы (JAX-RS/SOAP). Удаленное взаимодействие (JMS/AKKA)\". Организация онлайн стажировок и ведение проектов. Более 3500 выпускников.");
        info.add("Реализация двухфакторной аутентификации для онлайн платформы управления проектами Wrike. Интеграция с Twilio, DuoSecurity, Google Authenticator, Jira, Zendesk.");
        info.add("Налаживание процесса разработки и непрерывной интеграции ERP системы River BPM. Интеграция с 1С, Bonita BPM, CMIS, LDAP. Разработка приложения управления окружением на стеке: Scala/Play/Anorm/JQuery. Разработка SSO аутентификации и авторизации различных ERP модулей, интеграция CIFS/SMB java сервера.");
        info.add("Реализация c нуля Rich Internet Application приложения на стеке технологий JPA, Spring, Spring-MVC, GWT, ExtGWT (GXT), Commet, HTML5, Highstock для алгоритмического трейдинга.");
        info.add("Создание JavaEE фреймворка для отказоустойчивого взаимодействия слабо-связанных сервисов (SOA-base архитектура, JAX-WS, JMS, AS Glassfish). Сбор статистики сервисов и информации о состоянии через систему мониторинга Nagios. Реализация онлайн клиента для администрирования и мониторинга системы по JMX (Jython/ Django).");
        info.add("Реализация протоколов по приему платежей всех основных платежных системы России (Cyberplat, Eport, Chronopay, Сбербанк), Белоруcсии(Erip, Osmp) и Никарагуа.");

        ListSection achievement = new ListSection(info);

        ArrayList<String> info2 = new ArrayList<>();
        info2.add("JEE AS: GlassFish (v2.1, v3), OC4J, JBoss, Tomcat, Jetty, WebLogic, WSO2");
        info2.add("Version control: Subversion, Git, Mercury, ClearCase, Perforce");
        info2.add("DB: PostgreSQL(наследование, pgplsql, PL/Python), Redis (Jedis), H2, Oracle, MySQL, SQLite, MS SQL, HSQLDB");
        info2.add("Languages: Java, Scala, Python/Jython/PL-Python, JavaScript, Groovy");
        info2.add("XML/XSD/XSLT, SQL, C/C++, Unix shell scripts");
        info2.add("Java Frameworks: Java 8 (Time API, Streams), Guava, Java Executor, MyBatis, Spring (MVC, Security, Data, Clouds, Boot), JPA (Hibernate, EclipseLink), Guice, GWT(SmartGWT, ExtGWT/GXT), Vaadin, Jasperreports, Apache Commons, Eclipse SWT, JUnit, Selenium (htmlelements).");
        info2.add("Python: Django.");
        info2.add("JavaScript: jQuery, ExtJS, Bootstrap.js, underscore.js");
        info2.add("Scala: SBT, Play2, Specs2, Anorm, Spray, Akka");
        info2.add("Технологии: Servlet, JSP/JSTL, JAX-WS, REST, EJB, RMI, JMS, JavaMail, JAXB, StAX, SAX, DOM, XSLT, MDB, JMX, JDBC, JPA, JNDI, JAAS, SOAP, AJAX, Commet, HTML5, ESB, CMIS, BPMN2, LDAP, OAuth1, OAuth2, JWT.");
        info2.add("Инструменты: Maven + plugin development, Gradle, настройка Ngnix");
        info2.add("администрирование Hudson/Jenkins, Ant + custom task, SoapUI, JPublisher, Flyway, Nagios, iReport, OpenCmis, Bonita, pgBouncer");
        info2.add("Отличное знание и опыт применения концепций ООП, SOA, шаблонов проектрирования, архитектурных шаблонов, UML, функционального программирования");
        info2.add("Родной русский, английский \"upper intermediate\"");

        ListSection qualification = new ListSection(info2);

        r.addSectionContent(QUALIFICATIONS, qualification);
        r.addSectionContent(OBJECTIVE, objective);
        r.addSectionContent(PERSONAL, personal);
        r.addSectionContent(ACHIEVEMENT, achievement);
//
//        Company jop = new Company("Java Online Projects",
//                new URL("http://javaops.ru/"),
//                new Period(LocalDate.of(2013, 10, 1), LocalDate.of(2023, 8, 30), "Автор проекта.", "Создание, организация и проведение Java онлайн проектов и стажировок."));
//        Company wrike = new Company("Wrike",
//                new URL("https://www.wrike.com/"),
//                new Period(LocalDate.of(2014, 10, 1), LocalDate.of(2016, 1, 30), "Старший разработчик (backend)", "Проектирование и разработка онлайн платформы управления проектами Wrike (Java 8 API, Maven, Spring, MyBatis, Guava, Vaadin, PostgreSQL, Redis). Двухфакторная аутентификация, авторизация по OAuth1, OAuth2, JWT SSO."));
//        List<Company> info3 = new ArrayList<>();
//        info3.add(jop);
//        info3.add(wrike);
//        CompanySection experience = new CompanySection(info3);
//        r.addSectionContent(EXPERIENCE, experience);
//
//
//        Company coursera = new Company("Coursera",
//                new URL("https://www.coursera.org/learn/progfun1"),
//                new Period(LocalDate.of(2013, 3, 1), LocalDate.of(2013, 5, 30), "", "'Functional Programming Principles in Scala' by Martin Odersky"));
//        Company luxoft = new Company("Luxoft",
//                new URL("http://www.luxoft-training.ru/training/catalog/course.html?ID=22366"),
//                new Period(LocalDate.of(2011, 3, 1), LocalDate.of(2011, 4, 30), "", "Курс 'Объектно-ориентированный анализ ИС. Концептуальное моделирование на UML.'"));
//        Company siemens = new Company("Siemens AG",
//                new URL("http://www.siemens.ru/"),
//                new Period(LocalDate.of(2005, 1, 1), LocalDate.of(2013, 4, 30), "", "3 месяца обучения мобильным IN сетям (Берлин)"));
//        Company alcatel = new Company("Alcatel",
//                new URL("http://www.alcatel.ru/"),
//                new Period(LocalDate.of(1997, 9, 1), LocalDate.of(1998, 3, 30), "", "6 месяцев обучения цифровым телефонным сетям (Москва)"));
//        Company itmo = new Company("Санкт-Петербургский национальный исследовательский университет информационных технологий, механики и оптики",
//                new URL("http://www.ifmo.ru/"),
//                new Period(LocalDate.of(1993, 9, 1), LocalDate.of(1996, 7, 30), "Аспирантура", "программист С, С++"),
//                new Period(LocalDate.of(1987, 9, 1), LocalDate.of(1993, 7, 30), "Инженер", "программист Fortran, C"));
//
//        List<Company> info4 = new ArrayList<>();
//        info4.add(coursera);
//        info4.add(luxoft);
//        info4.add(siemens);
//        info4.add(alcatel);
//        info4.add(itmo);
//        CompanySection education = new CompanySection(info4);
//        r.addSectionContent(EDUCATION, education);
        return r;

    }
}
