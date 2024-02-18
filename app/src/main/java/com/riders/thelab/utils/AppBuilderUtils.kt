package com.riders.thelab.utils

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.riders.thelab.R
import com.riders.thelab.core.data.local.model.app.App
import com.riders.thelab.core.data.local.model.app.AppBuilder
import com.riders.thelab.feature.biometric.ui.BiometricActivity
import com.riders.thelab.feature.bluetooth.BluetoothActivity
import com.riders.thelab.feature.colors.ColorActivity
import com.riders.thelab.feature.download.DownloadActivity
import com.riders.thelab.feature.kat.ui.KatSplashscreenActivity
import com.riders.thelab.feature.lottie.LottieActivity
import com.riders.thelab.feature.mlkit.ui.chooser.MLKitChooserActivity
import com.riders.thelab.feature.musicrecognition.ui.MusicRecognitionChooserActivity
import com.riders.thelab.feature.schedule.ui.ScheduleActivity
import com.riders.thelab.feature.songplayer.ui.SongPlayerActivity
import com.riders.thelab.feature.streaming.StreamingActivity
import com.riders.thelab.feature.tabs.WorkingTabsActivity
import com.riders.thelab.feature.theaters.TheatersActivity
import com.riders.thelab.feature.weather.ui.WeatherActivity
import com.riders.thelab.ui.builtin.BuiltInWebViewActivity
import com.riders.thelab.ui.camera.CameraActivity
import com.riders.thelab.ui.compose.ComposeActivity
import com.riders.thelab.ui.contacts.ContactsActivity
import com.riders.thelab.ui.customtoast.CustomToastActivity
import com.riders.thelab.ui.floatingview.FloatingViewActivity
import com.riders.thelab.ui.googledrive.GoogleDriveActivity
import com.riders.thelab.ui.googlesignin.GoogleSignInActivity
import com.riders.thelab.ui.locationonmaps.LocationOnMapsActivity
import com.riders.thelab.ui.recycler.RecyclerViewActivity
import com.riders.thelab.ui.screenshot.ScreenShotActivity
import com.riders.thelab.ui.spring.SpringActivity
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
                        .withId(0)
                        .withActivityTitle(context.getString(R.string.activity_title_colors))
                        .withActivityDescription("Change color programmatically...")
                        .withActivityIcon(
                            getDrawableFromIntResource(
                                context,
                                R.drawable.logo_colors
                            )
                        )
                        .withActivityClass(ColorActivity::class.java)
                        .withActivityDate("2024/01/14")
                        .build()
                this.add(colors)

                //Biometric
                val biometric =
                    AppBuilder
                        .withId(1)
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
                        .withId(2)
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
                        .withId(3)
                        .withActivityTitle("Tabs")
                        .withActivityDescription("ViewPager Fragments Tabs...")
                        .withActivityIcon(getDrawableFromIntResource(context, R.drawable.ic_tab))
                        .withActivityClass(WorkingTabsActivity::class.java)
                        .withActivityDate("2015/01/20")
                        .build()
                this.add(tabs)

                //Transitions
                val transitions =
                    AppBuilder
                        .withId(4)
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

                //Contact List
                val contactList =
                    AppBuilder
                        .withId(6)
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
                        .withId(7)
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
                        .withId(8)
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

                // Palette
                val palette =
                    AppBuilder
                        .withId(10)
                        .withActivityTitle(context.getString(R.string.activity_title_palette))
                        .withActivityDescription("Get different color from an image...")
                        .withActivityIcon(
                            getDrawableFromIntResource(
                                context,
                                R.drawable.ic_palette
                            )
                        )
                        .withActivityClass(com.riders.thelab.feature.palette.PaletteActivity::class.java)
                        .withActivityDate("2024/02/12")
                        .build()
                this.add(palette)

                // Theaters
                val theaters =
                    AppBuilder
                        .withId(12)
                        .withActivityTitle(context.getString(R.string.activity_title_theaters))
                        .withActivityDescription("Netflix like but not Netflix...")
                        .withActivityIcon(
                            getDrawableFromIntResource(
                                context,
                                R.drawable.ic_theaters
                            )
                        )
                        .withActivityClass(TheatersActivity::class.java)
                        .withActivityDate("2024/01/24")
                        .build()
                this.add(theaters)

                // Built-In Web View
                val builtInWebView =
                    AppBuilder
                        .withId(14)
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
                        .withId(15)
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
                        .withId(16)
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
                        .withId(17)
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
                        .withId(18)
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
                        .withId(19)
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
                        .withId(20)
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
                        .withId(21)
                        .withActivityTitle("Chat")
                        .withActivityDescription("Realtime chat using firebase realtime database features")
                        .withActivityIcon(getDrawableFromIntResource(context, R.drawable.ic_k_at))
                        .withActivityClass(KatSplashscreenActivity::class.java)
                        .withActivityDate("2023/12/10")
                        .build()
                this.add(kat)


                // SongPlayer
                val songPlayer =
                    AppBuilder
                        .withId(22)
                        .withActivityTitle("Music Player")
                        .withActivityDescription("Play music that is stored on your phone (Live Streaming wip)...")
                        .withActivityIcon(getDrawableFromIntResource(context, R.drawable.ic_music))
                        .withActivityClass(SongPlayerActivity::class.java)
                        .withActivityDate("2024/02/17")
                        .build()
                this.add(songPlayer)

                // Google
                val googleSignIn =
                    AppBuilder
                        .withId(23)
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
                        .withId(24)
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
                        .withId(25)
                        .withActivityTitle("Download")
                        .withActivityDescription("Download file using Android DownloadManager, Kotlin Flow and Retrofit...")
                        .withActivityIcon(
                            getDrawableFromIntResource(
                                context,
                                R.drawable.ic_download
                            )
                        )
                        .withActivityClass(DownloadActivity::class.java)
                        .withActivityDate("2023/12/29")
                        .build()
                this.add(download)

                // lottie
                val lottie =
                    AppBuilder
                        .withId(26)
                        .withActivityTitle(context.getString(R.string.activity_title_lottie))
                        .withActivityDescription("Lottie is a mobile library for Android and iOS that parses Adobe After Effects animations and renders them natively on mobile!...")
                        .withActivityIcon(
                            getDrawableFromIntResource(
                                context,
                                R.drawable.ic_lottie_icon
                            )
                        )
                        .withActivityClass(LottieActivity::class.java)
                        .withActivityDate("2023/12/23")
                        .build()
                this.add(lottie)

                // Bluetooth
                val bluetooth =
                    AppBuilder
                        .withId(27)
                        .withActivityTitle("Bluetooth")
                        .withActivityDescription("Bluetooth feature, retrieve bounded devices and scan available bluetooth connections...")
                        .withActivityIcon(
                            getDrawableFromIntResource(
                                context,
                                R.drawable.ic_bluetooth
                            )
                        )
                        .withActivityClass(BluetoothActivity::class.java)
                        .withActivityDate("2023/12/27")
                        .build()
                this.add(bluetooth)

                // Jetpack Compose
                val jetpackCompose =
                    AppBuilder
                        .withId(28)
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
                        .withId(29)
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
                        .withId(30)
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
                        .withId(31)
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

                // Google ML Kit
                val mlkit =
                    AppBuilder
                        .withId(32)
                        .withActivityTitle(context.getString(R.string.activity_title_google_ml_kit))
                        .withActivityDescription("ML Kit brings Google’s machine learning expertise to mobile developers in a powerful and easy-to-use package...")
                        .withActivityIcon(
                            getDrawableFromIntResource(
                                context,
                                R.drawable.logo_mlkit
                            )
                        )
                        .withActivityClass(MLKitChooserActivity::class.java)
                        .withActivityDate("2024/02/18")
                        .build()
                this.add(mlkit)

                // Streaming
                val streaming =
                    AppBuilder
                        .withId(34)
                        .withActivityTitle(context.getString(com.riders.thelab.core.ui.R.string.activity_title_streaming))
                        .withActivityDescription("Use ExoPlayer to stream media from YouTube, Vimeo, Dailymotion, Twitch, and more...")
                        .withActivityIcon(
                            getDrawableFromIntResource(
                                context,
                                com.riders.thelab.core.ui.R.drawable.ic_streaming
                            )
                        )
                        .withActivityClass(StreamingActivity::class.java)
                        .withActivityDate("2024/01/24")
                        .build()
                this.add(streaming)

                // Wip
                val wip =
                    AppBuilder
                        .withId(40)
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

    private fun getDrawableFromIntResource(context: Context, redId: Int): Drawable {
        return ContextCompat.getDrawable(context, redId)!!
    }
}