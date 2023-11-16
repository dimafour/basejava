<%@ page import="com.urise.webapp.model.ContactType" %>
<%@ page import="com.urise.webapp.model.SectionType" %>
<%@ page import="com.urise.webapp.util.HtmlHelper" %>
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
        <input type="hidden" name="uuid" value="${resume.uuid}" required>
        <dl>
            <dt><h3>Имя:</h3></dt>
            <dd>
                <label>
                    <input type="text" name="fullName" id="name" size=50 value="${resume.fullName}">
                </label>
            </dd>
        </dl>
        <h3>Контакты:</h3>
        <c:forEach var="contactType" items="<%=ContactType.values()%>">
            <dl>
                <dt><h4>${contactType.title}</h4></dt>
                <dd>
                    <label>
                        <input type="text" name="${contactType.name()}" size=50
                               value="${resume.contacts.get(contactType)}">
                    </label>
                </dd>
            </dl>
        </c:forEach>
        <c:forEach var="sectionType" items="<%=SectionType.textListValues()%>">
            <dl>
                <dt><h4>${sectionType.title}</h4></dt>
                <dd>
                    <label>
                        <textarea style="width: 1000px; height: 250px;"
                                  name="${sectionType.name()}">${HtmlHelper.toHtmlTextArea(sectionType.name(), resume.sections.get(sectionType))} </textarea>
                    </label>
                </dd>
            </dl>
        </c:forEach>
        <c:forEach var="sectionType" items="<%=SectionType.companyValues()%>">
            <dl>
                <dt><h4>${sectionType.title}</h4></dt>
                <dd>
                    <label>
                        <c:forEach var="company" items="${(resume.sections.get(sectionType)).getCompanyList()}" varStatus="periodCounter">
                            <div class="box"> Организация: <input type="text" name="${sectionType.name()}"
                                                                  value="${company.name}">
                                Ссылка: <input type="text" name="url${sectionType.name()}" value="${company.url}"><br/>
                                <h5>Периоды занятости:</h5>
                                <c:forEach var="period" items="${company.getPeriods()}">
                                    Период: <input type="date"
                                                   name="startDate${sectionType.name()}${periodCounter.index}"
                                                   value="${period.getStartDate()}"> -
                                    <input type="date" name="endDate${sectionType.name()}${periodCounter.index}"
                                           value="${period.getEndDate()}">
                                    Должность:<input type="text" name="title${sectionType.name()}${periodCounter.index}" value="${period.getTitle()}">
                                    Описание :<input type="text" name="description${sectionType.name()}${periodCounter.index}" value="${period.getDescription()}"><br/>
                                </c:forEach>
                            </div>
                        </c:forEach>
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