package com.riders.thelab.core.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.riders.thelab.core.interfaces.ConnectivityListener;
import com.riders.thelab.data.remote.livedata.WeatherLiveDataHelper;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class LabNetworkManager extends ConnectivityManager.NetworkCallback {

    private static boolean isConnected = false;
    ConnectivityListener listener;

    public LabNetworkManager(ConnectivityListener listener) {
        this.listener = listener;
    }

    public static boolean isIsConnected() {
        return isConnected;
    }
    /**
     * Check the Internet connection
     *
     * @param context
     * @return
     */
    public static boolean isConnected(Context context) {

        if (!LabCompatibilityManager.isLollipop()) {
            ConnectivityManager connMgr =
                    (ConnectivityManager) context.getSystemService(Activity.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            isConnected = networkInfo != null && networkInfo.isConnected();
        }

        return isConnected;
    }

    @Override
    public void onAvailable(@NonNull Network network) {
        super.onAvailable(network);
        Timber.d("onAvailable()");

        isConnected = true;

        listener.onConnected();
    }

    @Override
    public void onBlockedStatusChanged(@NonNull Network network, boolean blocked) {
        super.onBlockedStatusChanged(network, blocked);
        Timber.e("onBlockedStatusChanged()");
    }

    @Override
    public void onLosing(@NonNull Network network, int maxMsToLive) {
        super.onLosing(network, maxMsToLive);
        Timber.e("onLosing()");
    }

    @Override
    public void onLost(@NonNull Network network) {
        super.onLost(network);
        Timber.e("onLost()");

        isConnected = false;

        listener.onLostConnection();
    }

    @Override
    public void onUnavailable() {
        super.onUnavailable();
        Timber.e("onUnavailable()");

        isConnected = false;
    }

    private void createUrlConnectionCall(String targetUrl, boolean withProgressCallback, @Nullable WeatherLiveDataHelper liveDataHelper) {
        Timber.d("createUrlConnectionCall()");

        Observable.create(emitter -> {
            try {
                URL url = new URL(targetUrl);
                URLConnection urlConnection = url.openConnection();
                urlConnection.connect();
                int fileLength = urlConnection.getContentLength();
                InputStream inputStream = urlConnection.getInputStream();
                byte[] buffer = new byte[1024];
                int len1;
                long total = 0;
                while ((len1 = inputStream.read(buffer)) > 0) {
                    total += len1;

                    if (withProgressCallback && (null != liveDataHelper)) {
                        int percentage = (int) ((total * 100) / fileLength);
                        publishProgress(percentage, liveDataHelper);
                    }
                }
                inputStream.close();
            } catch (Exception e) {
                Timber.e("---- URL Connection catch error ----");
                Timber.e(e);
                e.printStackTrace();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.from(Looper.myLooper()))
                .subscribe();
    }


    private void publishProgress(int progress, WeatherLiveDataHelper liveDataHelper) {
        Timber.d("Progress value : %d percent", progress);
        liveDataHelper.updateDownloadPer(progress);
    }

}
