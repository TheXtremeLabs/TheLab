package com.riders.thelab.data

import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.drawable.Drawable
import android.location.Location
import com.google.firebase.storage.StorageReference
import com.riders.thelab.TheLabApplication
import com.riders.thelab.data.local.DbImpl
import com.riders.thelab.data.local.model.App
import com.riders.thelab.data.local.model.Contact
import com.riders.thelab.data.local.model.Video
import com.riders.thelab.data.local.model.weather.CityModel
import com.riders.thelab.data.local.model.weather.WeatherData
import com.riders.thelab.data.remote.ApiImpl
import com.riders.thelab.data.remote.dto.artist.Artist
import com.riders.thelab.data.remote.dto.weather.City
import com.riders.thelab.data.remote.dto.weather.OneCallWeatherResponse
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single
import okhttp3.ResponseBody
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    dbImpl: DbImpl,
    apiImpl: ApiImpl
) : IRepository {

    companion object {
        private val TARGET_PACKAGES = arrayOf(
            "com.riders",
            "com.reepling",
            "com.praeter"
        )
    }

    private var mDbImpl: DbImpl = dbImpl
    private var mApiImpl: ApiImpl = apiImpl


    override fun getPackageList(): List<App> {

        val installedAppList: List<ApplicationInfo> = ArrayList()

        val appList: MutableList<App> = ArrayList<App>()

        val context = TheLabApplication.getInstance().getContext()

        if (isPackageExists(installedAppList as MutableList<ApplicationInfo>, TARGET_PACKAGES)) {
            for (appInfo in installedAppList) {
                Timber.e("package found : %s", appInfo.packageName)
                try {

                    val icon: Drawable =
                        context.packageManager.getApplicationIcon(appInfo.packageName)
                    val pInfo: PackageInfo =
                        context.packageManager.getPackageInfo(appInfo.packageName, 0)
                    val version = pInfo.versionName
                    val packageName = appInfo.packageName
                    appList.add(
                        App(
                            context.packageManager.getApplicationLabel(appInfo).toString(),
                            icon,
                            version,
                            packageName
                        )
                    )
                } catch (e: PackageManager.NameNotFoundException) {
                    e.printStackTrace()
                }
            }
        } else {
            Timber.e("package %s not found.", TARGET_PACKAGES.contentToString())
            //installPackage(directory, targetApkFile);
        }

        return appList
    }

    @SuppressLint("QueryPermissionsNeeded")
    fun isPackageExists(
        installedAppList: MutableList<ApplicationInfo>,
        targetPackages: Array<String>
    ): Boolean {
        var isPackageFound = false

        // First Method
        val packages: List<ApplicationInfo>
        val packageManager: PackageManager = TheLabApplication.getInstance().packageManager
        packages = packageManager.getInstalledApplications(0)

        for (packageInfo in packages) {
            for (packageItem in targetPackages) {
                if (packageInfo.packageName.contains(packageItem!!)) {

                    // Store found app package name
                    val appToAdd = packageInfo.packageName

                    // Check if it does equal to The Lab package name
                    // because we don't don't want to display it
                    if (appToAdd != TheLabApplication.getInstance().getLabPackageName())
                        installedAppList.add(packageInfo)
                    isPackageFound = true
                }
            }
        }
        return isPackageFound

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

    override fun insertContact(contact: Contact) {
        mDbImpl.insertContact(contact)
    }

    override fun insertContactRX(contact: Contact): Maybe<Long> {
        return mDbImpl.insertContactRX(contact)
    }

    override fun insertAllContacts(contactDetails: List<Contact>) {
        mDbImpl.insertAllContacts(contactDetails)
    }

    override fun getContacts(): List<Contact> {
        return mDbImpl.getContacts()
    }

    override fun getAllContacts(): Single<List<Contact>> {
        return mDbImpl.getAllContacts()
    }

    override fun clearData() {
        mDbImpl.clearData()
    }

    override fun insertWeatherData(isWeatherData: WeatherData): Maybe<Long> {
        return mDbImpl.insertWeatherData(isWeatherData)
    }

    override fun saveCity(city: CityModel): Maybe<Long> {
        return mDbImpl.saveCity(city)
    }

    override fun saveCities(dtoCities: List<City>): Maybe<List<Long>> {
        return mDbImpl.saveCities(dtoCities)
    }

    override fun getWeatherData(): Single<WeatherData> {
        return mDbImpl.getWeatherData()
    }

    override fun getCities(): Single<List<CityModel>> {
        return mDbImpl.getCities()
    }

    override fun getCitiesCursor(query: String): Cursor {
        return mDbImpl.getCitiesCursor(query)
    }

    override fun deleteAll() {
        mDbImpl.deleteAll()
    }

    override fun getStorageReference(activity: Activity): Single<StorageReference> {
        return mApiImpl.getStorageReference(activity)
    }

    override fun getArtists(url: String): Single<List<Artist>> {
        return mApiImpl.getArtists(url)
    }

    override fun getVideos(): Single<List<Video>> {
        return mApiImpl.getVideos()
    }

    override fun getWeatherOneCallAPI(location: Location): Single<OneCallWeatherResponse>? {
        return mApiImpl.getWeatherOneCallAPI(location)
    }

    override fun getBulkWeatherCitiesFile(): Single<ResponseBody> {
        return mApiImpl.getBulkWeatherCitiesFile()
    }
}