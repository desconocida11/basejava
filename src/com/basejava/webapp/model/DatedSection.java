package com.basejava.webapp.model;

import java.util.ArrayList;
import java.util.List;

public class DatedSection extends AbstractSection {

    private final List<Content> value = new ArrayList<>();

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public void addValue(Object value) {
        this.value.add((Content) value);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Content c: value ) {
            sb.append(c).append(", \n");
        }
        return sb.toString();
    }
}
