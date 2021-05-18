package com.riders.thelab.utils

import com.riders.thelab.R
import com.riders.thelab.data.local.model.App
import com.riders.thelab.ui.biometric.BiometricActivity
import com.riders.thelab.ui.builtin.BuiltInWebViewActivity
import com.riders.thelab.ui.colors.ColorActivity
import com.riders.thelab.ui.customtoast.CustomToastActivity
import com.riders.thelab.ui.floatinglabels.FloatingLabelsActivity
import com.riders.thelab.ui.floatingview.FloatingViewActivity
import com.riders.thelab.ui.kat.KatActivity
import com.riders.thelab.ui.speechtotext.SpeechToTextActivity
import com.riders.thelab.ui.spring.SpringActivity
import com.riders.thelab.ui.tabs.WorkingTabsActivity
import com.riders.thelab.ui.transition.TransitionActivity
import com.riders.thelab.ui.vectordrawables.VectorDrawablesActivity
import timber.log.Timber
import java.util.*

class Constants {

    companion object {
        const val EMULATOR_DEVICE_TAG = "sdk"

        //REST client Base URL
        const val BASE_ENDPOINT_YOUTUBE = "https://raw.githubusercontent.com"
        const val BASE_ENDPOINT_SEARCH = "https://ajax.googleapis.com"
        const val BASE_ENDPOINT_GOOGLE_FIREBASE_API = " https://firebasestorage.googleapis.com/"
        const val BASE_ENDPOINT_GOOGLE_MAPS_API = "https://maps.googleapis.com/maps/api/"
        const val BASE_ENDPOINT_GOOGLE_PLACES = "https://maps.googleapis.com/maps/api/place/"
        const val BASE_ENDPOINT_WEATHER = "http://api.openweathermap.org"
        const val BASE_ENDPOINT_WEATHER_BULK_DOWNLOAD = "http://bulk.openweathermap.org/"
        const val WEATHER_BULK_DOWNLOAD_URL = "sample/city.list.json.gz"
        const val BASE_ENDPOINT_WEATHER_ICON = "http://openweathermap.org/img/wn/"
        const val BASE_ENDPOINT_WEATHER_FLAG = "http://openweathermap.org/images/flags/"
        const val WEATHER_ICON_SUFFIX = "@2x.png"
        const val WEATHER_FLAG_PNG_SUFFIX = ".png"
        const val WEATHER_COUNTRY_CODE_FRANCE = "FR"
        const val WEATHER_COUNTRY_CODE_GUADELOUPE = "GP"
        const val WEATHER_COUNTRY_CODE_MARTINIQUE = "MQ"
        const val WEATHER_COUNTRY_CODE_GUYANE = "GF"
        const val WEATHER_COUNTRY_CODE_REUNION = "RE"
        const val FIREBASE_DATABASE_NAME = "kat"

        // Palette
        const val PALETTE_URL = "http://i.ytimg.com/vi/aNHOfJCphwk/maxresdefault.jpg"

        // Activity Recognition
        const val BROADCAST_DETECTED_ACTIVITY = "activity_intent"
        const val DETECTION_INTERVAL_IN_MILLISECONDS = (30 * 1000).toLong()
        const val CONFIDENCE = 70
    }

    private var instance: Constants? = null

    private lateinit var activityItems: List<App>


    init {
        Timber.d("Constructor constants()")
        activityItems = ArrayList(loadActivities())
    }

    // Factory method to provide the users with instances
    fun getInstance(): Constants {
        if (null == instance) instance = Constants()
        return instance!!
    }

    fun loadActivities(): List<App> {
        val list: MutableList<App> = ArrayList()

        list.add(App("Colors", "Change color programmatically...",
                R.drawable.logo_colors, ColorActivity::class.java))
        list.add(App("Biometric", "Check biometric hardware and test it...",
                R.drawable.ic_fingerprint, BiometricActivity::class.java))
        /*list.add(App("Recycler", "Recycler Basics and best practices...",
                R.drawable.ic_filter_list, RecyclerViewActivity::class.java))*/
        list.add(App("Tabs", "ViewPager Fragments Tabs...",
                R.drawable.ic_tab, WorkingTabsActivity::class.java))
        list.add(App("Transitions", "Start a new activity with awesome animations...",
                R.drawable.ic_flip_to_back, TransitionActivity::class.java))
        list.add(App("Floating Labels", "Floating Labels Form...",
                R.drawable.ic_outline_label, FloatingLabelsActivity::class.java))
        /*list.add(App("Contact List", "Fetch contacts from database, add one and more...",
                R.drawable.ic_contacts, ContactsActivity::class.java))
        list.add(App("Location On Maps", "Display User location on map...",
                R.drawable.ic_location_on, LocationOnMapsActivity::class.java))
        list.add(App("Schedule Job", "Own alarm to remind user...",
                R.drawable.ic_schedule, ScheduleActivity::class.java))
        list.add(App("Devices Informations", "Display device info...",
                R.drawable.ic_device_information, DeviceInformationActivity::class.java))
        list.add(App("Palette", "Get different color from an image...",
                R.drawable.ic_palette, PaletteActivity::class.java))
        list.add(App("Filter ListView", "ListView with filter...",
                R.drawable.ic_filter_list, FilterListViewActivity::class.java))
        list.add(App("Multi Pane", "Display content on split screen...",
                R.drawable.ic_aspect_ratio, MultipaneActivity::class.java))*/
        /*
        list.add(new App("Songs Player (Work In Progress)", "Display content on split screen...",
                R.mipmap.ic_launcher_round, SongPlayerActivity.class));
*/
        /*list.add(App("Youtube", "Youtube look like...",
                R.drawable.youtube_icon_like, YoutubeLikeActivity::class.java))*/
        list.add(App("My Speech To Text", "Own speech to text implementation...",
                R.drawable.ic_mic, SpeechToTextActivity::class.java))

        /*
        list.add(new App("Activity Recognition", "Display user activity...",
                R.drawable.ic_walking, ActivityRecognitionActivity.class));*/

        list.add(App("Built-in Web View", "Display web view in activity directly...",
                R.drawable.ic_alternate_email, BuiltInWebViewActivity::class.java))
        /*list.add(App("Weather App", "Current weather forecast in your city...",
                R.drawable.openweathermap, WeatherActivity::class.java))*/
        list.add(App("Floating Widget", "Create a floating widget that you can move around on the screen...",
                R.drawable.ic_flip_to_back, FloatingViewActivity::class.java))
        list.add(App("Custom Toast", "Custom Toast Layout...",
                R.drawable.ic_announcement, CustomToastActivity::class.java))
        list.add(App("Vector Drawables", "Animated, scale, transform vector drawables...",
                R.drawable.ic_aspect_ratio, VectorDrawablesActivity::class.java))
        list.add(App("Spring", "Physics-based motion is driven by force. Spring force is one such force that guides interactivity and motion....",
                R.drawable.ic_filter_center_focus, SpringActivity::class.java))
        list.add(App("Chat", "Realtime chat using firebase realtime database features",
                R.drawable.ic_k_at, KatActivity::class.java))
        list.add(App("WIP", "Coming soon...",
                R.drawable.ic_warning, null))
        return list
    }


    /**
     * Return app testing list
     *
     *
     * [Add here new app object]
     *
     * @return
     */
    fun getActivityList(): List<App> {
        return activityItems
    }
}