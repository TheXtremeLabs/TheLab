package com.riders.thelab.data.local.model.weather;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class WeatherKey {

    @SerializedName("appid")
    @Expose
    public String appID;
}
