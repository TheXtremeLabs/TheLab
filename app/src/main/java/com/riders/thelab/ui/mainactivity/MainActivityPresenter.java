package com.riders.thelab.ui.mainactivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import com.riders.thelab.TheLabApplication;
import com.riders.thelab.data.local.model.App;
import com.riders.thelab.navigator.Navigator;
import com.riders.thelab.ui.base.BasePresenterImpl;
import com.riders.thelab.utils.Constants;

import java.util.ArrayList;
import java.util.Arrays;
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


    @Override
    public void getApplications() {

        List<App> appList = new ArrayList<>();

        // Get constants activities
        appList.addAll(Constants.getInstance().getActivityList());
        appList.addAll(getPackageList());

        if (appList.isEmpty()) {
            getView().onErrorPackageList();
        } else {
            getView().onSuccessPackageList(appList);
        }

    }

    @Override
    public void launchActivityOrPackage(App item) {

        if (null != item.getPackageName()) {
            Timber.d("launchIntentForPackage(%s)", item.getPackageName());

            // Just use these following two lines,
            // so you can launch any installed application whose package name is known:
            launchIntentForPackage(item.getPackageName());
        } else {

            // Prevent app from crashing if you click on WIP item
            if (null != item.getActivity()) {
                Timber.d("launchActivity(%s)", item.getActivity().getSimpleName());

                launchActivity(item.getActivity());
            } else {
                // Just Log wip item
                Timber.e("Cannot launch this activity : %s", item.toString());
            }
        }
    }

    /**
     * Get all packages and check if the returned list contains the target package
     */
    public List<App> getPackageList() {

        final String[] TARGET_PACKAGES = new String[]{
                "com.riders",
                "com.reepling",
                "com.praeter"
        };
        List<ApplicationInfo> installedAppList = new ArrayList<>();

        List<App> appList = new ArrayList<>();

        if (isPackageExists(installedAppList, TARGET_PACKAGES)) {

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
        } else {
            Timber.e("package " + Arrays.toString(TARGET_PACKAGES) + " not found.");
            //installPackage(directory, targetApkFile);
        }

        return appList;
    }


    /**
     * Returns true if the target package has been found
     *
     * @param targetPackages
     * @return
     */
    @SuppressLint("QueryPermissionsNeeded")
    public boolean isPackageExists(final List<ApplicationInfo> installedAppList, String... targetPackages) {

        boolean isPackageFound = false;

        // First Method
        List<ApplicationInfo> packages;
        PackageManager packageManager;

        packageManager = activity.getPackageManager();
        packages = packageManager.getInstalledApplications(0);

        for (ApplicationInfo packageInfo : packages) {
            for (String packageItem : targetPackages) {
                if (packageInfo.packageName.contains(packageItem)) {

                    // Store found app package name
                    String appToAdd = packageInfo.packageName;

                    // Check if it does equal to The Lab package name
                    // because we don't don't want to display it
                    if (!appToAdd.equals(TheLabApplication.getLabPackageName()))
                        installedAppList.add(packageInfo);
                    isPackageFound = true;
                }
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

    @Override
    public void launchIntentForPackage(String packageName) {
        navigator.callIntentForPackageActivity(packageName);
    }

    @Override
    public void launchActivity(Class<? extends Activity> activity) {
        navigator.callIntentActivity(activity);
    }
}
