package com.riders.thelab.utils

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.riders.thelab.R
import com.riders.thelab.data.local.model.app.App
import com.riders.thelab.data.local.model.app.AppBuilder
import com.riders.thelab.ui.biometric.BiometricActivity
import com.riders.thelab.ui.bluetooth.BluetoothActivity
import com.riders.thelab.ui.builtin.BuiltInWebViewActivity
import com.riders.thelab.ui.camera.CameraActivity
import com.riders.thelab.ui.colors.ColorActivity
import com.riders.thelab.ui.compose.ComposeActivity
import com.riders.thelab.ui.contacts.ContactsActivity
import com.riders.thelab.ui.customtoast.CustomToastActivity
import com.riders.thelab.ui.deviceinformation.DeviceInformationActivity
import com.riders.thelab.ui.download.DownloadActivity
import com.riders.thelab.ui.filterlistview.FilterListViewActivity
import com.riders.thelab.ui.floatinglabels.FloatingLabelsActivity
import com.riders.thelab.ui.floatingview.FloatingViewActivity
import com.riders.thelab.ui.googledrive.GoogleDriveActivity
import com.riders.thelab.ui.googlemlkit.LiveBarcodeScanningActivity
import com.riders.thelab.ui.googlesignin.GoogleSignInActivity
import com.riders.thelab.ui.kat.KatActivity
import com.riders.thelab.ui.locationonmaps.LocationOnMapsActivity
import com.riders.thelab.ui.lottie.LottieActivity
import com.riders.thelab.ui.multipane.MultipaneActivity
import com.riders.thelab.ui.palette.PaletteActivity
import com.riders.thelab.ui.recycler.RecyclerViewActivity
import com.riders.thelab.ui.schedule.ScheduleActivity
import com.riders.thelab.ui.screenshot.ScreenShotActivity
import com.riders.thelab.ui.songplayer.SongPlayerActivity
import com.riders.thelab.ui.speechtotext.SpeechToTextActivity
import com.riders.thelab.ui.spring.SpringActivity
import com.riders.thelab.ui.tabs.WorkingTabsActivity
import com.riders.thelab.ui.transition.TransitionActivity
import com.riders.thelab.ui.vectordrawables.VectorDrawablesActivity
import com.riders.thelab.ui.weather.WeatherActivity
import com.riders.thelab.ui.youtubelike.YoutubeLikeActivity

class AppBuilderUtils {

