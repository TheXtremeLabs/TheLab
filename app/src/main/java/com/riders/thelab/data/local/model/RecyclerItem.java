package com.riders.thelab.data.local.model;

import com.riders.thelab.data.local.bean.RecyclerEnum;

import org.parceler.Parcel;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Parcel
public class RecyclerItem {

    String name;

    public RecyclerItem() {
    }

    public RecyclerItem(RecyclerEnum item) {
        this.name = item.getName();
    }
}
