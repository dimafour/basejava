package com.urise.webapp.model;

import java.io.Serial;
import java.util.List;
import java.util.Objects;

public class CompanySection extends Section {
    @Serial
    private static final long serialVersionUID = 1L;
    private List<Company> companyList;

    public CompanySection() {}

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompanySection that = (CompanySection) o;
        return Objects.equals(companyList, that.companyList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(companyList);
    }
}
