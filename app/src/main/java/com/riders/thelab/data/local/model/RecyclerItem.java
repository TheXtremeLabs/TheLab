package com.riders.thelab.data.local.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class RecyclerItem {

    private String name;

    public RecyclerItem(String name) {
        this.name = name;
    }
}
