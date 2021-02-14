package com.riders.thelab.core.bus;

import android.location.Location;

public class LocationFetchedEvent {

    private final Location location;

    public LocationFetchedEvent(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }
}
