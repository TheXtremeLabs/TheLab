package com.riders.thelab.utils

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.riders.thelab.R
import com.riders.thelab.core.data.local.model.app.App
import com.riders.thelab.core.data.local.model.app.AppBuilder
import com.riders.thelab.feature.biometric.ui.BiometricActivity
import com.riders.thelab.feature.musicrecognition.ui.MusicRecognitionChooserActivity
import com.riders.thelab.feature.weather.ui.WeatherActivity
import com.riders.thelab.ui.bluetooth.BluetoothActivity
import com.riders.thelab.ui.builtin.BuiltInWebViewActivity
import com.riders.thelab.ui.camera.CameraActivity
import com.riders.thelab.ui.colors.ColorActivity
import com.riders.thelab.ui.compose.ComposeActivity
import com.riders.thelab.ui.contacts.ContactsActivity
import com.riders.thelab.ui.customtoast.CustomToastActivity
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
import com.riders.thelab.ui.palette.PaletteActivity
import com.riders.thelab.ui.recycler.RecyclerViewActivity
import com.riders.thelab.ui.schedule.ScheduleActivity
import com.riders.thelab.ui.screenshot.ScreenShotActivity
import com.riders.thelab.ui.songplayer.SongPlayerActivity
import com.riders.thelab.ui.speechtotext.SpeechToTextActivity
import com.riders.thelab.ui.spring.SpringActivity
import com.riders.thelab.ui.tabs.WorkingTabsActivity
import com.riders.thelab.ui.theaters.TheatersActivity
import com.riders.thelab.ui.transition.TransitionActivity
import com.riders.thelab.ui.vectordrawables.VectorDrawablesActivity
import com.riders.thelab.ui.youtubelike.YoutubeLikeActivity

