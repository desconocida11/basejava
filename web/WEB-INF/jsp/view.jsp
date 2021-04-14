<%@ page import="com.basejava.webapp.model.SectionType" %>
<%@ page import="com.basejava.webapp.model.BulletedListSection" %>
<%@ page import="com.basejava.webapp.model.OrganizationSection" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <jsp:useBean id="resume" type="com.basejava.webapp.model.Resume" scope="request"/>
    <title>Resume ${resume.fullName}</title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<section>
    <h2>${resume.fullName}<a href="resume?uuid=${resume.uuid}&action=edit"><img src="img/pencil.png"
                                                                                alt="edit"></a></h2>
    <p>
        <c:forEach var="contactEntry" items="${resume.allContacts}">
            <jsp:useBean id="contactEntry"
                         type="java.util.Map.Entry<com.basejava.webapp.model.ContactType, java.lang.String>"/>
            <%=contactEntry.getKey().toHtml(contactEntry.getValue())%><br/>
        </c:forEach>
    </p>

    <c:forEach var="section" items="${resume.allSections}">
        <c:set var="key" value="${section.key}"/>
        <c:set var="value" value="${section.value}"/>
        <jsp:useBean id="section"
                     type="java.util.Map.Entry<com.basejava.webapp.model.SectionType,
                     com.basejava.webapp.model.AbstractSection>"/>
        <h2> ${key.title}</h2>
        <c:choose>
            <c:when test="${(key eq 'PERSONAL') || (key eq 'OBJECTIVE')}">
                <%=resume.getSectionByType(section.getKey())%>
            </c:when>
            <c:when test="${(key eq 'ACHIEVEMENT') || (key eq 'QUALIFICATIONS')}">
                <ul>
                    <c:forEach var="entry" items="<%=((BulletedListSection) section.getValue()).getValue()%>">
                        <jsp:useBean id="entry" type="java.lang.String"/>
                        <li><%=entry%>
                        </li>
                    </c:forEach>
                </ul>
            </c:when>

            <c:when test="${(key eq 'EDUCATION') || (key eq 'EXPERIENCE')}">
                <c:forEach var="orgEntry" items="<%=((OrganizationSection) section.getValue()).getValue()%>">
                    <jsp:useBean id="orgEntry" type="com.basejava.webapp.model.Organization"/>
                    <h3>
                        <c:if test="${orgEntry.organization.url != null}">
                            <h3><a href="${orgEntry.organization.url}">${orgEntry.organization.name}</a></h3>
                        </c:if>
                        <c:if test="${orgEntry.organization.url == null}">
                            <h3>${orgEntry.organization.name}</h3>
                        </c:if>
                    </h3>
                    <table>
                        <c:forEach items="${orgEntry.periods}" var="period">
                            <jsp:useBean id="period" type="com.basejava.webapp.model.Organization.Experience"/>
                            <tr>
                                <td style="width:10%;vertical-align:top">
                                        ${period.startDate.monthValue}/${period.startDate.year}
                                    - ${period.endDate.monthValue}/${period.endDate.year}
                                </td>
                                <td>
                                        ${period.title}
                                </td>
                            </tr>
                        </c:forEach>
                    </table>


                </c:forEach>
            </c:when>

            <c:otherwise>
                <%-- Statements which gets executed when all <c:when> tests are false.  --%>
            </c:otherwise>
        </c:choose>


    </c:forEach>

</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>