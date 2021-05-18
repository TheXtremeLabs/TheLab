package com.riders.thelab.ui.mainactivity

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.wifi.WifiManager
import android.os.Bundle
import android.provider.Settings
import android.text.Html
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import com.riders.thelab.R
import com.riders.thelab.core.interfaces.ConnectivityListener
import com.riders.thelab.core.utils.LabCompatibilityManager
import com.riders.thelab.core.utils.LabNetworkManagerNewAPI
import com.riders.thelab.core.utils.UIManager
import com.riders.thelab.data.local.model.App
import com.riders.thelab.databinding.ActivityMainBinding
import com.riders.thelab.ui.mainactivity.fragment.news.NewsFragment
import com.riders.thelab.ui.mainactivity.fragment.time.TimeFragment
import com.riders.thelab.ui.mainactivity.fragment.weather.WeatherFragment
import com.riders.thelab.utils.Validator
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity(),
        MainActivityAppClickListener,
        ConnectivityListener,
        MenuItem.OnMenuItemClickListener {

    private lateinit var viewBinding: ActivityMainBinding

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private var pagerAdapter: FragmentStateAdapter? = null
    private var mConnectivityManager: ConnectivityManager? = null
    private var networkManager: LabNetworkManagerNewAPI? = null
    private var menu: Menu? = null
    private var fragmentList: MutableList<Fragment>? = null
    private var layoutDots: LinearLayout? = null
    private lateinit var dots: Array<TextView?>

    /*
     * ViewPager page change listener
     */
    var pageChangeCallback: OnPageChangeCallback = object : OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            addBottomDots(position)
        }
    }

    private val mViewModel: MainViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // In Activity's onCreate() for instance
        // make fully Android Transparent Status bar

        // In Activity's onCreate() for instance
        // make fully Android Transparent Status bar
        if (LabCompatibilityManager.isLollipop()) {
            when (this.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                Configuration.UI_MODE_NIGHT_YES -> {
                    val w = window
                    w.statusBarColor = Color.TRANSPARENT
                }
                Configuration.UI_MODE_NIGHT_NO,
                Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                }
            }
            window.navigationBarColor = ContextCompat.getColor(this, R.color.default_dark)
        }

        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        if (!LabCompatibilityManager.isTablet(this)) {
            initViews()
        } else {
            bindTabletViews()
        }

        initViewModelsObservers()

    }

    override fun onStart() {
        super.onStart()
        mConnectivityManager = this.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager?
        networkManager = LabNetworkManagerNewAPI(this)
    }

    override fun onPause() {
        super.onPause()

        mConnectivityManager!!.unregisterNetworkCallback(networkManager!!)
    }

    override fun onResume() {
        super.onResume()

        // register connection status listener
        val request = NetworkRequest.Builder()
                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build()

        mConnectivityManager!!.registerNetworkCallback(request, networkManager!!)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        Timber.d("onCreateOptionsMenu()")

        this.menuInflater.inflate(R.menu.menu_main, menu)
        this.menu = menu

        mViewModel.checkConnection(this)
        return true
    }

    @SuppressLint("InlinedApi")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.connection_icon -> {
                UIManager.showActionInToast(this, "Wifi clicked")
                val wifiManager: WifiManager =
                        this.applicationContext.getSystemService(WIFI_SERVICE) as WifiManager

                if (!LabCompatibilityManager.isAndroid10()) {
                    val isWifi = wifiManager.isWifiEnabled
                    wifiManager.isWifiEnabled = !isWifi
                } else {
                    Timber.e("For applications targeting android.os.Build.VERSION_CODES Q or above, this API will always fail and return false")

                    /*
                        ACTION_INTERNET_CONNECTIVITY Shows settings related to internet connectivity, such as Airplane mode, Wi-Fi, and Mobile Data.
                        ACTION_WIFI Shows Wi-Fi settings, but not the other connectivity settings. This is useful for apps that need a Wi-Fi connection to perform large uploads or downloads.
                        ACTION_NFC Shows all settings related to near-field communication (NFC).
                        ACTION_VOLUME Shows volume settings for all audio streams.
                     */
                    val panelIntent = Intent(Settings.Panel.ACTION_WIFI)
                    this.startActivityForResult(panelIntent, 955)
                }
            }
            R.id.action_settings -> UIManager.showActionInToast(this, "Settings clicked")
            R.id.info_icon -> showBottomSheetDialogFragment()
            R.id.action_force_crash -> throw java.lang.RuntimeException("This is a crash")
            else -> {
            }
        }

        return true
    }


    override fun onDestroy() {
        Timber.d("onDestroy()")
        Timber.d("unregister network callback()")
        networkManager?.let { mConnectivityManager?.unregisterNetworkCallback(it) };
        super.onDestroy()
    }


    /////////////////////////////////////
    //
    // CLASS METHODS
    //
    /////////////////////////////////////
    private fun initViewModelsObservers() {

        mViewModel
                .getConnectionStatus()
                .observe(
                        this,
                        { connectionStatus ->
                            UIManager.showConnectionStatusInSnackBar(this, connectionStatus)
                            updateToolbarConnectionIcon(connectionStatus)
                        })

    }

    /**
     * Set up views (recyclerviews, spinner, etc...)
     */

    private fun initViews() {
        initCollapsingToolbar()
        initToolbar()

        // Instantiate a ViewPager2 and a PagerAdapter.
        fragmentList = ArrayList()

        // add Fragments in your ViewPagerFragmentAdapter class
        fragmentList!!.add(TimeFragment())
        fragmentList!!.add(WeatherFragment())
        fragmentList!!.add(NewsFragment())
        pagerAdapter = ViewPager2Adapter(
                this.supportFragmentManager,
                this.lifecycle,
                fragmentList)
        // set Orientation in your ViewPager2
        viewBinding.includeToolbarLayout?.viewPager?.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        viewBinding.includeToolbarLayout?.viewPager?.adapter = pagerAdapter
        viewBinding.includeToolbarLayout?.viewPager?.registerOnPageChangeCallback(pageChangeCallback)
    }

    private fun bindTabletViews() {
        this.supportFragmentManager
                .beginTransaction()
                .add(
                        R.id.fragment_time,
                        TimeFragment.newInstance())
                .commit()
        this.supportFragmentManager
                .beginTransaction()
                .add(
                        R.id.fragment_weather,
                        WeatherFragment.newInstance())
                .commit()
    }

    private fun addBottomDots(currentPage: Int) {

        dots = arrayOfNulls(fragmentList!!.size)

        val colorsActive: IntArray = this.resources.getIntArray(R.array.array_dot_active)
        val colorsInactive: IntArray = this.resources.getIntArray(R.array.array_dot_inactive)

        layoutDots?.removeAllViews()

        for (i in dots.indices) {
            dots[i] = TextView(this)
            dots[i]?.text = Html.fromHtml("&#8226;")
            dots[i]?.textSize = 35f
            dots[i]?.setTextColor(colorsInactive[currentPage])
            layoutDots?.addView(dots[i])
        }

        if (dots.isNotEmpty())
            dots[currentPage]?.setTextColor(colorsActive[currentPage])
    }


    /**
     * Initializing collapsing toolbar
     * Will show and hide the toolbar txtPostTitle on scroll
     */
    private fun initCollapsingToolbar() {
        viewBinding.includeToolbarLayout?.collapsingToolbar?.title = " "
        viewBinding.includeToolbarLayout?.appbar?.setExpanded(true)

        // hiding & showing the txtPostTitle when toolbar expanded & collapsed
        viewBinding.includeToolbarLayout?.appbar?.addOnOffsetChangedListener(
                object : OnOffsetChangedListener {
                    var isShow = false
                    var scrollRange = -1
                    override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                        if (scrollRange == -1) {
                            scrollRange = appBarLayout.totalScrollRange
                        }
                        if (scrollRange + verticalOffset == 0) {
                            // Toolbar is collapsed
                            viewBinding.includeToolbarLayout?.collapsingToolbar?.title =
                                    this@MainActivity.resources.getString(R.string.app_name)
                            showMenuButtons()
                            isShow = true
                        } else if (isShow) {
                            // Toolbar is expanded
                            viewBinding.includeToolbarLayout?.collapsingToolbar?.title = " "
                            hideMenuButtons()
                            isShow = false
                        }
                    }
                })
    }


    /**
     * Setup Toolbar menu icon differently than the basic way because of the collapsing toolbar
     * <p>
     * We want the button to show up only when the tollbar is collapsed
     * <p>
     * https://stackoverflow.com/questions/10692755/how-do-i-hide-a-menu-item-in-the-actionbar#:~:text=The%20best%20way%20to%20hide,menu%20inside%20the%20same%20group.&text=Then%2C%20on%20your%20activity%20(preferable,visibility%20to%20false%20or%20true.
     */
    private fun initToolbar() {
        viewBinding.includeToolbarLayout?.toolbar?.inflateMenu(R.menu.menu_main)
        menu = viewBinding.includeToolbarLayout?.toolbar?.menu

        hideMenuButtons()

        viewBinding.includeToolbarLayout?.toolbar?.setOnMenuItemClickListener { item: MenuItem? ->
            onMenuItemClick(item)
        }
    }

    fun showBottomSheetDialogFragment() {
        val bottomSheetFragment = BottomSheetFragment()
        bottomSheetFragment.show(
                this.supportFragmentManager,
                bottomSheetFragment.tag)
    }

    @SuppressLint("InlinedApi")
    override fun onMenuItemClick(item: MenuItem?): Boolean {
        Timber.d("onMenuItemClick()")
        when (item!!.itemId) {
            R.id.connection_icon -> {
                UIManager.showActionInToast(this, "Wifi clicked")
                val wifiManager: WifiManager =
                        this
                                .applicationContext
                                .getSystemService(WIFI_SERVICE) as WifiManager
                if (!LabCompatibilityManager.isAndroid10()) {
                    val isWifi = wifiManager.isWifiEnabled
                    wifiManager.isWifiEnabled = !isWifi
                } else {
                    Timber.e("For applications targeting android.os.Build.VERSION_CODES Q or above, this API will always fail and return false")

                    /*
                        ACTION_INTERNET_CONNECTIVITY Shows settings related to internet connectivity, such as Airplane mode, Wi-Fi, and Mobile Data.
                        ACTION_WIFI Shows Wi-Fi settings, but not the other connectivity settings. This is useful for apps that need a Wi-Fi connection to perform large uploads or downloads.
                        ACTION_NFC Shows all settings related to near-field communication (NFC).
                        ACTION_VOLUME Shows volume settings for all audio streams.
                     */
                    val panelIntent = Intent(Settings.Panel.ACTION_WIFI)
                    this.startActivityForResult(panelIntent, 955)
                }
            }
            R.id.action_settings -> UIManager.showActionInToast(this, "Settings clicked")
            R.id.info_icon -> showBottomSheetDialogFragment()
            R.id.action_force_crash -> throw RuntimeException("This is a crash")
            else -> {
            }
        }
        return true
    }


    /**
     * Display menu buttons when collapsing toolbar is collapsed
     */
    fun showMenuButtons() {
        if (menu != null) menu!!.setGroupVisible(R.id.menu_main_group, true) // Or true to be visible
    }

    /**
     * Hide menu buttons when collapse toolbar is expanded
     */
    fun hideMenuButtons() {
        if (menu != null) menu!!.setGroupVisible(R.id.menu_main_group, false) // Or true to be visible
    }

    private fun showItemDetail(app: App) {
        if (View.INVISIBLE == viewBinding.clDetailItem?.visibility)
            viewBinding.clDetailItem?.visibility = View.VISIBLE

        viewBinding.ivItemDetail?.let {
            Glide.with(this)
                    .load(if (0 != app.appIcon) app.appIcon else app.appDrawableIcon)
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(e: GlideException?, model: Any,
                                                  target: Target<Drawable>, isFirstResource: Boolean): Boolean {
                            return false
                        }

                        override fun onResourceReady(resource: Drawable, model: Any,
                                                     target: Target<Drawable>, dataSource: DataSource,
                                                     isFirstResource: Boolean): Boolean {

                            if (viewBinding.itemDetailBtn?.visibility == View.GONE) {
                                viewBinding.itemDetailBtn?.visibility = View.VISIBLE
                            }

                            if (0 != app.appIcon && app.appTitle.equals("Palette")) {
                                val myBitmap = (resource as BitmapDrawable).bitmap
                                val newBitmap = UIManager.addGradientToImageView(this@MainActivity, myBitmap)

                                viewBinding.ivItemDetail?.setImageDrawable(
                                        BitmapDrawable(this@MainActivity.resources, newBitmap))
                                return true
                            }
                            if (0 != app.appIcon && app.appTitle.equals("WIP")) {
                                viewBinding.ivItemDetail?.setImageDrawable(
                                        ContextCompat.getDrawable(this@MainActivity, R.drawable.logo_testing))
                                viewBinding.itemDetailBtn?.visibility = View.GONE
                                return true
                            }
                            return false
                        }
                    })
                    .into(it)
        }

        viewBinding.tvTitleDetail?.text = if (!Validator.isEmpty(app.appTitle)) app.appTitle else app.appName
        viewBinding.tvDescriptionDetail?.text = if (!Validator.isEmpty(app.appVersion)) app.appVersion else app.appDescription
    }

    override fun onAppItemCLickListener(view: View, item: App, position: Int) {
        Timber.d("Clicked item : $item, at position : $position")

        if (!LabCompatibilityManager.isTablet(this)) {
            mViewModel.launchActivityOrPackage(item)
        } else {
            showItemDetail(item)
            viewBinding.itemDetailBtn?.setOnClickListener { mViewModel.launchActivityOrPackage(item) }
        }
    }

    override fun onConnected() {
        UIManager.showConnectionStatusInSnackBar(this, true)

        updateToolbarConnectionIcon(true)
    }

    override fun onLostConnection() {
        UIManager.showConnectionStatusInSnackBar(this, false)

        updateToolbarConnectionIcon(false)
    }

    private fun updateToolbarConnectionIcon(isConnected: Boolean) {
        Timber.e("updateToolbarConnectionIcon, is connected : %s", isConnected)
        if (!LabCompatibilityManager.isTablet(this))
            this.runOnUiThread {
                menu?.getItem(0)?.icon =
                        ContextCompat.getDrawable(
                                this,
                                if (isConnected) R.drawable.ic_wifi else R.drawable.ic_wifi_off)
            }
    }
}