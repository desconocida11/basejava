<%@ page import="com.basejava.webapp.model.ContactType" %>
<%@ page import="com.basejava.webapp.model.SectionType" %>
<%@ page import="com.basejava.webapp.model.BulletedListSection" %>
<%@ page import="java.util.Collections" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
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
                        <dt>${section.title}</dt>
                        <dd><label>
                            <textarea name="${key}" rows="10" cols="150"><%=String.join("\n", resume.getSectionByType(section) == null ? Collections.EMPTY_LIST : ((BulletedListSection) resume.getSectionByType(section)).getValue())%></textarea>
                        </label>
                        </dd>
                    </dl>
                </c:when>
                <c:otherwise>

                </c:otherwise>
            </c:choose>
        </c:forEach>
        <button type="submit">Save</button>
        <button type="button" onclick="window.history.back()">Cancel</button>
    </form>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>