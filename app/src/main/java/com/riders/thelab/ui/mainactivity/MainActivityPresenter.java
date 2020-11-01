package com.riders.thelab.ui.mainactivity;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import com.riders.thelab.TheLabApplication;
import com.riders.thelab.data.local.model.App;
import com.riders.thelab.navigator.Navigator;
import com.riders.thelab.ui.base.BasePresenterImpl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

public class MainActivityPresenter extends BasePresenterImpl<MainActivityView>
        implements MainActivityContract.Presenter {

    @Inject
    MainActivity activity;

    @Inject
    Navigator navigator;

    @Inject
    MainActivityPresenter() {
    }


    /**
     * Get all packages and check if the returned list contains the target package
     */
    @Override
    public void getPackageList() {

        final String TARGET_PACKAGE = "com.riders";
        List<ApplicationInfo> installedAppList = new ArrayList<>();

        getView().showLoading();

        if (isPackageExists(installedAppList, TARGET_PACKAGE)) {

            List<App> appList = new ArrayList<>();

            for (ApplicationInfo appInfo : installedAppList) {
                Timber.e("package found : %s", appInfo.packageName);
                try {
                    String name = (String) activity.getPackageManager().getApplicationLabel(appInfo);
                    Drawable icon = activity.getPackageManager().getApplicationIcon(appInfo.packageName);
                    PackageInfo pInfo = activity.getPackageManager().getPackageInfo(appInfo.packageName, 0);
                    String version = pInfo.versionName;
                    String packageName = appInfo.packageName;

                    appList.add(
                            new App(name,
                                    icon,
                                    version,
                                    packageName));
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
            getView().hideLoading();

            getView().onSuccessPackageList(appList);
        } else {
            Timber.e("package " + TARGET_PACKAGE + " not found.");
            //installPackage(directory, targetApkFile);
            getView().hideLoading();

            getView().onErrorPackageList();
        }
    }


    /**
     * Returns true if the target package has been found
     *
     * @param targetPackage
     * @return
     */
    public boolean isPackageExists(final List<ApplicationInfo> installedAppList, String targetPackage) {

        boolean isPackageFound = false;

        // First Method
        List<ApplicationInfo> packages;
        PackageManager packageManager;

        packageManager = activity.getPackageManager();
        packages = packageManager.getInstalledApplications(0);

        for (ApplicationInfo packageInfo : packages) {
            if (packageInfo.packageName.contains(targetPackage)) {

                // Store found app package name
                String appToAdd = packageInfo.packageName;

                // Check if it does equal to the hub package
                // because we don't don't want to display it
                if (!appToAdd.equals(TheLabApplication.HUB_PACKAGE_NAME))
                    installedAppList.add(packageInfo);
                isPackageFound = true;
            }
        }

        return isPackageFound;
        // Second method
        /*try {
            PackageInfo info = packageManager
                    .getPackageInfo(targetPackage, PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e){
            e.printStackTrace();
            return false
        }
        return true;
        */
    }

    public void launchIntentForPackage(String packageName) {
        navigator.callIntentForPackageActivity(packageName);
    }
}
