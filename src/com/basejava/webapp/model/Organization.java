package com.basejava.webapp.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Organization extends AbstractSection {

    private static final Comparator<Experience> EXPERIENCE_PERIOD_COMPARATOR = Comparator.comparing(Experience::getEndDate, Comparator.reverseOrder());

    private final List<Experience> value = new ArrayList<>();

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public void addValue(Object value) {
        this.value.add((Experience) value);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        List<Experience> sortedValue = value;
        sortedValue.sort(EXPERIENCE_PERIOD_COMPARATOR);
        for (Experience c: sortedValue ) {
            sb.append(c).append(", \n");
        }
        return sb.toString();
    }
}
