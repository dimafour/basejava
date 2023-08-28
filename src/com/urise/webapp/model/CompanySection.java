package com.urise.webapp.model;

import java.util.List;

public class CompanySection extends Section {
    private final List<Company> companyList;

    public CompanySection(List<Company> companyList) {
        this.companyList = companyList;
    }

    public List<Company> getCompanyList() {
        return companyList;
    }

    @Override
    public String toString() {
        return companyList.toString();
    }
}
