package com.urise.webapp.util;

import com.urise.webapp.model.ContactType;

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
            default -> throw new IllegalArgumentException(ct.name() + "is not a contact");
        }
    }

    public static String toLink(String title, String link) {
        return "<a href='" + link + "'>" + title + "</a>";
    }


}
