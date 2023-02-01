package com.fulvio.dailyshop.message.placeholder;

public class Placeholder {

    private final String placeholder;

    private final String value;

    public Placeholder(PlaceholderType type, String value) {
        this.placeholder = type.getId();
        this.value = value;
    }

    public String set(String s) {
        return s.replaceAll(placeholder, value);
    }

}
