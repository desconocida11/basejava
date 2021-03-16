package com.basejava.webapp.model;

import java.util.Objects;

public class SingleLineSection extends AbstractSection {

    private String value;

    public SingleLineSection(String value) {
        Objects.requireNonNull(value, "SingleLine value must not be null");
        this.value = value;
    }

    public SingleLineSection() {
    }

    public String getValue() {
        return value;
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
