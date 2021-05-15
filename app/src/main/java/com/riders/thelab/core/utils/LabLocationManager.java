package com.riders.thelab.core.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.riders.thelab.core.bus.LocationFetchedEvent;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import lombok.SneakyThrows;
import timber.log.Timber;

public class LabLocationManager extends Service
        implements LocationListener {

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute
    private final Context mContext;
    // Declaring a Location Manager
    protected LocationManager locationManager;
    // flag for GPS status
    boolean isGPSEnabled = false;
    // flag for network status
    boolean isNetworkEnabled = false;
    // flag for GPS status
    boolean canGetLocation = false;
    Location location = null; // location
    double latitude; // latitude
    double longitude; // longitude
    private Activity mActivity;
    private LocationListener mLocationListener;


    // Constructors
    public LabLocationManager(Context context) {
        this.mContext = context;
        locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
    }

    public LabLocationManager(Context context, Activity activity) {
        this.mContext = context;
        this.mActivity = activity;
        mLocationListener = this;
        getLocation();
    }

    public static Location buildTargetLocationObject(final double latitude, final double longitude){
        Location location = new Location("");

        location.setLatitude(latitude);
        location.setLongitude(longitude);

        return location;
    }


    public static String getDeviceLocationToString(final Geocoder geocoder, final Location location, final Context context) {

        Timber.i("getDeviceLocationToString");

        String finalAddress = ""; //This is the complete address.
        String finalCity = ""; //This is the complete address.

        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        //get the address
        StringBuilder addressStringBuilder = new StringBuilder();

        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            Timber.e("addresses : %s", addresses);

            Address address = addresses.get(0);

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

            finalAddress = addressStringBuilder.toString(); //This is the complete address.

            Timber.e("Address : %s", finalAddress); //This will display the final address.

        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }

        return finalAddress;
    }

    public static Single<String> getDeviceLocationWithRX(final Location location, final Context context) {

        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        final String[] finalCity = new String[1]; //This is the complete address.
        //get the address
        Geocoder geoCoder = new Geocoder(context, Locale.getDefault());
        StringBuilder addressStringBuilder = new StringBuilder();

        return new Single<String>() {
            @Override
            protected void subscribeActual(SingleObserver<? super String> observer) {
                getRXAddress(geoCoder, latitude, longitude)
                        .subscribe(new DisposableSingleObserver<List<Address>>() {
                            @Override
                            public void onSuccess(@NonNull List<Address> addresses) {

                                for (Address element : addresses) {
                                    Timber.e("element : %s", element.toString());
                                }

                                Address address = addresses.get(0);

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

                                finalCity[0] = address.getLocality(); //This is the complete address.


                                if (!finalCity[0].isEmpty()) {
                                    observer.onSuccess(finalCity[0]);
                                } else {
                                    Timber.e("value are empty");
                                }
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                observer.onError(e);
                            }
                        });
            }
        };
    }

    static Single<List<Address>> getRXAddress(final Geocoder geoCoder, final double latitude, final double longitude) {
        return new Single<List<Address>>() {
            @SneakyThrows
            @Override
            protected void subscribeActual(@NonNull SingleObserver<? super List<Address>> observer) {
                List<Address> addressList = geoCoder.getFromLocation(latitude, longitude, 1);

                if (!addressList.isEmpty()) {
                    observer.onSuccess(addressList);
                } else {
                    observer.onError(new Throwable());
                }
            }
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    public void setActivity(Activity activity) {
        this.mActivity = activity;
    }

    public void setLocationListener() {
        mLocationListener = this;
    }

    public Location getLocation() {

        locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);

        // Check device API in order to run dexter permission or not
        if (!LabCompatibilityManager.isMarshmallow()) {
            if (!canGetLocation()) {
                // no network provider is enabled
                Timber.e("no network provider is enabled");
            } else {
                try {

                    // if Network Enabled get lat/long using Network
                    if (isNetworkEnabled) {
                        getLocationViaNetwork();
                    }

                    // if GPS Enabled get lat/long using GPS Services
                    if (isGPSEnabled) {
                        getLocationViaGPS();
                    }

                    EventBus.getDefault().post(new LocationFetchedEvent(location));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {

            Dexter.withContext(mActivity)
                    .withPermissions(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                    .withListener(new MultiplePermissionsListener() {
                        @SuppressLint("MissingPermission")
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            // check if all permissions are granted
                            if (report.areAllPermissionsGranted()) {
                                // do you work now
                            }

                            // check for permanent denial of any permission
                            if (report.isAnyPermissionPermanentlyDenied()) {
                                // permission is denied permanently, navigate user to app settings
                            }

                            if (!canGetLocation()) {
                                // no network provider is enabled
                                Timber.e("no network provider is enabled");
                            } else {
                                try {

                                    // if Network Enabled get lat/long using Network
                                    if (isNetworkEnabled) {
                                        getLocationViaNetwork();
                                    }

                                    // if GPS Enabled get lat/long using GPS Services
                                    if (isGPSEnabled) {
                                        getLocationViaGPS();
                                    }

                                    EventBus.getDefault().post(new LocationFetchedEvent(location));

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                            token.continuePermissionRequest();
                        }
                    })
                    .withErrorListener(error -> Toast.makeText(mActivity, "Error occurred! " + error.toString(), Toast.LENGTH_SHORT).show())
                    .onSameThread()
                    .check();
        }

        // return location object
        return location;
    }

    @SuppressLint("MissingPermission")
    private void getLocationViaNetwork() {
        locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                MIN_TIME_BW_UPDATES,
                MIN_DISTANCE_CHANGE_FOR_UPDATES,
                mLocationListener);
        Timber.d("Network Enabled");
        if (locationManager != null) {
            location =
                    locationManager
                            .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void getLocationViaGPS() {
        if (location == null) {
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    MIN_TIME_BW_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES,
                    mLocationListener);
            Timber.d("GPS Enabled");
            if (locationManager != null) {
                location = locationManager
                        .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location != null) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                }
            }
        }
    }

    /**
     * Stop using GPS listener Calling this function will stop using GPS in your
     * app
     */
    public void stopUsingGPS() {
        if (locationManager != null) {
            locationManager.removeUpdates(LabLocationManager.this);
        }
    }

    /**
     * Function to get latitude
     */
    public double getLatitude() {
        if (location != null) {
            latitude = location.getLatitude();
        }

        // return latitude
        return latitude;
    }

    /**
     * Function to get longitude
     */
    public double getLongitude() {
        if (location != null) {
            longitude = location.getLongitude();
        }

        // return longitude
        return longitude;
    }

    /**
     * Function to check GPS/wifi enabled
     *
     * @return boolean
     */
    public boolean canGetLocation() {
        Timber.d("canGetLocation()");

        try {
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
            Timber.e(ex);
        }

        try {
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
            Timber.e(ex);
        }

        this.canGetLocation = isGPSEnabled && isNetworkEnabled;

        return this.canGetLocation;
    }

    public Location getLocationObject() {
        return location;
    }

    /**
     * Function to show settings alert dialog On pressing Settings button will
     * lauch Settings Options
     */
    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");

        // Setting Dialog Message
        alertDialog
                .setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings",
                (dialog, which) -> {
                    Intent intent = new Intent(
                            Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    mContext.startActivity(intent);
                });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel",
                (dialog, which) -> dialog.cancel());

        // Showing Alert Message
        alertDialog.show();
    }

    @Override
    public void onLocationChanged(Location location) {
        Timber.d("onLocationChanged");

        //EventBus.getDefault().post(new LocationFetchedEvent(location));
    }

    @Override
    public void onProviderDisabled(String provider) {
        Timber.e("onProviderDisabled");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Timber.d("onProviderEnabled");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Timber.w("onStatusChanged");
    }
}
