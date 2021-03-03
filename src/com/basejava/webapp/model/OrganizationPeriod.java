package com.basejava.webapp.model;

import java.time.LocalDate;
import java.util.Objects;

public class OrganizationPeriod {

    private final LocalDate startDate;
    private final LocalDate endDate;
    private final String value;

    public OrganizationPeriod(LocalDate startDate, LocalDate endDate, String value) {
        Objects.requireNonNull(startDate, "start date must not be null");
        Objects.requireNonNull(endDate, "end date must not be null");
        if (!isPeriodValid(startDate, endDate)) {
            throw new IllegalArgumentException("start date must be earlier than end date");
        }
        this.startDate = startDate;
        this.endDate = endDate;
        this.value = value;
    }

    private boolean isPeriodValid(LocalDate startDate, LocalDate endDate) {
        return endDate.isAfter(startDate);
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrganizationPeriod that = (OrganizationPeriod) o;
        return startDate.equals(that.startDate) && endDate.equals(that.endDate) && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startDate, endDate, value);
    }

    @Override
    public String toString() {
        if (value == null) {
            return "Period: " + startDate + " -- " + endDate;
        }
        return "Period: " + startDate + " -- " + endDate + ", " + value;
    }
}
