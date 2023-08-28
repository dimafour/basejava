package com.urise.webapp.model;

import java.util.List;

public class ListSection extends Section {
    private final List<String> fields;
    public ListSection(List<String> field) {
        this.fields = field;
    }

    public List<String> getFields() {
        return fields;
    }



    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("\n");
        for (String field : fields) {
            sb.append("*");
            sb.append(field);
            sb.append("\n");
        }
        return sb.toString();
    }
}
