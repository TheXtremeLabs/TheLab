package com.riders.thelab.utils

import com.riders.thelab.R
import com.riders.thelab.data.local.model.app.App
import com.riders.thelab.data.local.model.app.AppBuilder
import com.riders.thelab.ui.biometric.BiometricActivity
import com.riders.thelab.ui.builtin.BuiltInWebViewActivity
import com.riders.thelab.ui.colors.ColorActivity
import com.riders.thelab.ui.contacts.ContactsActivity
import com.riders.thelab.ui.customtoast.CustomToastActivity
import com.riders.thelab.ui.deviceinformation.DeviceInformationActivity
import com.riders.thelab.ui.filterlistview.FilterListViewActivity
import com.riders.thelab.ui.floatinglabels.FloatingLabelsActivity
import com.riders.thelab.ui.floatingview.FloatingViewActivity
import com.riders.thelab.ui.kat.KatActivity
import com.riders.thelab.ui.locationonmaps.LocationOnMapsActivity
import com.riders.thelab.ui.multipane.MultipaneActivity
import com.riders.thelab.ui.palette.PaletteActivity
import com.riders.thelab.ui.recycler.RecyclerViewActivity
import com.riders.thelab.ui.schedule.ScheduleActivity
import com.riders.thelab.ui.speechtotext.SpeechToTextActivity
import com.riders.thelab.ui.spring.SpringActivity
import com.riders.thelab.ui.tabs.WorkingTabsActivity
import com.riders.thelab.ui.transition.TransitionActivity
import com.riders.thelab.ui.vectordrawables.VectorDrawablesActivity
import com.riders.thelab.ui.weather.WeatherActivity
import com.riders.thelab.ui.youtubelike.YoutubeLikeActivity
import java.util.*

class AppBuilderUtils {