    companion object {
        fun buildActivities(context: Context): List<App> {

            val list: MutableList<App> = ArrayList()

            // Colors
            val colors =
                AppBuilder
                    .withId(0L)
                    .withActivityTitle("Colors")
                    .withActivityDescription("Change color programmatically...")
                    .withActivityIcon(getDrawableFromIntResource(context, R.drawable.logo_colors))
                    .withActivityClass(ColorActivity::class.java)
                    .withActivityDate("2015/01/20")
                    .build()
            list.add(colors)

            //Biometric
            val biometric =
                AppBuilder
                    .withId(1L)
                    .withActivityTitle("Biometric")
                    .withActivityDescription("Check biometric hardware and test it...")
                    .withActivityIcon(
                        getDrawableFromIntResource(
                            context,
                            R.drawable.ic_fingerprint
                        )
                    )
                    .withActivityClass(BiometricActivity::class.java)
                    .withActivityDate("2020/12/24")
                    .build()
            list.add(biometric)

            //Recycler
            val recycler =
                AppBuilder
                    .withId(2L)
                    .withActivityTitle("Recycler")
                    .withActivityDescription("Recycler Basics and best practices...")
                    .withActivityIcon(
                        getDrawableFromIntResource(
                            context,
                            R.drawable.ic_filter_list
                        )
                    )
                    .withActivityClass(RecyclerViewActivity::class.java)
                    .withActivityDate("01/20/2015")
                    .build()
            list.add(recycler)

            //Tabs
            val tabs =
                AppBuilder
                    .withId(3L)
                    .withActivityTitle("Tabs")
                    .withActivityDescription("ViewPager Fragments Tabs...")
                    .withActivityIcon(getDrawableFromIntResource(context, R.drawable.ic_tab))
                    .withActivityClass(WorkingTabsActivity::class.java)
                    .withActivityDate("01/20/2015")
                    .build()
            list.add(tabs)

            //Transitions
            val transitions =
                AppBuilder
                    .withId(4L)
                    .withActivityTitle("Transitions")
                    .withActivityDescription("Start a new activity with awesome animations...")
                    .withActivityIcon(
                        getDrawableFromIntResource(
                            context,
                            R.drawable.ic_flip_to_back
                        )
                    )
                    .withActivityClass(TransitionActivity::class.java)
                    .withActivityDate("01/20/2015")
                    .build()
            list.add(transitions)

            //Floating Labels
            val floatingsLabels =
                AppBuilder
                    .withId(5L)
                    .withActivityTitle("Floating Labels")
                    .withActivityDescription("Floating Labels Form...")
                    .withActivityIcon(
                        getDrawableFromIntResource(
                            context,
                            R.drawable.ic_outline_label
                        )
                    )
                    .withActivityClass(FloatingLabelsActivity::class.java)
                    .withActivityDate("01/20/2015")
                    .build()
            list.add(floatingsLabels)

            //Contact List
            val contactList =
                AppBuilder
                    .withId(6L)
                    .withActivityTitle("Contact List")
                    .withActivityDescription("Fetch contacts from database, add one and more...")
                    .withActivityIcon(getDrawableFromIntResource(context, R.drawable.ic_contacts))
                    .withActivityClass(ContactsActivity::class.java)
                    .withActivityDate("01/20/2015")
                    .build()
            list.add(contactList)

            //Location On Maps
            val locationOnMaps =
                AppBuilder
                    .withId(7L)
                    .withActivityTitle("Location On Maps")
                    .withActivityDescription("Display User location on map...")
                    .withActivityIcon(
                        getDrawableFromIntResource(
                            context,
                            R.drawable.ic_location_on
                        )
                    )
                    .withActivityClass(LocationOnMapsActivity::class.java)
                    .withActivityDate("01/20/2015")
                    .build()
            list.add(locationOnMaps)

            //Schedule Job
            val scheduleJob =
                AppBuilder
                    .withId(8L)
                    .withActivityTitle("Schedule Job")
                    .withActivityDescription("Own alarm to remind user...")
                    .withActivityIcon(getDrawableFromIntResource(context, R.drawable.ic_schedule))
                    .withActivityClass(ScheduleActivity::class.java)
                    .withActivityDate("01/20/2015")
                    .build()
            list.add(scheduleJob)

            //Devices Information
            val deviceInformation =
                AppBuilder
                    .withId(9L)
                    .withActivityTitle("Devices Information")
                    .withActivityDescription("Display device info...")
                    .withActivityIcon(
                        getDrawableFromIntResource(
                            context,
                            R.drawable.ic_device_information
                        )
                    )
                    .withActivityClass(DeviceInformationActivity::class.java)
                    .withActivityDate("01/20/2015")
                    .build()
            list.add(deviceInformation)

            // Palette
            val palette =
                AppBuilder
                    .withId(10L)
                    .withActivityTitle("Palette")
                    .withActivityDescription("Get different color from an image...")
                    .withActivityIcon(getDrawableFromIntResource(context, R.drawable.ic_palette))
                    .withActivityClass(PaletteActivity::class.java)
                    .withActivityDate("01/20/2015")
                    .build()
            list.add(palette)

            // Filter ListView
            val filterListView =
                AppBuilder
                    .withId(11L)
                    .withActivityTitle("Filter ListView")
                    .withActivityDescription("ListView with filter...")
                    .withActivityIcon(
                        getDrawableFromIntResource(
                            context,
                            R.drawable.ic_filter_list
                        )
                    )
                    .withActivityClass(FilterListViewActivity::class.java)
                    .withActivityDate("01/20/2015")
                    .build()
            list.add(filterListView)

            // Multi Pane
            val multiPane =
                AppBuilder
                    .withId(12L)
                    .withActivityTitle("Multi Pane")
                    .withActivityDescription("Display content on split screen...")
                    .withActivityIcon(
                        getDrawableFromIntResource(
                            context,
                            R.drawable.ic_aspect_ratio
                        )
                    )
                    .withActivityClass(MultipaneActivity::class.java)
                    .withActivityDate("01/20/2015")
                    .build()
            list.add(multiPane)

            //Speech to Text
            val speechToText =
                AppBuilder
                    .withId(13L)
                    .withActivityTitle("My Speech To Text")
                    .withActivityDescription("Own speech to text implementation...")
                    .withActivityIcon(getDrawableFromIntResource(context, R.drawable.ic_mic))
                    .withActivityClass(SpeechToTextActivity::class.java)
                    .withActivityDate("01/20/2015")
                    .build()
            list.add(speechToText)

            // Built-In Web View
            val builtInWebView =
                AppBuilder
                    .withId(14L)
                    .withActivityTitle("Built-in Web View")
                    .withActivityDescription("Display web view in activity directly...")
                    .withActivityIcon(
                        getDrawableFromIntResource(
                            context,
                            R.drawable.ic_alternate_email
                        )
                    )
                    .withActivityClass(BuiltInWebViewActivity::class.java)
                    .withActivityDate("01/20/2015")
                    .build()
            list.add(builtInWebView)

            // Youtube
            val youtubeLike =
                AppBuilder
                    .withId(15L)
                    .withActivityTitle("Youtube")
                    .withActivityDescription("Youtube look like...")
                    .withActivityIcon(
                        getDrawableFromIntResource(
                            context,
                            R.drawable.youtube_icon_like
                        )
                    )
                    .withActivityClass(YoutubeLikeActivity::class.java)
                    .withActivityDate("01/20/2015")
                    .build()
            list.add(youtubeLike)


            // Weather
            val weather =
                AppBuilder
                    .withId(16L)
                    .withActivityTitle("Weather")
                    .withActivityDescription("Current weather forecast in your city...")
                    .withActivityIcon(
                        getDrawableFromIntResource(
                            context,
                            R.drawable.openweathermap
                        )
                    )
                    .withActivityClass(WeatherActivity::class.java)
                    .withActivityDate("01/20/2015")
                    .build()
            list.add(weather)

            // Floating Widgets
            val floatingWidgets =
                AppBuilder
                    .withId(17L)
                    .withActivityTitle("Floating Widget")
                    .withActivityDescription("Create a floating widget that you can move around on the screen...")
                    .withActivityIcon(
                        getDrawableFromIntResource(
                            context,
                            R.drawable.ic_flip_to_back
                        )
                    )
                    .withActivityClass(FloatingViewActivity::class.java)
                    .withActivityDate("01/20/2015")
                    .build()
            list.add(floatingWidgets)

            // Custom Toast
            val customToast =
                AppBuilder
                    .withId(18L)
                    .withActivityTitle("Custom Toast")
                    .withActivityDescription("Custom Toast Layout...")
                    .withActivityIcon(
                        getDrawableFromIntResource(
                            context,
                            R.drawable.ic_announcement
                        )
                    )
                    .withActivityClass(CustomToastActivity::class.java)
                    .withActivityDate("01/20/2015")
                    .build()
            list.add(customToast)

            // Vector Drawables
            val vectorDrawables =
                AppBuilder
                    .withId(19L)
                    .withActivityTitle("Vector Drawables")
                    .withActivityDescription("Animated, scale, transform vector drawables...")
                    .withActivityIcon(
                        getDrawableFromIntResource(
                            context,
                            R.drawable.ic_aspect_ratio
                        )
                    )
                    .withActivityClass(VectorDrawablesActivity::class.java)
                    .withActivityDate("01/20/2015")
                    .build()
            list.add(vectorDrawables)

            // Spring
            val spring =
                AppBuilder
                    .withId(20L)
                    .withActivityTitle("Spring")
                    .withActivityDescription("Physics-based motion is driven by force. Spring force is one such force that guides interactivity and motion....")
                    .withActivityIcon(
                        getDrawableFromIntResource(
                            context,
                            R.drawable.ic_filter_center_focus
                        )
                    )
                    .withActivityClass(SpringActivity::class.java)
                    .withActivityDate("01/20/2015")
                    .build()
            list.add(spring)

            // Kat
            val kat =
                AppBuilder
                    .withId(21L)
                    .withActivityTitle("Chat")
                    .withActivityDescription("Realtime chat using firebase realtime database features")
                    .withActivityIcon(getDrawableFromIntResource(context, R.drawable.ic_k_at))
                    .withActivityClass(KatActivity::class.java)
                    .withActivityDate("01/20/2015")
                    .build()
            list.add(kat)


            // SongPlayer
            val songPlayer =
                AppBuilder
                    .withId(22L)
                    .withActivityTitle("Music Player")
                    .withActivityDescription("Play music that is stored on your phone (Live Streaming wip)...")
                    .withActivityIcon(getDrawableFromIntResource(context, R.drawable.ic_music))
                    .withActivityClass(SongPlayerActivity::class.java)
                    .withActivityDate("01/20/2015")
                    .build()
            list.add(songPlayer)

            // Google
            val googleSignIn =
                AppBuilder
                    .withId(23L)
                    .withActivityTitle("Google Sign In")
                    .withActivityDescription("Exploring Google Sign In Api...")
                    .withActivityIcon(getDrawableFromIntResource(context, R.drawable.googleg_color))
                    .withActivityClass(GoogleSignInActivity::class.java)
                    .withActivityDate("01/20/2015")
                    .build()
            list.add(googleSignIn)

            val googleDrive =
                AppBuilder
                    .withId(24L)
                    .withActivityTitle("Google Drive API")
                    .withActivityDescription("Exploring Google Drive Api...")
                    .withActivityIcon(getDrawableFromIntResource(context, R.drawable.googleg_color))
                    .withActivityClass(GoogleDriveActivity::class.java)
                    .withActivityDate("01/20/2015")
                    .build()
            list.add(googleDrive)

            // download
            val download =
                AppBuilder
                    .withId(25L)
                    .withActivityTitle("Download")
                    .withActivityDescription("Download file using Kotlin Flow...")
                    .withActivityIcon(getDrawableFromIntResource(context, R.drawable.ic_download))
                    .withActivityClass(DownloadActivity::class.java)
                    .withActivityDate("2021/09/21")
                    .build()
            list.add(download)

            // lottie
            val lottie =
                AppBuilder
                    .withId(26L)
                    .withActivityTitle("Lottie")
                    .withActivityDescription("Lottie is a mobile library for Android and iOS that parses Adobe After Effects animations and renders them natively on mobile!...")
                    .withActivityIcon(
                        getDrawableFromIntResource(
                            context,
                            R.drawable.ic_lottie_icon
                        )
                    )
                    .withActivityClass(LottieActivity::class.java)
                    .withActivityDate("2021/09/21")
                    .build()
            list.add(lottie)

            // Bluetooth
            val bluetooth =
                AppBuilder
                    .withId(27L)
                    .withActivityTitle("Bluetooth")
                    .withActivityDescription("Bluetooth feature, retrieve bounded devices and scan available bluetooth connections...")
                    .withActivityIcon(
                        getDrawableFromIntResource(
                            context,
                            R.drawable.ic_bluetooth
                        )
                    )
                    .withActivityClass(BluetoothActivity::class.java)
                    .withActivityDate("2021/09/29")
                    .build()
            list.add(bluetooth)

            // Jetpack Compose
            val jetpackCompose =
                AppBuilder
                    .withId(28L)
                    .withActivityTitle("Compose")
                    .withActivityDescription("Jetpack Compose is Android’s modern toolkit for building native UI with less code, powerful tools, and intuitive Kotlin APIs...")
                    .withActivityIcon(
                        getDrawableFromIntResource(
                            context,
                            R.drawable.jetpack_compose
                        )
                    )
                    .withActivityClass(ComposeActivity::class.java)
                    .withActivityDate("2021/10/13")
                    .build()
            list.add(jetpackCompose)


            // Camera
            val camera =
                AppBuilder
                    .withId(29L)
                    .withActivityTitle("Camera")
                    .withActivityDescription("CameraX is a Jetpack support library, built to help you make camera app development easier....")
                    .withActivityIcon(
                        getDrawableFromIntResource(
                            context,
                            R.drawable.ic_camera
                        )
                    )
                    .withActivityClass(CameraActivity::class.java)
                    .withActivityDate("2021/10/13")
                    .build()
            list.add(camera)

            // Screen Shot
            val screenShot =
                AppBuilder
                    .withId(30L)
                    .withActivityTitle("Screen Shot")
                    .withActivityDescription("Screen Shot the device display programmatically...")
                    .withActivityIcon(
                        getDrawableFromIntResource(
                            context,
                            R.drawable.ic_fullscreen
                        )
                    )
                    .withActivityClass(ScreenShotActivity::class.java)
                    .withActivityDate("2021/10/13")
                    .build()
            list.add(screenShot)

            // Google ML Kit - Live Barcode
            val liveBarcode =
                AppBuilder
                    .withId(31L)
                    .withActivityTitle("Google Ml Kit Live Barcode")
                    .withActivityDescription("ML Kit brings Google’s machine learning expertise to mobile developers in a powerful and easy-to-use package...")
                    .withActivityIcon(
                        getDrawableFromIntResource(
                            context,
                            R.drawable.logo_mlkit
                        )
                    )
                    .withActivityClass(LiveBarcodeScanningActivity::class.java)
                    .withActivityDate("2022/01/28")
                    .build()
            list.add(liveBarcode)

            // Wip
            val wip =
                AppBuilder
                    .withId(32L)
                    .withActivityTitle("WIP")
                    .withActivityDescription("Coming soon...")
                    .withActivityIcon(getDrawableFromIntResource(context, R.drawable.ic_warning))
                    .withActivityClass(null)
                    .withActivityDate("1970/01/01")
                    .build()
            list.add(wip)

            return list
        }

         fun getDrawableFromIntResource(context: Context, redId: Int): Drawable {
            return ContextCompat.getDrawable(context, redId)!!
        }
    }
}