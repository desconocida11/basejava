<%@ page import="com.basejava.webapp.model.ContactType" %>
<%@ page import="com.basejava.webapp.model.SectionType" %>
<%@ page import="com.basejava.webapp.model.BulletedListSection" %>
<%@ page import="java.util.Collections" %>
<%@ page import="com.basejava.webapp.model.OrganizationSection" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <script rel="script" src="js/events.js"></script>
    <script>
        var rows = 0;
    </script>
    <jsp:useBean id="resume" type="com.basejava.webapp.model.Resume" scope="request"/>
    <title>Edit resume ${resume.fullName}</title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<section>
    <form method="post" action="resume" enctype="application/x-www-form-urlencoded">
        <input type="hidden" name="uuid" value="${resume.uuid}">
        <dl>
            <dt>Name:</dt>
            <dd><label>
                <input type="text" name="fullName" size="50" value="${resume.fullName}" required>
            </label></dd>
        </dl>
        <h3>Contacts:</h3>
        <c:forEach var="contact" items="<%=ContactType.values()%>">
            <dl>
                <dt>${contact.type}</dt>
                <dd><label>
                    <input type="text" name="${contact.name()}" size="30" value="${resume.getContactByType(contact)}">
                </label>
                </dd>
            </dl>
        </c:forEach>
        <hr>

        <c:forEach var="section" items="<%=SectionType.values()%>">
            <jsp:useBean id="section" type="com.basejava.webapp.model.SectionType"/>
            <c:set var="key" value="${section.name()}"/>
            <c:choose>
                <c:when test="${(key eq 'OBJECTIVE') || (key eq 'PERSONAL')}">
                    <dl>
                        <dt>${section.title}</dt>
                        <dd><label>
                            <input type="text" name="${key}" size="200" height="30"
                                   value="${resume.getSectionByType(section)}">
                        </label>
                        </dd>
                    </dl>
                </c:when>
                <c:when test="${(key eq 'ACHIEVEMENT') || (key eq 'QUALIFICATIONS')}">
                    <dl>
                        <dt style="text-emphasis: blueviolet">${section.title}</dt>
                        <dd><label>
                            <textarea name="${key}" rows="10"
                                      cols="150"><%=String.join("\n", resume.getSectionByType(section) == null ? Collections.EMPTY_LIST : ((BulletedListSection) resume.getSectionByType(section)).getValue())%></textarea>
                        </label>
                        </dd>
                    </dl>
                </c:when>
                <c:otherwise>
                    <h3>${section.title}</h3>
                    <c:set var="organizations"
                           value="<%= resume.getSectionByType(section) != null ? ((OrganizationSection) resume.getSectionByType(section)).getValue() : Collections.EMPTY_LIST%>"/>
                    <c:set var="counter" value="0" scope="request"/>

                    <table id="organization${key}" width="100%" border="1" cellpadding="2" cellspacing="0">
                        <tr>
                            <th>
                                Организация
                            </th>
                            <th>
                                Сайт
                            </th>
                            <th>
                                Периоды (с, по, описание)
                            </th>
                        </tr>
                        <c:forEach items="${organizations}" var="organization">
                            <jsp:useBean id="organization" type="com.basejava.webapp.model.Organization"/>
                            <tr>
                                <c:set var="counter" value="${counter + 1}" scope="request"/>
                                <script>
                                    rows = rows + 1;
                                </script>
                                <td>
                                    <label>
                                        <input type="text" name="${key}.${counter}"
                                               value="${organization.organization.name}">
                                    </label>
                                </td>
                                <td>
                                    <label>
                                        <input type="url" name="${key}.${counter}"
                                               value="${organization.organization.url}">
                                    </label>
                                </td>

                                <td>
                                    <table id="periods${key}.${counter}">
                                        <c:forEach items="${organization.periods}" var="period">
                                            <jsp:useBean id="period"
                                                         type="com.basejava.webapp.model.Organization.Experience"/>
                                            <tr>
                                                <td width="10%">
                                                    <label>
                                                        <input type="month" name="${key}.${counter}"
                                                               value="<%=period.getStartDate().getYear()%>-<%=String.format("%02d", period.getStartDate().getMonthValue())%>">
                                                    </label>
                                                </td>
                                                <td width="10%">
                                                    <label>
                                                        <input type="month" name="${key}.${counter}"
                                                               value="<%=period.getEndDate().getYear()%>-<%=String.format("%02d", period.getEndDate().getMonthValue())%>">
                                                    </label>
                                                </td>
                                                <td width="60%">
                                                    <label>
                                                        <textarea name="${key}.${counter}" rows="2"
                                                                  cols="50"><%=period.getTitle()%></textarea>
                                                    </label>
                                                </td>
                                                <td width="10%">
                                                    <input type="button" id="delPos" value="Удалить поз."
                                                           onclick="SomeDeleteRowFunction(this)">
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </table>

                                    <button type="button" id="addPos"
                                            onclick="addPosition('periods${key}.${counter}', '${key}.${counter}');">
                                        Добавить позицию
                                    </button>
                                </td>

                                <td>
                                    <input type="image" id="delOrg" src="img/delete.png" alt="Удалить организацию"
                                           onclick="SomeDeleteRowFunction(this)">
                                </td>
                            </tr>
                        </c:forEach>
                    </table>

                    <button type="button" id="addOrg"
                            onclick="rows = rows + 1;addOrganization('organization${key}',  '${key}', rows);">
                        Добавить организацию
                    </button>
                </c:otherwise>
            </c:choose>
        </c:forEach>
        <br/>
        <hr>
        <button type="submit">Save</button>
        <button type="button" onclick="window.history.back()">Cancel</button>
    </form>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>