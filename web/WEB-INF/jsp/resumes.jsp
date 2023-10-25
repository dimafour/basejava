<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.urise.webapp.model.ContactType" %>
<%@ page contentType="text/html;charset=UTF-8" %>


<html>
<style> table, th, td {
    border: 1px solid black;
} </style>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" type="text/css">
    <title>Resumes</title>
</head>
<body>
<jsp:include page="fragments/header.jsp" />
<h3>Resumes from database:</h3>
<section>
<table>
    <tr>
        <th>Full Name</th>
        <th>Email</th>
    </tr>
    <jsp:useBean id="resumes" scope="request" type="java.util.List"/>
    <c:forEach items="${resumes}" var="resume">
        <jsp:useBean id="resume" type="com.urise.webapp.model.Resume"/>
        <tr>
            <td><a href="resume?uuid=${resume.uuid}">${resume.fullName}</a></td>
            <td>${resume.contacts.get(ContactType.MAIL)}</td>
        </tr>
    </c:forEach>
</table>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>