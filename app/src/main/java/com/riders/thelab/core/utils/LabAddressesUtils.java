package com.riders.thelab.core.utils;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class LabAddressesUtils {

    private LabAddressesUtils() {
    }

    public static Address getDeviceAddress(final Geocoder geocoder, final Location location) {
        Timber.i("getDeviceAddress");
        try {
            List<Address> addresses =
                    geocoder.getFromLocation(
                            location.getLatitude(),
                            location.getLongitude(),
                            1);
            Timber.e("addresses : %s", addresses);

            //get the address
            return addresses.get(0);
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }

        return null;
    }


    public static Single<List<Address>> getRXAddress(
            final Geocoder geoCoder,
            final double latitude,
            final double longitude
    ) {
        return new Single<List<Address>>() {

            @Override
            protected void subscribeActual(@NotNull SingleObserver<? super List<Address>> observer) {
                List<Address> addressList = new ArrayList<>();

                try {
                    addressList = geoCoder.getFromLocation(latitude, longitude, 1);

                } catch (Exception exception) {

                }
                if (addressList.isEmpty()) {
                    observer.onSuccess(addressList);
                } else {
                    observer.onError(new Throwable());
                }
            }

        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static String buildAddress(final Address address) {
        String finalCity = "";
        StringBuilder addressStringBuilder = new StringBuilder();

        String street = address.getFeatureName() + ", " + address.getThoroughfare();
        String locality = address.getLocality();
        String postalCode = address.getPostalCode();
        String departmentName = address.getSubAdminArea();
        String regionName = address.getAdminArea();
        String countryName = address.getCountryName();

        addressStringBuilder
                .append(street).append(" - ")
                .append(locality).append(" - ")
                .append(postalCode).append(" - ")
                .append(departmentName).append(" - ")
                .append(regionName).append(" - ")
                .append(countryName);

        Timber.d(addressStringBuilder.toString());

        return locality;
    }

}
