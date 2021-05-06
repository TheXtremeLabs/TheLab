package com.riders.thelab.data.remote.dto.weather;

import com.google.gson.annotations.Expose;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Weather {
    @Expose
    int id;

    @Expose
    String main;

    @Expose
    String description;

    @Expose
    String icon;
}
