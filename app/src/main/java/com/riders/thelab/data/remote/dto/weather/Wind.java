package com.riders.thelab.data.remote.dto.weather;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Wind {

    @Expose
    double speed;

    @SerializedName("deg")
    int degree;
}
