package com.riders.thelab.core.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

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

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import lombok.SneakyThrows;
import timber.log.Timber;

public class LabLocationManager extends Service
        implements LocationListener {

    private Activity mActivity;
    private final Context mContext;

    // flag for GPS status
    boolean isGPSEnabled = false;

    // flag for network status
    boolean isNetworkEnabled = false;

    // flag for GPS status
    boolean canGetLocation = false;

    Location location = null; // location
    double latitude; // latitude
    double longitude; // longitude

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

    // Declaring a Location Manager
    protected LocationManager locationManager;
    private LocationListener mLocationListener;


    // Constructors
    public LabLocationManager(Context context) {
        this.mContext = context;
    }

    public LabLocationManager(Context context, Activity activity) {
        this.mContext = context;
        this.mActivity = activity;
        mLocationListener = this;
        getLocation();
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }


    public Location getLocation() {

        locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);

        // getting GPS status
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        // getting network status
        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isGPSEnabled && !isNetworkEnabled) {
            // no network provider is enabled
            Timber.e("no network provider is enabled");
        } else {
            this.canGetLocation = true;

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


                            try {

                                if (isNetworkEnabled) {
                                        /*if (Build.VERSION_CODES.M < Build.VERSION.SDK_INT) {
                                            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                                                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                                // TODO: Consider calling
                                                //    ActivityCompat#requestPermissions
                                                // here to request the missing permissions, and then overriding
                                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                                //                                          int[] grantResults)
                                                // to handle the case where the user grants the permission. See the documentation
                                                // for ActivityCompat#requestPermissions for more details.
                                                return null;
                                            }
                                        }*/

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


                                // if GPS Enabled get lat/long using GPS Services
                                if (isGPSEnabled) {
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

                                EventBus.getDefault().post(new LocationFetchedEvent(location));

                            } catch (Exception e) {
                                e.printStackTrace();
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

    /**
     * Stop using GPS listener Calling this function will stop using GPS in your
     * app
     */
    public void stopUsingGPS() {
        if (locationManager != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
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
        return this.canGetLocation;
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
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
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

    public static Single<String> getDeviceLocationWithRX(final Location location, final Context context) {

        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        //get the address
        Geocoder geoCoder = new Geocoder(context, Locale.getDefault());
        StringBuilder builderAddr = new StringBuilder();
        StringBuilder builderCity = new StringBuilder();

        return new Single<String>() {
            @Override
            protected void subscribeActual(SingleObserver<? super String> observer) {
                getRXAddress(geoCoder, latitude, longitude)
                        .subscribe(new DisposableSingleObserver<List<Address>>() {
                            @Override
                            public void onSuccess(@NonNull List<Address> addresses) {
                                List<Address> address = addresses;

                                for (Address element : address) {
                                    Timber.e("element : %s", element.toString());
                                }

                                int maxLines = address.size();

                                for (int i = 0; i < maxLines; i++) {
                                    Timber.e("1 -- : %s", address.get(0).getAddressLine(i));
                                    Timber.e("2 -- : %s", address.get(0).getLocality());

                                    String addressStr = address.get(0).getAddressLine(i);
                                    String cityStr = address.get(0).getLocality();

                                    //This will display the final address.
                                    // Log.e(TAG, "Address : " + addressStr + " | " + "City : " + cityStr);

                                    builderAddr.append(addressStr);
                                    builderAddr.append(" ");

                                    builderCity.append(cityStr);
                                    builderCity.append(" ");
                                }

                                String finalAddress = builderAddr.toString(); //This is the complete address.
                                String finalCity = builderCity.toString(); //This is the complete address.

                                if (!finalCity.isEmpty()) {
                                    observer.onSuccess(finalCity);
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
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
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
}
