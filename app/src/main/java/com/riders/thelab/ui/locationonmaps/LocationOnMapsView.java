package com.riders.thelab.ui.locationonmaps;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.riders.thelab.R;
import com.riders.thelab.core.utils.LabLocationManager;
import com.riders.thelab.data.local.bean.MapsEnum;
import com.riders.thelab.ui.base.BaseViewImpl;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;

import static android.content.Context.LOCATION_SERVICE;
import static com.google.android.gms.common.api.CommonStatusCodes.RESOLUTION_REQUIRED;
import static com.google.android.gms.location.LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE;

@SuppressLint("NonConstantResourceId")
public class LocationOnMapsView extends BaseViewImpl<LocationOnMapsPresenter>
        implements LocationOnMapsContract.View, OnMapReadyCallback, LocationListener {

    private final LocationOnMapsActivity context;

    @BindView(R.id.rl_maps_laoding)
    RelativeLayout rlMapsLoading;
    @BindView(R.id.tv_location)
    TextView tvLocation;

    Unbinder unbinder;

    MapFragment mapFragment;
    private GoogleMap mMap;

    Geocoder geocoder;
    private Location mLocation;
    private LocationManager mLocationManager;
    private Criteria mCriteria;
    private String mProvider = "";

    // bunch of location related apis
    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;

    // boolean flag to toggle the ui
    private Boolean mRequestingLocationUpdates;

    // location last updated time
    private String mLastUpdateTime;

    // location updates interval - 10sec
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

    // fastest updates interval - 5 sec
    // location updates will be received if another app is requesting the locations
    // than your app can handle
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 5000;
    private static final int REQUEST_CHECK_SETTINGS = 100;


    @Inject
    LocationOnMapsView(LocationOnMapsActivity context) {
        this.context = context;
    }


    @Override
    public void onCreate() {

        getPresenter().attachView(this);

        Objects.requireNonNull(context.getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        context.getSupportActionBar().setTitle(context.getString(R.string.activity_title_location_on_maps));

        ButterKnife.bind(this, context.findViewById(android.R.id.content));


        Dexter.withContext(context)
                .withPermissions(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                )
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        if (multiplePermissionsReport.areAllPermissionsGranted()) {
                            Timber.i("All permissions are granted");
                            mRequestingLocationUpdates = true;
                            initLocationSettings();
                            setupMap();
                        } else {
                            Timber.e("All permissions are not granted");
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                })
                .withErrorListener(dexterError -> Timber.e(dexterError.toString()))
                .onSameThread()
                .check();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Timber.e("onActivityResult()");
        // Check for the integer request code originally supplied to startResolutionForResult().
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    Timber.e("User agreed to make required location settings changes.");
                    // Nothing to do. startLocationupdates() gets called in onResume again.
                    break;
                case Activity.RESULT_CANCELED:
                    Timber.e("User chose not to make required location settings changes.");
                    mRequestingLocationUpdates = false;
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onPause() {
        Timber.e("onPause()");
        if (mRequestingLocationUpdates) {
            // pausing location updates
            stopLocationUpdates();
        }
    }

    @Override
    public void onResume() {
        Timber.e("onResume()");
        // Resuming location updates depending on button state and
        // allowed permissions
        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }

        updateLocationUI();
    }


    @Override
    public void onDestroy() {

    }
    /////////////////////////////////////
    //
    // IMPLEMENTS
    //
    /////////////////////////////////////

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * <p>
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Timber.i("onMapReady()");
        mMap = googleMap;

        // Set a preference for minimum and maximum zoom.
        mMap.setMinZoomPreference(MapsEnum.WORLD.getDistance());
        mMap.setMaxZoomPreference(MapsEnum.DEFAULT_MAX_ZOOM.getDistance());
        mMap.moveCamera(CameraUpdateFactory.zoomTo(MapsEnum.WORLD.getDistance()));

        Completable.complete()
                .delay(3, TimeUnit.SECONDS)
                .doOnComplete(() -> context.runOnUiThread(this::setLocationSettings))
                .doOnError(Timber::e)
                .doAfterTerminate(() -> context.runOnUiThread(this::hideLoading))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onLocationChanged(@NonNull Location location) {

        // Remove markers
        mMap.clear();

        Timber.i("onLocationChanged()");
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        Timber.e("Lat : %s - Lon : %s", latitude, longitude);

        LatLng latLng = new LatLng(latitude, longitude);
        mMap.addMarker(
                new MarkerOptions()
                        .position(latLng)
                        .title(
                                LabLocationManager.getDeviceLocationToString(
                                        geocoder,
                                        location,
                                        context)));
        mMap.setMyLocationEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(750), 2000, null);
        mMap.getUiSettings().setScrollGesturesEnabled(true);

        tvLocation.setText("Latitude:" + latitude + "\nLongitude:" + longitude);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Timber.d("onStatusChanged()");
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        Timber.d("onProviderEnabled()");

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        Timber.d("onProviderDisabled()");

    }

    /////////////////////////////////////
    //
    // CLASS METHODS
    //
    /////////////////////////////////////
    private void initLocationSettings() {
        Timber.i("initLocationSettings()");

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        mSettingsClient = LocationServices.getSettingsClient(context);

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                // location is received
                mCurrentLocation = locationResult.getLastLocation();
                mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());

                updateLocationUI();
            }
        };

        mRequestingLocationUpdates = false;

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();

        geocoder = new Geocoder(context, Locale.getDefault());
    }

    private void setupMap() {
        Timber.i("setupMap()");
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment =
                (MapFragment) context.getFragmentManager()
                        .findFragmentById(R.id.google_maps_fragment);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    public void setLocationSettings() {
        Timber.i("setLocationSettings()");
        mLocationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        mCriteria = new Criteria();

        mProvider = mLocationManager.getBestProvider(mCriteria, true);
        try {
            mLocation = mLocationManager.getLastKnownLocation(mProvider);
        } catch (SecurityException e) {
            e.printStackTrace();
        }

        if (mLocation != null) {
            onLocationChanged(mLocation);
        }

        try {

            mLocationManager.requestLocationUpdates(
                    mProvider,
                    20000,
                    0,
                    this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private void hideLoading() {
        if (null != rlMapsLoading
                && rlMapsLoading.getVisibility() == View.VISIBLE)
            startAnimation(rlMapsLoading);
    }

    public void startAnimation(final View view) {
        Timber.d("startAnimation()");

        Animation animFadeOut = AnimationUtils.loadAnimation(context, R.anim.fade_out);
        animFadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                Timber.d("onAnimationStart()");
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Timber.d("onAnimationEnd()");
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                Timber.d("onAnimationRepeat()");
            }
        });

        final AnimationSet mAnimationSet = new AnimationSet(true);
        mAnimationSet.setInterpolator(new AccelerateInterpolator());
        mAnimationSet.addAnimation(animFadeOut);

        view.startAnimation(mAnimationSet);
    }

    /**
     * Starting location updates
     * Check whether location settings are satisfied and then
     * location updates will be requested
     */
    private void startLocationUpdates() {
        mSettingsClient
                .checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener((Activity) context, locationSettingsResponse -> {
                    Timber.i("All location settings are satisfied.");

                    Toast.makeText(context.getApplicationContext(), "Started location updates!", Toast.LENGTH_SHORT).show();

                    //noinspection MissingPermission
                    mFusedLocationClient.requestLocationUpdates(
                            mLocationRequest,
                            mLocationCallback,
                            Looper.myLooper());

                    updateLocationUI();
                })
                .addOnFailureListener((Activity) context, e -> {

                    int statusCode = ((ApiException) e).getStatusCode();

                    if (statusCode == RESOLUTION_REQUIRED) {
                        Timber.i("Location settings are not satisfied. Attempting to upgrade location settings ");

                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the
                            // result in onActivityResult().
                            ResolvableApiException rae = (ResolvableApiException) e;
                            rae.startResolutionForResult((Activity) context, REQUEST_CHECK_SETTINGS);

                        } catch (IntentSender.SendIntentException sie) {
                            Timber.i("PendingIntent unable to execute request.");
                        }
                    } else if (statusCode == SETTINGS_CHANGE_UNAVAILABLE) {

                        String errorMessage = "Location settings are inadequate, and cannot be " +
                                "fixed here. Fix in Settings.";
                        Timber.e(errorMessage);

                        Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();
                    }

                    updateLocationUI();
                });
    }

    public void stopLocationUpdates() {
        // Removing location updates
        mFusedLocationClient
                .removeLocationUpdates(mLocationCallback)
                .addOnCompleteListener(context, task -> Toast.makeText(
                        context.getApplicationContext(),
                        "Location updates stopped!",
                        Toast.LENGTH_SHORT).show());
    }

    /**
     * Update the UI displaying the location data
     * and toggling the buttons
     */
    private void updateLocationUI() {
        if (mCurrentLocation != null) {
            Timber.e("Lat: " + mCurrentLocation.getLatitude() + ", " +
                    "Lng: " + mCurrentLocation.getLongitude());

            geocoder = new Geocoder(context, Locale.getDefault());

            List<Address> addresses;

            try {
                addresses = geocoder.getFromLocation(
                        mCurrentLocation.getLatitude(),
                        mCurrentLocation.getLongitude(),
                        // In this sample, get just a single address.
                        1);

                Timber.e(addresses.get(0).getCountryName());

            } catch (IOException ioException) {
                // Catch network or other I/O problems.
//            errorMessage = getString(R.string.service_not_available);
                Timber.e(ioException, "errorMessage");
            } catch (IllegalArgumentException illegalArgumentException) {
                // Catch invalid latitude or longitude values.
//            errorMessage = getString(R.string.invalid_lat_long_used);
                Timber.e(illegalArgumentException, "errorMessage" + ". " +
                        "Latitude = " + mCurrentLocation.getLatitude() +
                        ", Longitude = " +
                        mCurrentLocation.getLongitude());
            }
        }
    }
}
