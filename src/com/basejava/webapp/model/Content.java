package com.basejava.webapp.model;

import java.time.LocalDate;

public class Content {

    private final LocalDate startDate;
    private final LocalDate endDate;
    private final String organization;
    private final String value;

    public Content(LocalDate startDate, LocalDate endDate, String organization, String value) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.organization = organization;
        this.value = value;
    }

    private boolean isPeriodValid(LocalDate startDate, LocalDate endDate) {
        return endDate.isAfter(startDate);
    }

    @Override
    public String toString() {
        return "from " + startDate + " to " + endDate + ", organization='" + organization + '\'' +
                ", value='" + value + '\'';
    }
}