object AppBuilderUtils {
    fun buildActivities(context: Context): List<App> =
        mutableListOf<App>()
            .apply {

                // Colors
                val colors =
                    AppBuilder
                        .withId(0L)
                        .withActivityTitle(context.getString(R.string.activity_title_colors))
                        .withActivityDescription("Change color programmatically...")
                        .withActivityIcon(
                            getDrawableFromIntResource(
                                context,
                                R.drawable.logo_colors
                            )
                        )
                        .withActivityClass(ColorActivity::class.java)
                        .withActivityDate("2015/01/20")
                        .build()
                this.add(colors)

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
                        .withActivityDate("2023/07/24")
                        .build()
                this.add(biometric)

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
                        .withActivityDate("2023/01/19")
                        .build()
                this.add(recycler)

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
                this.add(tabs)

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
                this.add(transitions)

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
                this.add(floatingsLabels)

                //Contact List
                val contactList =
                    AppBuilder
                        .withId(6L)
                        .withActivityTitle("Contact List")
                        .withActivityDescription("Fetch contacts from database, add one and more...")
                        .withActivityIcon(
                            getDrawableFromIntResource(
                                context,
                                R.drawable.ic_contacts
                            )
                        )
                        .withActivityClass(ContactsActivity::class.java)
                        .withActivityDate("01/20/2015")
                        .build()
                this.add(contactList)

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
                this.add(locationOnMaps)

                //Schedule Job
                val scheduleJob =
                    AppBuilder
                        .withId(8L)
                        .withActivityTitle("Schedule Job")
                        .withActivityDescription("Own alarm to remind user...")
                        .withActivityIcon(
                            getDrawableFromIntResource(
                                context,
                                R.drawable.ic_schedule
                            )
                        )
                        .withActivityClass(ScheduleActivity::class.java)
                        .withActivityDate("2023/03/23")
                        .build()
                this.add(scheduleJob)

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
                        .withActivityClass(com.riders.thelab.feature.deviceinformation.DeviceInformationActivity::class.java)
                        .withActivityDate("2023/11/15")
                        .build()
                this.add(deviceInformation)

                // Palette
                val palette =
                    AppBuilder
                        .withId(10L)
                        .withActivityTitle(context.getString(R.string.activity_title_palette))
                        .withActivityDescription("Get different color from an image...")
                        .withActivityIcon(
                            getDrawableFromIntResource(
                                context,
                                R.drawable.ic_palette
                            )
                        )
                        .withActivityClass(PaletteActivity::class.java)
                        .withActivityDate("2023/01/16")
                        .build()
                this.add(palette)

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
                this.add(filterListView)

                // Theaters
                val theaters =
                    AppBuilder
                        .withId(12L)
                        .withActivityTitle(context.getString(R.string.activity_title_theaters))
                        .withActivityDescription("Netflix like but not Netflix...")
                        .withActivityIcon(
                            getDrawableFromIntResource(
                                context,
                                R.drawable.ic_theaters
                            )
                        )
                        .withActivityClass(TheatersActivity::class.java)
                        .withActivityDate("2023/06/08")
                        .build()
                this.add(theaters)

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
                this.add(speechToText)

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
                this.add(builtInWebView)

                // Youtube
                val youtubeLike =
                    AppBuilder
                        .withId(15L)
                        .withActivityTitle(context.getString(R.string.activity_title_youtube_like))
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
                this.add(youtubeLike)


                // Weather
                val weather =
                    AppBuilder
                        .withId(16L)
                        .withActivityTitle(context.getString(R.string.activity_title_weather))
                        .withActivityDescription("Current weather forecast in your city...")
                        .withActivityIcon(
                            getDrawableFromIntResource(
                                context,
                                R.drawable.openweathermap
                            )
                        )
                        .withActivityClass(WeatherActivity::class.java)
                        .withActivityDate("2023/05/15")
                        .build()
                this.add(weather)

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
                this.add(floatingWidgets)

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
                this.add(customToast)

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
                this.add(vectorDrawables)

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
                this.add(spring)

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
                this.add(kat)


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
                this.add(songPlayer)

                // Google
                val googleSignIn =
                    AppBuilder
                        .withId(23L)
                        .withActivityTitle("Google Sign In")
                        .withActivityDescription("Exploring Google Sign In Api...")
                        .withActivityIcon(
                            getDrawableFromIntResource(
                                context,
                                R.drawable.googleg_color
                            )
                        )
                        .withActivityClass(GoogleSignInActivity::class.java)
                        .withActivityDate("01/20/2015")
                        .build()
                this.add(googleSignIn)

                val googleDrive =
                    AppBuilder
                        .withId(24L)
                        .withActivityTitle("Google Drive API")
                        .withActivityDescription("Exploring Google Drive Api...")
                        .withActivityIcon(
                            getDrawableFromIntResource(
                                context,
                                R.drawable.googleg_color
                            )
                        )
                        .withActivityClass(GoogleDriveActivity::class.java)
                        .withActivityDate("01/20/2015")
                        .build()
                this.add(googleDrive)

                // download
                val download =
                    AppBuilder
                        .withId(25L)
                        .withActivityTitle("Download")
                        .withActivityDescription("Download file using Kotlin Flow...")
                        .withActivityIcon(
                            getDrawableFromIntResource(
                                context,
                                R.drawable.ic_download
                            )
                        )
                        .withActivityClass(DownloadActivity::class.java)
                        .withActivityDate("2021/09/21")
                        .build()
                this.add(download)

                // lottie
                val lottie =
                    AppBuilder
                        .withId(26L)
                        .withActivityTitle(context.getString(R.string.activity_title_lottie))
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
                this.add(lottie)

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
                this.add(bluetooth)

                // Jetpack Compose
                val jetpackCompose =
                    AppBuilder
                        .withId(28L)
                        .withActivityTitle(context.getString(R.string.activity_title_compose))
                        .withActivityDescription("Jetpack Compose is Android’s modern toolkit for building native UI with less code, powerful tools, and intuitive Kotlin APIs...")
                        .withActivityIcon(
                            getDrawableFromIntResource(
                                context,
                                R.drawable.jetpack_compose
                            )
                        )
                        .withActivityClass(ComposeActivity::class.java)
                        .withActivityDate("2023/01/29")
                        .build()
                this.add(jetpackCompose)


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
                this.add(camera)

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
                this.add(screenShot)

                // Music Recognition
                val musicRecognition =
                    AppBuilder
                        .withId(31L)
                        .withActivityTitle("Music Recognition")
                        .withActivityDescription("Choose ACRCLoud Or Shazam and see...")
                        .withActivityIcon(
                            getDrawableFromIntResource(
                                context,
                                R.drawable.ic_shazam
                            )
                        )
                        .withActivityClass(MusicRecognitionChooserActivity::class.java)
                        .withActivityDate("2023/09/21")
                        .build()
                this.add(musicRecognition)

                // Google ML Kit - Live Barcode
                val liveBarcode =
                    AppBuilder
                        .withId(31L)
                        .withActivityTitle(context.getString(R.string.activity_title_google_ml_kit))
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
                this.add(liveBarcode)

                // Wip
                val wip =
                    AppBuilder
                        .withId(32L)
                        .withActivityTitle("WIP")
                        .withActivityDescription("Coming soon...")
                        .withActivityIcon(
                            getDrawableFromIntResource(
                                context,
                                R.drawable.ic_warning
                            )
                        )
                        .withActivityClass(null)
                        .withActivityDate("1970/01/01")
                        .build()
                this.add(wip)
            }
            .toList()

    fun getDrawableFromIntResource(context: Context, redId: Int): Drawable {
        return ContextCompat.getDrawable(context, redId)!!
    }
}