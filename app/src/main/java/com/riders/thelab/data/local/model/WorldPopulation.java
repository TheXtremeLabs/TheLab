package com.riders.thelab.data.local.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by michael on 12/01/2016.
 */
@Setter
@Getter
@ToString
public class WorldPopulation {

    private String rank;
    private String country;
    private String population;

    public WorldPopulation(String rank, String country, String population) {
        this.rank = rank;
        this.country = country;
        this.population = population;
    }

}
