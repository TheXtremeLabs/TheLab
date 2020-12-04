package com.riders.thelab.core.utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import java.io.IOException;
import java.util.List;

import timber.log.Timber;

public class LabLocationManager {


    private LabLocationManager() {
    }


    public static String getDeviceLocationToString(final Geocoder geocoder, final Location location, final Context context) {

        Timber.i("getDeviceLocationToString");

        String finalAddress = ""; //This is the complete address.
        String finalCity = ""; //This is the complete address.

        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        //get the address
        StringBuilder builderAddr = new StringBuilder();
        StringBuilder builderCity = new StringBuilder();

        try {
            List<Address> address = geocoder.getFromLocation(latitude, longitude, 1);
            Timber.e("addresses : %s", address);
            int maxLines = address.get(0).getMaxAddressLineIndex();

            for (int i = 0; i < maxLines; i++) {
                String addressStr = address.get(0).getAddressLine(i);
                Timber.d("addressStr : %s", addressStr);

                String cityStr = address.get(0).getLocality();
                Timber.d("cityStr : %s", cityStr);

                builderAddr.append(addressStr);
                builderAddr.append(" ");

                builderCity.append(cityStr);
                builderCity.append(" ");
            }

            finalAddress = builderAddr.toString(); //This is the complete address.
            finalCity = builderCity.toString(); //This is the complete address.

            Timber.e("Adresse : " + finalAddress + " | " + "City : " + finalCity); //This will display the final address.

        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }

        return finalAddress;
    }

}
