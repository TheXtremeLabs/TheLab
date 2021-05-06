package com.riders.thelab.data.local.bean;

import com.riders.thelab.data.local.model.RecyclerItem;

import java.util.ArrayList;
import java.util.List;

public enum RecyclerEnum {
    ALEX("Alex Rider"),
    KEN("Ken Rider"),
    HALLEY_BECKSMANN("Halley Becksmann"),
    PI_ERRE("PI'ERRE"),
    NESRINE("Nesrine"),
    MIKE_TYSON("Mike Tyson"),
    MICHAEL_B_JORDAN("Michael B. Jordan"),
    CARLOS_ESPOSIIO("Carlos Esposito"),
    STEPH_DANN("Steph Dann"),
    KEN_INVERSION("Ken Iverson");

    private final String name;

    RecyclerEnum(String name) {
        this.name = name;
    }

    public static List<RecyclerItem> getRecyclerItems() {
        List<RecyclerItem> list = new ArrayList<>();

        for (RecyclerEnum item : values()) {
            list.add(new RecyclerItem(item));
        }
        return list;
    }

    public String getName() {
        return name;
    }
}
