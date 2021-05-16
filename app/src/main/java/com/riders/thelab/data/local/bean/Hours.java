package com.riders.thelab.data.local.bean;

public enum Hours {
    MORNING("06"),
    DAY("12"),
    EVENING("18"),
    NIGHT("22");

    private final String hourValue;

    Hours(String hourValue) {
        this.hourValue = hourValue;
    }

    public String getHourValue() {
        return hourValue;
    }
}
