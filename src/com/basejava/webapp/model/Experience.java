package com.basejava.webapp.model;

import java.util.List;
import java.util.Objects;

public class Experience {

    private final Link organization;
    private final List<OrganizationPeriod> periods;

    public Experience(Link organization, List<OrganizationPeriod> periods) {
        this.organization = organization;
        this.periods = periods;
    }

    public Link getOrganization() {
        return organization;
    }

    public List<OrganizationPeriod> getPeriods() {
        return periods;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Experience that = (Experience) o;
        return organization.equals(that.organization) && periods.equals(that.periods);
    }

    @Override
    public int hashCode() {
        return Objects.hash(organization, periods);
    }

    @Override
    public String toString() {
        return periods.toString() + ": " + organization.toString() + "\n";
    }
}