    companion object {
        fun buildActivities(): List<App> {

            val list: MutableList<App> = ArrayList()

            // Colors
            val colors =
                AppBuilder
                    .withActivityTitle("Colors")
                    .withActivityDescription("Change color programmatically...")
                    .withActivityIcon(R.drawable.logo_colors)
                    .withActivityClass(ColorActivity::class.java)
                    .build()
            list.add(colors)

            //Biometric
            val biometric =
                AppBuilder
                    .withActivityTitle("Biometric")
                    .withActivityDescription("Check biometric hardware and test it...")
                    .withActivityIcon(R.drawable.ic_fingerprint)
                    .withActivityClass(BiometricActivity::class.java)
                    .build()
            list.add(biometric)

            //Recycler
            val recycler =
                AppBuilder
                    .withActivityTitle("Recycler")
                    .withActivityDescription("Recycler Basics and best practices...")
                    .withActivityIcon(R.drawable.ic_filter_list)
                    .withActivityClass(RecyclerViewActivity::class.java)
                    .build()
            list.add(recycler)

            //Tabs
            val tabs =
                AppBuilder
                    .withActivityTitle("Tabs")
                    .withActivityDescription("ViewPager Fragments Tabs...")
                    .withActivityIcon(R.drawable.ic_tab)
                    .withActivityClass(WorkingTabsActivity::class.java)
                    .build()
            list.add(tabs)

            //Transitions
            val transitions =
                AppBuilder
                    .withActivityTitle("Transitions")
                    .withActivityDescription("Start a new activity with awesome animations...")
                    .withActivityIcon(R.drawable.ic_flip_to_back)
                    .withActivityClass(TransitionActivity::class.java)
                    .build()
            list.add(transitions)

            //Floating Labels
            val floatingsLabels =
                AppBuilder
                    .withActivityTitle("Floating Labels")
                    .withActivityDescription("Floating Labels Form...")
                    .withActivityIcon(R.drawable.ic_outline_label)
                    .withActivityClass(FloatingLabelsActivity::class.java)
                    .build()
            list.add(floatingsLabels)

            //Contact List
            val contactList =
                AppBuilder
                    .withActivityTitle("Contact List")
                    .withActivityDescription("Fetch contacts from database, add one and more...")
                    .withActivityIcon(R.drawable.ic_contacts)
                    .withActivityClass(ContactsActivity::class.java)
                    .build()
            list.add(contactList)

            //Location On Maps
            val locationOnMaps =
                AppBuilder
                    .withActivityTitle("Location On Maps")
                    .withActivityDescription("Display User location on map...")
                    .withActivityIcon(R.drawable.ic_location_on)
                    .withActivityClass(LocationOnMapsActivity::class.java)
                    .build()
            list.add(locationOnMaps)

            //Schedule Job
            val scheduleJob =
                AppBuilder
                    .withActivityTitle("Schedule Job")
                    .withActivityDescription("Own alarm to remind user...")
                    .withActivityIcon(R.drawable.ic_schedule)
                    .withActivityClass(ScheduleActivity::class.java)
                    .build()
            list.add(scheduleJob)

            //Devices Information
            val deviceInformation =
                AppBuilder
                    .withActivityTitle("Devices Information")
                    .withActivityDescription("Display device info...")
                    .withActivityIcon(R.drawable.ic_device_information)
                    .withActivityClass(DeviceInformationActivity::class.java)
                    .build()
            list.add(deviceInformation)

            // Palette
            val palette =
                AppBuilder
                    .withActivityTitle("Palette")
                    .withActivityDescription("Get different color from an image...")
                    .withActivityIcon(R.drawable.ic_palette)
                    .withActivityClass(PaletteActivity::class.java)
                    .build()
            list.add(palette)

            // Filter ListView
            val filterListView =
                AppBuilder
                    .withActivityTitle("Filter ListView")
                    .withActivityDescription("ListView with filter...")
                    .withActivityIcon(R.drawable.ic_filter_list)
                    .withActivityClass(FilterListViewActivity::class.java)
                    .build()
            list.add(filterListView)

            // Multi Pane
            val multiPane =
                AppBuilder
                    .withActivityTitle("Multi Pane")
                    .withActivityDescription("Display content on split screen...")
                    .withActivityIcon(R.drawable.ic_aspect_ratio)
                    .withActivityClass(MultipaneActivity::class.java)
                    .build()
            list.add(multiPane)

            //Speech to Text
            val speechToText =
                AppBuilder
                    .withActivityTitle("My Speech To Text")
                    .withActivityDescription("Own speech to text implementation...")
                    .withActivityIcon(R.drawable.ic_mic)
                    .withActivityClass(SpeechToTextActivity::class.java)
                    .build()
            list.add(speechToText)

            // Built-In Web View
            val builtInWebView =
                AppBuilder
                    .withActivityTitle("Built-in Web View")
                    .withActivityDescription("Display web view in activity directly...")
                    .withActivityIcon(R.drawable.ic_alternate_email)
                    .withActivityClass(BuiltInWebViewActivity::class.java)
                    .build()
            list.add(builtInWebView)

            // Youtube
            val youtubeLike =
                AppBuilder
                    .withActivityTitle("Youtube")
                    .withActivityDescription("Youtube look like...")
                    .withActivityIcon(R.drawable.youtube_icon_like)
                    .withActivityClass(YoutubeLikeActivity::class.java)
                    .build()
            list.add(youtubeLike)


            // Weather
            val weather =
                AppBuilder
                    .withActivityTitle("Weather")
                    .withActivityDescription("Current weather forecast in your city...")
                    .withActivityIcon(R.drawable.openweathermap)
                    .withActivityClass(WeatherActivity::class.java)
                    .build()
            list.add(weather)

            // Floating Widgets
            val floatingWidgets =
                AppBuilder
                    .withActivityTitle("Floating Widget")
                    .withActivityDescription("Create a floating widget that you can move around on the screen...")
                    .withActivityIcon(R.drawable.ic_flip_to_back)
                    .withActivityClass(FloatingViewActivity::class.java)
                    .build()
            list.add(floatingWidgets)

            // Custom Toast
            val customToast =
                AppBuilder
                    .withActivityTitle("Custom Toast")
                    .withActivityDescription("Custom Toast Layout...")
                    .withActivityIcon(R.drawable.ic_announcement)
                    .withActivityClass(CustomToastActivity::class.java)
                    .build()
            list.add(customToast)

            // Vector Drawables
            val vectorDrawables =
                AppBuilder
                    .withActivityTitle("Vector Drawables")
                    .withActivityDescription("Animated, scale, transform vector drawables...")
                    .withActivityIcon(R.drawable.ic_aspect_ratio)
                    .withActivityClass(VectorDrawablesActivity::class.java)
                    .build()
            list.add(vectorDrawables)

            // Spring
            val spring =
                AppBuilder
                    .withActivityTitle("Spring")
                    .withActivityDescription("Physics-based motion is driven by force. Spring force is one such force that guides interactivity and motion....")
                    .withActivityIcon(R.drawable.ic_filter_center_focus)
                    .withActivityClass(SpringActivity::class.java)
                    .build()
            list.add(spring)

            // Kat
            val kat =
                AppBuilder
                    .withActivityTitle("Chat")
                    .withActivityDescription("Realtime chat using firebase realtime database features")
                    .withActivityIcon(R.drawable.ic_k_at)
                    .withActivityClass(KatActivity::class.java)
                    .build()
            list.add(kat)

            // Wip
            val wip =
                AppBuilder
                    .withActivityTitle("WIP")
                    .withActivityDescription("Coming soon...")
                    .withActivityIcon(R.drawable.ic_warning)
                    .withActivityClass(null)
                    .build()
            list.add(wip)

            return list
        }
    }
}