package com.riders.thelab.utils;

import com.riders.thelab.R;
import com.riders.thelab.data.local.model.App;
import com.riders.thelab.data.local.model.RecyclerItem;
import com.riders.thelab.ui.colors.ColorActivity;
import com.riders.thelab.ui.filterlistview.FilterListViewActivity;
import com.riders.thelab.ui.floatinglabels.FloatingLabelsActivity;
import com.riders.thelab.ui.palette.PaletteActivity;
import com.riders.thelab.ui.weather.WeatherActivity;
import com.riders.thelab.ui.youtubelike.YoutubeLikeActivity;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class Constants {

    private static Constants instance;

    private final List<App> activityItems;
    static ArrayList<RecyclerItem> recyclerItems;

    //REST client Base URL
    public static final String BASE_ENDPOINT_YOUTUBE = "https://raw.githubusercontent.com";
    public static final String BASE_ENDPOINT_SEARCH = "https://ajax.googleapis.com";
    public static final String BASE_ENDPOINT_GOOGLE_PLACES = "https://maps.googleapis.com/maps/api/place/";
    public static final String BASE_ENDPOINT_WEATHER = "http://api.openweathermap.org";
    public static final String BASE_ENDPOINT_WEATHER_ICON = "http://openweathermap.org/img/wn/";
    public static final String WEATHER_ICON_SUFFIX = "@2x.png";

    public static final String WEATHER_COUNTRY_CODE_FRANCE = "FR";
    public static final String WEATHER_COUNTRY_CODE_GUADELOUPE = "GP";
    public static final String WEATHER_COUNTRY_CODE_MARTINIQUE = "MQ";
    public static final String WEATHER_COUNTRY_CODE_GUYANE = "GF";
    public static final String WEATHER_COUNTRY_CODE_REUNION = "RE";


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

        /*
        list.add(new App("Recycler", "Recycler Basics and best practices...",
                R.drawable.ic_launcher_background, RecyclerViewActivity.class));

        list.add(new App("Transition", "Transition, shared elements...",
                R.drawable.transition_fade, TransitionActivity.class));

        list.add(new App("Tabs", "ViewPager Fragments Tabs...",
                R.drawable.ic_tab_black_48dp, WorkingTabsActivity.class));
*/
        list.add(new App("Floating Labels", "Floating Labels Form...",
                R.drawable.ic_outline_label, FloatingLabelsActivity.class));

/*
        list.add(new App("Location Google API", "Find user location and display coordinates...",
                R.drawable.ic_location_on_black_24dp, LocationGoogleAPIActivity.class));

        list.add(new App("Contact List", "Contact List...",
                R.drawable.ic_contacts_black_48dp, ContactListActivity.class));

        list.add(new App("Location On Maps", "Display User location on map...",
                R.drawable.ic_location_on_black_48dp, LocationOnMapsActivity.class));

        list.add(new App("Schedule Job", "Own alarm to remind user...",
                R.drawable.ic_schedule_black_48dp, ScheduleJobActivity.class));

        list.add(new App("Devices Informations", "Display device info...",
                R.drawable.ic_perm_device_information_black_48dp, DeviceInformationsActivity.class));*/

        list.add(new App("Palette", "Get different color from an image...",
                R.drawable.ic_palette_72, PaletteActivity.class));

        list.add(new App("Filter ListView", "ListView with filter...",
                R.drawable.ic_filter_list, FilterListViewActivity.class));

        /*list.add(new App("Multi Pane", "Display content on split screen...",
                R.drawable.ic_aspect_ratio_black_48dp, MultiPaneActivity.class));*/
/*
        listActivities.add( new ItemActivity("Contacts Database Activity", R.drawable.ic_perm_contact_calendar_black_48dp, ContactsDatabaseActivity.class) );
        listActivities.add( new ItemActivity("Search Box Activity", R.drawable.ic_alarm_black_48dp, SearchBoxActivity.class) );
        listActivities.add( new ItemActivity("Bandeau Pictures Activity", R.drawable.ic_picture_in_picture_black_48dp, BandeauPicturesActivity.class) );
        listActivities.add( new ItemActivity("Send Email Activity", R.drawable.ic_email_black_48dp, SendMailActivity.class) );
        listActivities.add( new ItemActivity("RecyclerView Basics Activity", R.drawable.ic_filter_list_black_48dp, RecyclerBasicsActivity.class) );
        */

        /*list.add(new App("RxJava", "Reactive Android...",
                R.drawable.logo_rx_java, RxJavaActivity.class));

        list.add(new App("Built-in Web View", "Display web view in activity directly...",
                R.drawable.ic_aspect_ratio_black_48dp, BuiltInWebViewActivity.class));

        list.add(new App("Songs Player (Work In Progress)", "Display content on split screen...",
                R.mipmap.ic_launcher_round, SongPlayerActivity.class));
*/
        list.add(new App("Youtube", "Youtube look like...",
                R.drawable.youtube_icon_like, YoutubeLikeActivity.class));

                /*

        list.add(new App("My Speech To Text", "Own speech to text implementation...",
                R.drawable.ic_mic, SpeechToTextActivity.class));

        list.add(new App("Activity Recognition", "Display user activity...",
                R.drawable.ic_walking, ActivityRecognitionActivity.class));*/

        list.add(new App("Weather App", "Current weather forecast.. in your city...",
                R.drawable.openweathermap, WeatherActivity.class));


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


    public static ArrayList<RecyclerItem> getRecyclerItems() {

        recyclerItems = new ArrayList<>();

        recyclerItems.add(new RecyclerItem("Dwayne Johnson"));
        recyclerItems.add(new RecyclerItem("Majd Bayassi"));
        recyclerItems.add(new RecyclerItem("Nesrine"));
        recyclerItems.add(new RecyclerItem("Mourad"));
        recyclerItems.add(new RecyclerItem("Mike Tyson"));
        recyclerItems.add(new RecyclerItem("Lyes"));
        recyclerItems.add(new RecyclerItem("Cedric"));
        recyclerItems.add(new RecyclerItem("Michael B. J."));
        recyclerItems.add(new RecyclerItem("Ken Iverson"));
        recyclerItems.add(new RecyclerItem("Max"));
        recyclerItems.add(new RecyclerItem("Halley Becksmann"));
        recyclerItems.add(new RecyclerItem("Carol Danvers"));
        recyclerItems.add(new RecyclerItem("Steph Dann"));
        recyclerItems.add(new RecyclerItem("Danny Walter"));
        recyclerItems.add(new RecyclerItem("Carlos Esposito"));
        recyclerItems.add(new RecyclerItem("Wilmer"));
        recyclerItems.add(new RecyclerItem("Gilles"));
        recyclerItems.add(new RecyclerItem("Yohan"));

        return recyclerItems;
    }
}
