package com.riders.thelab.ui.splashscreen;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.riders.thelab.ui.base.BasePresenterImpl;

import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;


public class SplashScreenPresenter extends BasePresenterImpl<SplashScreenView>
        implements SplashScreenContract.Presenter {

    @Inject
    SplashScreenActivity activity;

    @Inject
    SplashScreenPresenter() {
    }

    @Override
    public void hasPermissions(Context context) {

        Dexter
                .withContext(context)
                .withPermissions(
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.RECORD_AUDIO
                )
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            // do you work now
//                            Toast.makeText(context, "All permissions are granted!", Toast.LENGTH_SHORT).show();

                            getView().onPermissionsGranted();

                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // permission is denied permanently, navigate user to app settings
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .withErrorListener(error -> {
                    Toast.makeText(context, "Error occurred! " + error.toString(), Toast.LENGTH_SHORT).show();
                    getView().onPermissionsDenied();
                })
                .onSameThread()
                .check();
    }

    @Override
    public void getAppVersion() {
        try {
            PackageInfo pInfo = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0);
            String version = pInfo.versionName;

            getView().displayAppVersion(version);

        } catch (PackageManager.NameNotFoundException error) {
            Timber.e(error);
        }
    }
}
