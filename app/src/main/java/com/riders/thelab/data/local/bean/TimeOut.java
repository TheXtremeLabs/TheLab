package com.riders.thelab.data.local.bean;

public enum TimeOut {

    TIME_OUT_READ(60),
    TIME_OUT_CONNECTION(60);

    private final int value;

    TimeOut(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
