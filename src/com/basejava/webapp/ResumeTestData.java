package com.basejava.webapp;

import com.basejava.webapp.model.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ResumeTestData {
    private static final List<String> ACHIEVEMENT = Arrays.asList(
            "С 2013 года: разработка проектов \"Разработка Web приложения\",\"Java Enterprise\", \"Многомодульный maven. Многопоточность. XML (JAXB/StAX). Веб сервисы (JAX-RS/SOAP). Удаленное взаимодействие (JMS/AKKA)\". Организация онлайн стажировок и ведение проектов. Более 1000 выпускников.",
            "Реализация двухфакторной аутентификации для онлайн платформы управления проектами Wrike. Интеграция с Twilio, DuoSecurity, Google Authenticator, Jira, Zendesk.",
            "Налаживание процесса разработки и непрерывной интеграции ERP системы River BPM. Интеграция с 1С, Bonita BPM, CMIS, LDAP. Разработка приложения управления окружением на стеке: Scala/Play/Anorm/JQuery. Разработка SSO аутентификации и авторизации различных ERP модулей, интеграция CIFS/SMB java сервера.",
            "Реализация c нуля Rich Internet Application приложения на стеке технологий JPA, Spring, Spring-MVC, GWT, ExtGWT (GXT), Commet, HTML5, Highstock для алгоритмического трейдинга.",
            "Создание JavaEE фреймворка для отказоустойчивого взаимодействия слабо-связанных сервисов (SOA-base архитектура, JAX-WS, JMS, AS Glassfish). Сбор статистики сервисов и информации о состоянии через систему мониторинга Nagios. Реализация онлайн клиента для администрирования и мониторинга системы по JMX (Jython/ Django).",
            "Реализация протоколов по приему платежей всех основных платежных системы России (Cyberplat, Eport, Chronopay, Сбербанк), Белоруcсии(Erip, Osmp) и Никарагуа.");

    private static final List<String> QUALIFICATIONS = Arrays.asList(
            "JEE AS: GlassFish (v2.1, v3), OC4J, JBoss, Tomcat, Jetty, WebLogic, WSO2",
            "Version control: Subversion, Git, Mercury, ClearCase, Perforce",
            "DB: PostgreSQL(наследование, pgplsql, PL/Python), Redis (Jedis), H2, Oracle,",
            "MySQL, SQLite, MS SQL, HSQLDB",
            "Languages: Java, Scala, Python/Jython/PL-Python, JavaScript, Groovy,",
            "XML/XSD/XSLT, SQL, C/C++, Unix shell scripts,",
            "Java Frameworks: Java 8 (Time API, Streams), Guava, Java Executor, MyBatis, Spring (MVC, Security, Data, Clouds, Boot), JPA (Hibernate, EclipseLink), Guice, GWT(SmartGWT, ExtGWT/GXT), Vaadin, Jasperreports, Apache Commons, Eclipse SWT, JUnit, Selenium (htmlelements).",
            "Python: Django.",
            "JavaScript: jQuery, ExtJS, Bootstrap.js, underscore.js",
            "Scala: SBT, Play2, Specs2, Anorm, Spray, Akka",
            "Технологии: Servlet, JSP/JSTL, JAX-WS, REST, EJB, RMI, JMS, JavaMail, JAXB, StAX, SAX, DOM, XSLT, MDB, JMX, JDBC, JPA, JNDI, JAAS, SOAP, AJAX, Commet, HTML5, ESB, CMIS, BPMN2, LDAP, OAuth1, OAuth2, JWT.",
            "Инструменты: Maven + plugin development, Gradle, настройка Ngnix,",
            "администрирование Hudson/Jenkins, Ant + custom task, SoapUI, JPublisher, Flyway, Nagios, iReport, OpenCmis, Bonita, pgBouncer.",
            "Отличное знание и опыт применения концепций ООП, SOA, шаблонов проектрирования, архитектурных шаблонов, UML, функционального программирования",
            "Родной русский, английский \"upper intermediate\"");

    private static final String OBJECTIVE = "Ведущий стажировок и корпоративного обучения по Java Web и Enterprise технологиям";

    private static final List<Experience> EXPERIENCE = Arrays.asList(
            new Experience("09/1997", "01/2005", "Alcatel", "Инженер по аппаратному и программному тестированию\n" +
                    "Тестирование, отладка, внедрение ПО цифровой телефонной станции Alcatel 1000 S12 (CHILL, ASM)."),
            new Experience("01/2005", "02/2007", "Siemens AG", "Разработчик ПО. Разработка информационной модели, проектирование интерфейсов, реализация и отладка ПО на мобильной IN платформе Siemens @vantage (Java, Unix)."),
            new Experience("03/2007", "06/2008", "Enkata", "Разработчик ПО\n" +
                    "Реализация клиентской (Eclipse RCP) и серверной (JBoss 4.2, Hibernate 3.0, Tomcat, JMS) частей кластерного J2EE приложения (OLAP, Data mining)."),
            new Experience("06/2008", "12/2010", "Yota", "Ведущий специалист\n" +
                    "Дизайн и имплементация Java EE фреймворка для отдела \"Платежные Системы\" (GlassFish v2.1, v3, OC4J, EJB3, JAX-WS RI 2.1, Servlet 2.4, JSP, JMX, JMS, Maven2). Реализация администрирования, статистики и мониторинга фреймворка. Разработка online JMX клиента (Python/ Jython, Django, ExtJS)"),
            new Experience("12/2010", "04/2012", "Luxoft (Deutsche Bank)", "Ведущий программист. Участие в проекте Deutsche Bank CRM (WebLogic, Hibernate, Spring, Spring MVC, SmartGWT, GWT, Jasper, Oracle). Реализация клиентской и серверной части CRM. Реализация RIA-приложения для администрирования, мониторинга и анализа результатов в области алгоритмического трейдинга. JPA, Spring, Spring-MVC, GWT, ExtGWT (GXT), Highstock, Commet, HTML5."),
            new Experience("04/2012", "10/2014", "RIT Center", "Java архитектор. Организация процесса разработки системы ERP для разных окружений: релизная политика, версионирование, ведение CI (Jenkins), миграция базы (кастомизация Flyway), конфигурирование системы (pgBoucer, Nginx), AAA via SSO. Архитектура БД и серверной части системы. Разработка интергационных сервисов: CMIS, BPMN2, 1C (WebServices), сервисов общего назначения (почта, экспорт в pdf, doc, html). Интеграция Alfresco JLAN для online редактирование из браузера документов MS Office. Maven + plugin development, Ant, Apache Commons, Spring security, Spring MVC, Tomcat,WSO2, xcmis, OpenCmis, Bonita, Python scripting, Unix shell remote scripting via ssh tunnels, PL/Python"),
            new Experience("10/2014", "01/2016", "Wrike", "Старший разработчик (backend). Проектирование и разработка онлайн платформы управления проектами Wrike (Java 8 API, Maven, Spring, MyBatis, Guava, Vaadin, PostgreSQL, Redis). Двухфакторная аутентификация, авторизация по OAuth1, OAuth2, JWT SSO."),
            new Experience("10/2013", "03/2021", "Java Online Projects", "Автор проекта.\n" +
                    "Создание, организация и проведение Java онлайн проектов и стажировок.")
    );

    public static final List<Experience> EDUCATION = Arrays.asList(
            new Experience("09/1984", "06/1987", "Заочная физико-техническая школа при МФТИ", "Закончил с отличием"),
            new Experience("09/1987", "07/1993", "Санкт-Петербургский национальный исследовательский университет информационных технологий, механики и оптики", "Инженер (программист Fortran, C)"),
            new Experience("09/1993", "07/1996", "Санкт-Петербургский национальный исследовательский университет информационных технологий, механики и оптики", "Аспирантура (программист С, С++)"),
            new Experience("09/1997", "03/1998", "Alcatel", "6 месяцев обучения цифровым телефонным сетям (Москва)"),
            new Experience("01/2005", "04/2005", "Siemens AG", "3 месяца обучения мобильным IN сетям (Берлин)"),
            new Experience("03/2011", "04/2011", "Luxoft", "Курс \"Объектно-ориентированный анализ ИС. Концептуальное моделирование на UML.\""),
            new Experience("03/2013", "05/2013", "Coursera", "\"Functional Programming Principles in Scala\" by Martin Odersky")
    );

    private static AbstractSection buildBulletedList(List<String> content) {
        AbstractSection bls = new BulletedListSection();
        for (String s : content) {
            bls.addValue(s);
        }
        return bls;
    }

    private static AbstractSection buildDatedSection(List<Experience> experiences) {
        AbstractSection datedSection = new Organization();
        for (Experience e : experiences) {
            datedSection.addValue(e);
        }
        return datedSection;
    }

    public static void main(String[] args) {

        final Resume resume = new Resume("Григорий Кислин");

        resume.addContact(ContactType.PHONE, "+7(921)855-0482");
        resume.addContact(ContactType.SKYPE, "grigory.kislin");
        resume.addContact(ContactType.EMAIL, "gkislin@yandex.ru");
        resume.addContact(ContactType.LINKEDIN, "https://www.linkedin.com/in/gkislin");
        resume.addContact(ContactType.GITHUB, "https://github.com/gkislin");
        resume.addContact(ContactType.STACKOVERFLOW, "https://stackoverflow.com/users/548473");
        resume.addContact(ContactType.SITE, "http://gkislin.ru/");

        resume.addSection(SectionType.PERSONAL,
                new SingleLineSection("Аналитический склад ума, сильная логика, креативность, инициативность. Пурист кода и архитектуры."));

        resume.addSection(SectionType.OBJECTIVE,
                new SingleLineSection(OBJECTIVE));
        resume.addSection(SectionType.ACHIEVEMENT, buildBulletedList(ACHIEVEMENT));
        resume.addSection(SectionType.QUALIFICATIONS, buildBulletedList(QUALIFICATIONS));

        resume.addSection(SectionType.EXPERIENCE, buildDatedSection(EXPERIENCE));

        resume.addSection(SectionType.EDUCATION, buildDatedSection(EDUCATION));

        for (Map.Entry<ContactType, String> entry : resume.getAllContacts().entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }

        for (Map.Entry<SectionType, AbstractSection> entry : resume.getAllSections().entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}
