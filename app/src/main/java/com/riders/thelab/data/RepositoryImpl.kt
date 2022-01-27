package com.riders.thelab.data

import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.drawable.Drawable
import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.firebase.storage.StorageReference
import com.riders.thelab.TheLabApplication
import com.riders.thelab.data.local.DbImpl
import com.riders.thelab.data.local.model.Contact
import com.riders.thelab.data.local.model.Download
import com.riders.thelab.data.local.model.Video
import com.riders.thelab.data.local.model.app.App
import com.riders.thelab.data.local.model.weather.CityModel
import com.riders.thelab.data.local.model.weather.WeatherData
import com.riders.thelab.data.preferences.PreferencesImpl
import com.riders.thelab.data.remote.ApiImpl
import com.riders.thelab.data.remote.dto.ApiResponse
import com.riders.thelab.data.remote.dto.UserDto
import com.riders.thelab.data.remote.dto.artist.Artist
import com.riders.thelab.data.remote.dto.weather.City
import com.riders.thelab.data.remote.dto.weather.OneCallWeatherResponse
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import retrofit2.Call
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    dbImpl: DbImpl,
    apiImpl: ApiImpl,
    preferencesImpl: PreferencesImpl
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
    private var mPreferencesImpl: PreferencesImpl = preferencesImpl


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
                if (packageInfo.packageName.contains(packageItem)) {

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


    private val mLocationData: MediatorLiveData<Boolean>
        get() = MediatorLiveData()

    override fun getLocationStatusData(): LiveData<Boolean> {
        return mLocationData
    }

    override fun addLocationStatusDataSource(data: LiveData<Boolean>) {
        mLocationData.addSource(data, mLocationData::setValue)
    }

    override fun removeLocationStatusDataSource(data: LiveData<Boolean>) {
        mLocationData.removeSource(data)
    }


    override fun insertContact(contact: Contact) {
        mDbImpl.insertContact(contact)
    }

    override suspend fun insertContactRX(contact: Contact): Long {
        return mDbImpl.insertContactRX(contact)
    }

    override fun insertAllContacts(contactDetails: List<Contact>) {
        mDbImpl.insertAllContacts(contactDetails)
    }

    override fun getContacts(): List<Contact> {
        return mDbImpl.getContacts()
    }

    override suspend fun getAllContacts(): List<Contact> {
        return mDbImpl.getAllContacts()
    }

    override fun clearData() {
        mDbImpl.clearData()
    }

    override suspend fun insertWeatherData(isWeatherData: WeatherData): Long {
        return mDbImpl.insertWeatherData(isWeatherData)
    }

    override suspend fun saveCity(city: CityModel): Long {
        return mDbImpl.saveCity(city)
    }

    override suspend fun saveCities(dtoCities: List<City>): List<Long> {
        return mDbImpl.saveCities(dtoCities)
    }

    override suspend fun getWeatherData(): WeatherData? {
        return mDbImpl.getWeatherData()
    }

    override suspend fun getCities(): List<CityModel> {
        return mDbImpl.getCities()
    }

    override fun getCitiesCursor(query: String): Cursor {
        return mDbImpl.getCitiesCursor(query)
    }

    override fun deleteAll() {
        mDbImpl.deleteAll()
    }

    override suspend fun getStorageReference(activity: Activity): StorageReference? {
        return mApiImpl.getStorageReference(activity)
    }

    override suspend fun getArtists(url: String): List<Artist> {
        return mApiImpl.getArtists(url)
    }

    override suspend fun getVideos(): List<Video> {
        return mApiImpl.getVideos()
    }

    override suspend fun getWeatherOneCallAPI(location: Location): OneCallWeatherResponse {
        return mApiImpl.getWeatherOneCallAPI(location)
    }

    override fun getBulkWeatherCitiesFile(): Call<ResponseBody> {
        return mApiImpl.getBulkWeatherCitiesFile()
    }

    override suspend fun getBulkDownload(): Flow<Download> {
        return mApiImpl.getBulkDownload()
    }

    override suspend fun getApi(): ApiResponse = mApiImpl.getApi()

    override suspend fun login(user: UserDto) = mApiImpl.login(user)

    override suspend fun saveUser(user: UserDto) = mApiImpl.saveUser(user)


    override fun isNightMode(): Flow<Boolean> = mPreferencesImpl.isNightMode()

    override suspend fun toggleNightMode() = mPreferencesImpl.toggleNightMode()

    override fun getEmailPref(): Flow<String> = mPreferencesImpl.getEmailPref()

    override suspend fun saveEmailPref(email: String) = mPreferencesImpl.saveEmailPref(email)

    override fun getPasswordPref(): Flow<String> = mPreferencesImpl.getPasswordPref()

    override suspend fun savePasswordPref(password: String) =
        mPreferencesImpl.savePasswordPref(password)

    override fun isRememberCredentialsPref(): Flow<Boolean> =
        mPreferencesImpl.isRememberCredentialsPref()

    override suspend fun saveRememberCredentialsPref(isChecked: Boolean) =
        mPreferencesImpl.saveRememberCredentialsPref(isChecked)

    override fun getUserToken(): Flow<String> = mPreferencesImpl.getUserToken()

    override suspend fun saveTokenPref(token: String) = mPreferencesImpl.saveTokenPref(token)
}