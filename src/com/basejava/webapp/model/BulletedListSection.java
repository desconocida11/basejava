package com.basejava.webapp.model;

import java.util.ArrayList;
import java.util.List;

public class BulletedListSection extends AbstractSection {

    private final List<String> value = new ArrayList<>();

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public void addValue(Object value) {
        this.value.add((String) value);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String s : value) {
            sb.append(s).append(", \n");
        }
        return sb.toString();
    }
}
