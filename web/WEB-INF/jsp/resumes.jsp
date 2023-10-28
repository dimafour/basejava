<%@ taglib prefix="c" uri="jakarta.tags.core" %>
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
<jsp:include page="fragments/header.jsp"/>
<h3>Все резюме:</h3>
<section>
    <table>
        <jsp:useBean id="resumes" scope="request" type="java.util.List"/>
        <c:if test="${!resumes.isEmpty()}">
        <tr>
            <th>Full Name</th>
            <th>Email</th>
            <th></th>
            <th></th>
        </tr>
        <c:forEach items="${resumes}" var="resume">
            <jsp:useBean id="resume" type="com.urise.webapp.model.Resume"/>
            <tr>
                <td> <a href="resume?uuid=${resume.uuid}&action=view">${resume.fullName}</a></td>
                <td>${resume.contacts.get(ContactType.MAIL)}</td>
                <td><a href="resume?uuid=${resume.uuid}&action=delete"><img src="img/delete.png" height="30"
                                                                            alt="delete"></a></td>
                <td><a href="resume?uuid=${resume.uuid}&action=edit"><img src="img/edit.png" height="30" alt="edit"></a>
                </td>
            </tr>
        </c:forEach>
        </c:if>
        <c:if test="${resumes.isEmpty()}">
            <tr><td> <c:out value="Список резюме пуст"/></td></tr>
        </c:if>
    </table>
    <br/>
    <a href="resume?uuid=${resume.uuid}&action=add"><img src="img/add.png" height="30" alt="add"></a><br/><br/>
    <a href="resume?uuid=&action=clear"><img src="img/clear.png" height="30" alt="clear"></a>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
