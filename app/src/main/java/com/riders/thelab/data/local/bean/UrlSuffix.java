package com.riders.thelab.data.local.bean;

public enum UrlSuffix {

    MOVIES_URL_SUFFIX("/json/glide.json");

    private final String value;

    UrlSuffix(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
