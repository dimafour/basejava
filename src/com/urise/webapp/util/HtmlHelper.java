package com.urise.webapp.util;

import com.urise.webapp.model.*;

public class HtmlHelper {
    public static String toHtml(ContactType ct, String value) {
        if (value == null) return "";
        switch (ct) {
            case MOBILE -> {
                return value;
            }
            case MAIL -> {
                return toLink(value, "mailto:" + value);
            }
            case SKYPE -> {
                return toLink(value, "skype:" + value);
            }
            case HOMEPAGE, STACKOVERFLOW, GITHUB, LINKEDIN -> {
                return toLink(value, value);
            }
            default -> throw new IllegalArgumentException(ct.name() + " is not a contact");
        }
    }

    public static String toHtml(SectionType st, Section content) {
        StringBuilder sb = new StringBuilder();
        if (content == null) return "";
        switch (st) {
            case ACHIEVEMENT, QUALIFICATIONS -> {
                ListSection ls = (ListSection) content;
                ls.getFields().forEach(field -> sb.append("<br>").append(field));
                return sb.toString();
            }
            case PERSONAL, OBJECTIVE -> {
                TextSection ts = (TextSection) content;
                return ts.getText();
            }
            case EDUCATION, EXPERIENCE -> {
                CompanySection cs = (CompanySection) content;
                cs.getCompanyList().forEach(field -> {
                    sb.append("<br>").append(toLink(field.getName(), field.getUrl().toString())).append("<br>");
                    field.getPeriods().forEach(period -> {
                        sb.append(period.getStartDate()).append(" - ").append(period.getEndDate()).append("<br>");
                        sb.append(period.getTitle()).append("<br>");
                        sb.append(period.getDescription()).append("<br>");
                    });
                });
                return sb.toString();
            }
            default -> throw new IllegalArgumentException(st.name() + " is not a section");
        }
    }

    public static String toHtmlTextArea(SectionType st, Section content) {
        return HtmlHelper.toHtml(st, content).replaceAll("<br>", "\r\n").trim();
    }

    public static String toLink(String title, String link) {
        return "<a href='" + link + "'>" + title + "</a>";
    }

}
