package com.basejava.webapp.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Experience {

    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");

    private final LocalDate startDate;
    private final LocalDate endDate;
    private final String organization;
    private final String value;

    public Experience (String startDate, String endDate, String organization, String value) {
        this(LocalDate.parse("01/" + startDate, formatter), LocalDate.parse("01/" + endDate, formatter), organization, value);
    }

    public Experience(LocalDate startDate, LocalDate endDate, String organization, String value) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.organization = organization;
        this.value = value;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    private boolean isPeriodValid(LocalDate startDate, LocalDate endDate) {
        return endDate.isAfter(startDate);
    }

    @Override
    public String toString() {
        return startDate.format(DateTimeFormatter.ofPattern("MM/yyyy")) + " - " + endDate.format(DateTimeFormatter.ofPattern("MM/yyyy")) + ", " + organization +
                ", " + value ;
    }
}
