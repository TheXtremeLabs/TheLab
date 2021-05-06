package com.riders.thelab.data.remote.dto.weather;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Rain {

    // Rain volume for the last 1 hour, mm
    @SerializedName("1h")
    double lastHour;

    // Rain volume for the last 3 hour, mm
    @SerializedName("3h")
    double lastThreeHour;
}
