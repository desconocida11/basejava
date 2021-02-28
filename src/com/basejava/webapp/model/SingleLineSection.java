package com.basejava.webapp.model;

import java.util.Objects;

public class SingleLineSection extends AbstractSection {

    private String value;

    public SingleLineSection(String value) {
        this.value = value;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public void addValue(Object value) {
        StringBuilder sb = new StringBuilder();
        sb.append(this.value);
        sb.append(value);
        this.value = sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SingleLineSection that = (SingleLineSection) o;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
