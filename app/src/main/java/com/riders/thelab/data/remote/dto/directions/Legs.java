package com.riders.thelab.data.remote.dto.directions;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Legs {

    @Expose
    ArrayList<Steps> steps;
}
