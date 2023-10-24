<%@ page import="com.urise.webapp.model.Resume" %>
<%@ page import="java.util.List" %>
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
        <%
            for (Resume resume: (List<Resume>) request.getAttribute("resumes")) {
                %> <tr>
    <td><a href="resume?uuid=<%=resume.getUuid()%>"> <%=resume.getFullName()%></a></td>
    <td><%=resume.getContacts().get(ContactType.MAIL)%></td>
           <%
            }
           %>
    </tr>
</table>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
