<%@ page import="com.urise.webapp.model.ContactType" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <jsp:useBean id="resume" type="com.urise.webapp.model.Resume" scope="request"/>
    <title>Резюме ${resume.fullName}</title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<section>
    <form method="post" id="form" onsubmit="validateForm()" action="resume" enctype="application/x-www-form-urlencoded">
        <script>
            function validateForm() {
                if (document.getElementById("name").value.trim() === "") {
                    alert("Имя не может быть пустым. Изменения не будут сохранены");
                }
            }
        </script>
        <input type="hidden" name="uuid" value="${resume.uuid}">
        <dl>
            <dt><h3>Имя:</h3></dt>
            <dd>
                <label>
                    <input type="text" name="fullName" id="name" size=50 value="${resume.fullName}">
                </label>
            </dd>
        </dl>
        <h3>Контакты:</h3>
        <c:forEach var="type" items="<%=ContactType.values()%>">
            <dl>
                <dt><h4>${type.title}</h4></dt>
                <dd>
                    <label>
                        <input type="text" name="${type.name()}" size=50 value="${resume.contacts.get(type)}">
                    </label>
                </dd>
            </dl>
        </c:forEach>
        <button type="submit" id="submit" onclick="history.back()">Сохранить</button>
        <button type="reset" onclick="history.back()">Отмена</button>
    </form>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>