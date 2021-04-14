package com.basejava.webapp.model;

public enum ContactType {

    PHONE("Телефон"),
    SKYPE("Skype") {
        @Override
        public String toHtml0(String value) {
            return "<a href='skype:" + value + "'>" + value + "</a>";
        }
    },
    EMAIL("Почта") {
        @Override
        public String toHtml0(String value) {
            return "<a href='mailto:" + value + "'>" + value + "</a>";
        }
    },
    LINKEDIN("LinkedIn"){
        @Override
        public String toHtml0(String value) {
            return "<a href='" + value + "'>" + getType() + "</a>";
        }
    },
    GITHUB("GitHub"){
        @Override
        public String toHtml0(String value) {
            return "<a href='" + value + "'>" + getType() + "</a>";
        }
    },
    STACKOVERFLOW("StackOverflow"){
        @Override
        public String toHtml0(String value) {
            return "<a href='" + value + "'>" + getType() + "</a>";
        }
    },
    SITE("Домашняя страница"){
        @Override
        public String toHtml0(String value) {
            return "<a href='" + value + "'>" + getType() + "</a>";
        }
    };

    private final String type;

    ContactType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    protected String toHtml0(String value) {
        return type + ": " + value;
    }
    public String toHtml(String value) {
        return value==null ? "" : toHtml0(value);
    }
}
