package com.basejava.webapp.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class Organization implements Serializable {

    private static final long serialVersionUID = 1L;
    private final Link organization;
    private final List<Experience> periods;

    public Organization(Link organization, List<Experience> periods) {
        this.organization = organization;
        this.periods = periods;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Organization that = (Organization) o;
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

    public static class Experience implements Serializable {

        private static final long serialVersionUID = 1L;
        private final LocalDate startDate;
        private final LocalDate endDate;
        private final String title;

        public Experience(LocalDate startDate, LocalDate endDate, String title) {
            Objects.requireNonNull(startDate, "start date must not be null");
            Objects.requireNonNull(endDate, "end date must not be null");
            if (!isPeriodValid(startDate, endDate)) {
                throw new IllegalArgumentException("start date must be earlier than end date");
            }
            this.startDate = startDate;
            this.endDate = endDate;
            this.title = title;
        }

        private boolean isPeriodValid(LocalDate startDate, LocalDate endDate) {
            return endDate.isAfter(startDate);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Experience that = (Experience) o;
            return startDate.equals(that.startDate) && endDate.equals(that.endDate) && Objects.equals(title, that.title);
        }

        @Override
        public int hashCode() {
            return Objects.hash(startDate, endDate, title);
        }

        @Override
        public String toString() {
            if (title == null) {
                return startDate + " - " + endDate;
            }
            return startDate + " - " + endDate + ", " + title;
        }
    }
}
