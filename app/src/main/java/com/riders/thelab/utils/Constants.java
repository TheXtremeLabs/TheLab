package com.riders.thelab.utils;

import com.riders.thelab.R;
import com.riders.thelab.data.local.model.App;
import com.riders.thelab.ui.biometric.BiometricActivity;
import com.riders.thelab.ui.builtin.BuiltInWebViewActivity;
import com.riders.thelab.ui.colors.ColorActivity;
import com.riders.thelab.ui.contacts.ContactsActivity;
import com.riders.thelab.ui.customtoast.CustomToastActivity;
import com.riders.thelab.ui.deviceinformation.DeviceInformationActivity;
import com.riders.thelab.ui.filterlistview.FilterListViewActivity;
import com.riders.thelab.ui.floatinglabels.FloatingLabelsActivity;
import com.riders.thelab.ui.floatingview.FloatingViewActivity;
import com.riders.thelab.ui.locationonmaps.LocationOnMapsActivity;
import com.riders.thelab.ui.multipane.MultipaneActivity;
import com.riders.thelab.ui.palette.PaletteActivity;
import com.riders.thelab.ui.recycler.RecyclerViewActivity;
import com.riders.thelab.ui.schedule.ScheduleActivity;
import com.riders.thelab.ui.speechtotext.SpeechToTextActivity;
import com.riders.thelab.ui.tabs.WorkingTabsActivity;
import com.riders.thelab.ui.transition.TransitionActivity;
import com.riders.thelab.ui.vectordrawables.VectorDrawablesActivity;
import com.riders.thelab.ui.weather.WeatherActivity;
import com.riders.thelab.ui.youtubelike.YoutubeLikeActivity;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class Constants {

    private static Constants instance;

    private final List<App> activityItems;

    public static final String EMULATOR_DEVICE_TAG = "sdk";

    //REST client Base URL
    public static final String BASE_ENDPOINT_YOUTUBE = "https://raw.githubusercontent.com";
    public static final String BASE_ENDPOINT_SEARCH = "https://ajax.googleapis.com";
    public static final String BASE_ENDPOINT_GOOGLE_MAPS_API = "https://maps.googleapis.com/maps/api/";
    public static final String BASE_ENDPOINT_GOOGLE_PLACES = "https://maps.googleapis.com/maps/api/place/";
    public static final String BASE_ENDPOINT_WEATHER = "http://api.openweathermap.org";
    public static final String BASE_ENDPOINT_WEATHER_BULK_DOWNLOAD = "http://bulk.openweathermap.org/";
    public static final String WEATHER_BULK_DOWNLOAD_URL = "sample/city.list.json.gz";
    public static final String BASE_ENDPOINT_WEATHER_ICON = "http://openweathermap.org/img/wn/";
    public static final String WEATHER_ICON_SUFFIX = "@2x.png";

    public static final String WEATHER_COUNTRY_CODE_FRANCE = "FR";
    public static final String WEATHER_COUNTRY_CODE_GUADELOUPE = "GP";
    public static final String WEATHER_COUNTRY_CODE_MARTINIQUE = "MQ";
    public static final String WEATHER_COUNTRY_CODE_GUYANE = "GF";
    public static final String WEATHER_COUNTRY_CODE_REUNION = "RE";

    // Palette
    public static final String PALETTE_URL = "http://i.ytimg.com/vi/aNHOfJCphwk/maxresdefault.jpg";


    // Activity Recognition
    public static final String BROADCAST_DETECTED_ACTIVITY = "activity_intent";
    public static final long DETECTION_INTERVAL_IN_MILLISECONDS = 30 * 1000;
    public static final int CONFIDENCE = 70;


    // private constructor can't be accessed outside the class
    private Constants() {
        Timber.d("Constructor constants()");
        this.activityItems = new ArrayList<>(loadActivities());
    }

    // Factory method to provide the users with instances
    public static Constants getInstance() {
        if (null == instance)
            instance = new Constants();
        return instance;
    }

    public List<App> loadActivities() {
        List<App> list = new ArrayList<>();
        /*list.add(new App("Highcharts API", "Testing Highcharts API for Android ...",
                R.drawable.logo_highcharts, HighChartsActivity.class));

        list.add(new App("Android Services", "Android Services for any action...",
                R.drawable.logo_service, ServiceActivity.class));

         */

        list.add(new App("Colors", "Change color programmatically...",
                R.drawable.logo_colors, ColorActivity.class));

        list.add(new App("Biometric", "Check biometric hardware and test it...",
                R.drawable.ic_fingerprint, BiometricActivity.class));

        list.add(new App("Recycler", "Recycler Basics and best practices...",
                R.drawable.ic_filter_list, RecyclerViewActivity.class));

        list.add(new App("Tabs", "ViewPager Fragments Tabs...",
                R.drawable.ic_tab, WorkingTabsActivity.class));


        list.add(new App("Transitions", "Start a new activity with awesome animations...",
                R.drawable.ic_flip_to_back, TransitionActivity.class));


        list.add(new App("Floating Labels", "Floating Labels Form...",
                R.drawable.ic_outline_label, FloatingLabelsActivity.class));

        list.add(new App("Contact List", "Fetch contacts from database, add one and more...",
                R.drawable.ic_contacts, ContactsActivity.class));

        list.add(new App("Location On Maps", "Display User location on map...",
                R.drawable.ic_location_on, LocationOnMapsActivity.class));

        list.add(new App("Schedule Job", "Own alarm to remind user...",
                R.drawable.ic_schedule, ScheduleActivity.class));

        list.add(new App("Devices Informations", "Display device info...",
                R.drawable.ic_device_information, DeviceInformationActivity.class));

        list.add(new App("Palette", "Get different color from an image...",
                R.drawable.ic_palette, PaletteActivity.class));

        list.add(new App("Filter ListView", "ListView with filter...",
                R.drawable.ic_filter_list, FilterListViewActivity.class));

        list.add(new App("Multi Pane", "Display content on split screen...",
                R.drawable.ic_aspect_ratio, MultipaneActivity.class));
/*
        listActivities.add( new ItemActivity("Search Box Activity", R.drawable.ic_alarm_black_48dp, SearchBoxActivity.class) );
        listActivities.add( new ItemActivity("Bandeau Pictures Activity", R.drawable.ic_picture_in_picture_black_48dp, BandeauPicturesActivity.class) );
        listActivities.add( new ItemActivity("Send Email Activity", R.drawable.ic_email_black_48dp, SendMailActivity.class) );
        */

/*
        list.add(new App("Songs Player (Work In Progress)", "Display content on split screen...",
                R.mipmap.ic_launcher_round, SongPlayerActivity.class));
*/
        list.add(new App("Youtube", "Youtube look like...",
                R.drawable.youtube_icon_like, YoutubeLikeActivity.class));


        list.add(new App("My Speech To Text", "Own speech to text implementation...",
                R.drawable.ic_mic, SpeechToTextActivity.class));

                /*
        list.add(new App("Activity Recognition", "Display user activity...",
                R.drawable.ic_walking, ActivityRecognitionActivity.class));*/

        list.add(new App("Built-in Web View", "Display web view in activity directly...",
                R.drawable.ic_alternate_email, BuiltInWebViewActivity.class));

        list.add(new App("Weather App", "Current weather forecast in your city...",
                R.drawable.openweathermap, WeatherActivity.class));

        list.add(new App("Floating Widget", "Create a floating widget that you can move around on the screen...",
                R.drawable.ic_flip_to_back, FloatingViewActivity.class));

        list.add(new App("Custom Toast", "Custom Toast Layout...",
                R.drawable.ic_announcement, CustomToastActivity.class));

        list.add(new App("Vector Drawables", "Animated, scale, transform vector drawables...",
                R.drawable.ic_aspect_ratio, VectorDrawablesActivity.class));


        list.add(new App("WIP", "Coming soon...",
                R.drawable.ic_warning, null));

        return list;
    }


    /**
     * Return app testing list
     * <p>
     * [Add here new app object]
     *
     * @return
     */
    public List<App> getActivityList() {
        return activityItems;
    }

}
