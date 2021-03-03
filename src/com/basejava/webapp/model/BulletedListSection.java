package com.basejava.webapp.model;

import java.util.List;
import java.util.Objects;

public class BulletedListSection extends AbstractSection {

    private final List<String> value;

    public BulletedListSection(List<String> value) {
        Objects.requireNonNull(value, "BulletedList value must not be null");
        this.value = value;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String s : value) {
            sb.append(s).append(", \n");
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BulletedListSection that = (BulletedListSection) o;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
