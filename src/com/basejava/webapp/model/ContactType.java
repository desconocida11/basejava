package com.basejava.webapp.model;

public enum ContactType {

    PHONE("Телефон"),
    SKYPE("Skype"),
    EMAIL("Почта"),
    LINKEDIN("LinkedIn"),
    GITHUB("GitHub"),
    STACKOVERFLOW("StackOverflow"),
    SITE("Домашняя страница");

    private final String type;

    ContactType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
