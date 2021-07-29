package com.riders.thelab.ui.mainactivity

import android.app.Activity
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionInflater
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import com.google.android.material.tabs.TabLayoutMediator
import com.riders.thelab.R
import com.riders.thelab.core.broadcast.LocationBroadcastReceiver
import com.riders.thelab.core.interfaces.ConnectivityListener
import com.riders.thelab.core.location.GpsUtils
import com.riders.thelab.core.location.OnGpsListener
import com.riders.thelab.core.utils.LabCompatibilityManager
import com.riders.thelab.core.utils.LabGlideListener
import com.riders.thelab.core.utils.LabNetworkManagerNewAPI
import com.riders.thelab.core.utils.UIManager
import com.riders.thelab.core.views.ItemSnapHelper
import com.riders.thelab.data.local.model.app.App
import com.riders.thelab.databinding.ActivityMainBinding
import com.riders.thelab.navigator.Navigator
import com.riders.thelab.ui.mainactivity.fragment.bottomsheet.BottomSheetFragment
import com.riders.thelab.ui.mainactivity.fragment.news.NewsFragment
import com.riders.thelab.ui.mainactivity.fragment.time.TimeFragment
import com.riders.thelab.ui.mainactivity.fragment.weather.WeatherFragment
import com.riders.thelab.utils.Constants.Companion.GPS_REQUEST
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.*
import kotlin.system.exitProcess

@AndroidEntryPoint
class MainActivity : AppCompatActivity(),
    Toolbar.OnMenuItemClickListener, OnOffsetChangedListener,
    ConnectivityListener, MainActivityAppClickListener, OnGpsListener {

    private lateinit var viewBinding: ActivityMainBinding

    private val mViewModel: MainActivityViewModel by viewModels()

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private var pagerAdapter: FragmentStateAdapter? = null
    private var mConnectivityManager: ConnectivityManager? = null
    private lateinit var networkManager: LabNetworkManagerNewAPI

    private var menu: Menu? = null
    private var fragmentList: MutableList<Fragment>? = null

    private lateinit var locationReceiver: LocationBroadcastReceiver
    private lateinit var mGpsUtils: GpsUtils
    private var isGPS: Boolean = false

    private var isShow = false
    private var scrollRange = -1

    /////////////////////////////////////
    //
    // OVERRIDE
    //
    /////////////////////////////////////
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val w = window
        w.allowEnterTransitionOverlap = true

        // In Activity's onCreate() for instance
        // make fully Android Transparent Status bar
        when (this.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> {
                w.statusBarColor = Color.TRANSPARENT
            }
            Configuration.UI_MODE_NIGHT_NO,
            Configuration.UI_MODE_NIGHT_UNDEFINED -> {
            }
        }
        window.navigationBarColor = ContextCompat.getColor(this, R.color.default_dark)

        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        // Variables
        locationReceiver = LocationBroadcastReceiver()
        mGpsUtils = GpsUtils(this)

        // Views
        if (!LabCompatibilityManager.isTablet(this)) {
            initViews()
        } else {
            bindTabletViews()
        }

        // ViewModel
        initViewModelsObservers()
        mViewModel.retrieveApplications()
    }

    override fun onStart() {
        super.onStart()
        mConnectivityManager = this.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager?
        networkManager = LabNetworkManagerNewAPI(this)
    }

    override fun onPause() {
        mConnectivityManager!!.unregisterNetworkCallback(networkManager)

        // View Models implementation
        // don't forget to remove receiver data source
        mViewModel.removeDataSource(locationReceiver.getLocationStatus())
        unregisterReceiver(locationReceiver)
        super.onPause()
    }

    override fun onResume() {
        super.onResume()

        // register connection status listener
        val request = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        mConnectivityManager!!.registerNetworkCallback(request, networkManager)


        val intentFilter = IntentFilter()
        intentFilter.addAction(LocationManager.PROVIDERS_CHANGED_ACTION)

        // View Models implementation
        // add data source
        mViewModel.addDataSource(locationReceiver.getLocationStatus())
        registerReceiver(locationReceiver, intentFilter)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GPS_REQUEST) {
                isGPS = true; // flag maintain before get location
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        Timber.d("onCreateOptionsMenu()")
        this.menuInflater.inflate(R.menu.menu_main, menu)
        this.menu = menu
        mViewModel.checkConnection()
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        exitProcess(0)
    }

    override fun onDestroy() {
        Timber.d("onDestroy()")
        Timber.d("unregister network callback()")
        try {
            networkManager.let { mConnectivityManager?.unregisterNetworkCallback(it) };
        } catch (exception: RuntimeException) {
            Timber.e("NetworkCallback was already unregistered")
        }
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

        mViewModel
            .getLocationData()
            .observe(
                this,
                { locationStatus ->
                    Timber.d("getLocationData().observe : $locationStatus")
                    menu?.findItem(R.id.action_location_settings)?.setIcon(
                        if (!locationStatus) R.drawable.ic_location_off else R.drawable.ic_location_on
                    )
                })

        mViewModel
            .getApplications().observe(
                this,
                { appList ->
                    Timber.d("onSuccessPackageList()")

                    if (appList.isEmpty()) {
                        Timber.d("App list is empty")
                    } else {
                        bindApps(appList)
                    }
                })
    }


    /**
     * Set up views (recyclerviews, spinner, etc...)
     */
    private fun initViews() {
        initCollapsingToolbar()
        initToolbar()
        setupViewPager()
    }

    private fun bindTabletViews() {
        this.supportFragmentManager
            .beginTransaction()
            .add(R.id.fragment_time, TimeFragment.newInstance())
            .commit()
        this.supportFragmentManager
            .beginTransaction()
            .add(R.id.fragment_weather, WeatherFragment.newInstance())
            .commit()
    }

    /**
     * Initializing collapsing toolbar
     * Will show and hide the toolbar txtPostTitle on scroll
     */
    private fun initCollapsingToolbar() {
        viewBinding.includeToolbarLayout?.collapsingToolbar?.title = " "
        viewBinding.includeToolbarLayout?.appbar?.setExpanded(true)

        // hiding & showing the txtPostTitle when toolbar expanded & collapsed
        viewBinding.includeToolbarLayout?.appbar?.addOnOffsetChangedListener(this)
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

        menu?.let { menu -> UIManager.hideMenuButtons(menu) }

        viewBinding.includeToolbarLayout?.toolbar?.setOnMenuItemClickListener(this)
    }

    private fun setupViewPager() {

        // Instantiate a ViewPager2 and a PagerAdapter.
        fragmentList = ArrayList()

        val timeFragment = TimeFragment.newInstance()
        timeFragment.sharedElementEnterTransition =
            TransitionInflater.from(this).inflateTransition(R.transition.change_image_transform)

        // add Fragments in your ViewPagerFragmentAdapter class
        fragmentList!!.add(timeFragment)
        fragmentList!!.add(WeatherFragment.newInstance())
        fragmentList!!.add(NewsFragment.newInstance())

        pagerAdapter = ViewPager2Adapter(this@MainActivity, fragmentList as ArrayList<Fragment>)

        // set Orientation in your ViewPager2
        viewBinding.includeToolbarLayout?.viewPager?.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        viewBinding.includeToolbarLayout?.viewPager?.adapter = pagerAdapter

        viewBinding.includeToolbarLayout?.tabLayout?.let { tabLayout ->
            viewBinding.includeToolbarLayout?.viewPager?.let { viewPager2 ->
                TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
                    //Some implementation
                }.attach()
            }
        }
    }


    private fun bindApps(appList: List<App>) {
        Timber.d("bindApps()")

        val adapter = MainActivityAdapter(this, appList, this)

        val linearLayoutManager =
            LinearLayoutManager(
                this,
                if (!LabCompatibilityManager.isTablet(this)) LinearLayoutManager.VERTICAL
                else LinearLayoutManager.HORIZONTAL, false
            )

        viewBinding.appRecyclerView?.layoutManager = linearLayoutManager

        val divider = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        divider.setDrawable(
            ContextCompat.getDrawable(
                this,
                R.drawable.item_separator_view_gradient
            )!!
        )
        if (!LabCompatibilityManager.isTablet(this))
            viewBinding.appRecyclerView?.addItemDecoration(divider)
        else {
            val helper = ItemSnapHelper()
            helper.attachToRecyclerView(viewBinding.appRecyclerView)
        }

        viewBinding.appRecyclerView?.itemAnimator = DefaultItemAnimator()
        viewBinding.appRecyclerView?.adapter = adapter
    }

    fun showBottomSheetDialogFragment() {
        val bottomSheetFragment = BottomSheetFragment()
        bottomSheetFragment.show(
            this.supportFragmentManager,
            bottomSheetFragment.tag
        )
    }


    private fun showItemDetail(app: App) {
        viewBinding.app = app

        if (View.INVISIBLE == viewBinding.clDetailItem?.visibility)
            viewBinding.clDetailItem?.visibility = View.VISIBLE
        viewBinding.clDetailItem?.let { UIManager.showView(it) }


        viewBinding.ivItemDetail?.let {
            UIManager.loadImage(
                this,
                (if (0 != app.appIcon) app.appIcon else app.appDrawableIcon)!!,
                it,
                LabGlideListener(
                    onLoadingSuccess = { resource ->
                        Timber.d("dskjfnodsnv")
                        if (viewBinding.itemDetailBtn?.visibility == View.GONE) {
                            viewBinding.itemDetailBtn?.visibility = View.VISIBLE
                        }

                        if (0 != app.appIcon && app.appTitle == "Palette") {
                            val myBitmap: Bitmap = UIManager.drawableToBitmap(resource!!)
                            val newBitmap =
                                UIManager.addGradientToImageView(this@MainActivity, myBitmap)

                            viewBinding.ivItemDetail?.setImageDrawable(
                                BitmapDrawable(this@MainActivity.resources, newBitmap)
                            )
                            return@LabGlideListener true
                        }
                        if (0 != app.appIcon && app.appTitle == "WIP") {
                            viewBinding.ivItemDetail?.setImageDrawable(
                                ContextCompat.getDrawable(
                                    this@MainActivity,
                                    R.drawable.logo_testing
                                )
                            )
                            viewBinding.itemDetailBtn?.visibility = View.GONE
                            return@LabGlideListener true
                        }

                        false

                    })
            )
        }
    }


    /////////////////////////////////////
    //
    // IMPLEMENTS
    //
    /////////////////////////////////////
    override fun onMenuItemClick(item: MenuItem?): Boolean {
        Timber.d("onMenuItemClick()")
        when (item?.itemId) {
            R.id.action_connection_settings -> networkManager.changeWifiState(
                this.applicationContext,
                this@MainActivity
            )
            R.id.action_location_settings -> if (!isGPS) mGpsUtils.turnGPSOn(this)
            R.id.action_settings -> UIManager.showActionInToast(this, "Settings clicked")
            R.id.action_info_settings -> showBottomSheetDialogFragment()
            R.id.action_force_crash -> throw RuntimeException("This is a crash")
            else -> {
            }
        }
        return true
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
        if (scrollRange == -1) {
            scrollRange = appBarLayout!!.totalScrollRange
        }
        if (scrollRange + verticalOffset == 0) {
            // Toolbar is collapsed
            viewBinding.includeToolbarLayout?.collapsingToolbar?.title =
                this@MainActivity.resources.getString(R.string.app_name)
            menu?.let { menu -> UIManager.showMenuButtons(menu) }
            isShow = true
        } else if (isShow) {
            // Toolbar is expanded
            viewBinding.includeToolbarLayout?.collapsingToolbar?.title = " "
            menu?.let { menu -> UIManager.hideMenuButtons(menu) }
            isShow = false
        }
    }

    override fun onAppItemCLickListener(view: View, item: App, position: Int) {
        Timber.d("Clicked item : $item, at position : $position")

        if (!LabCompatibilityManager.isTablet(this@MainActivity)) {
            mViewModel.launchActivityOrPackage(Navigator(this@MainActivity), item)
        } else {
            showItemDetail(item)
            viewBinding.itemDetailBtn?.setOnClickListener {
                mViewModel.launchActivityOrPackage(Navigator(this@MainActivity), item)
            }
        }
    }

    override fun onConnected() {
        UIManager.showConnectionStatusInSnackBar(this@MainActivity, true)
        updateToolbarConnectionIcon(true)
    }

    override fun onLostConnection() {
        UIManager.showConnectionStatusInSnackBar(this@MainActivity, false)
        updateToolbarConnectionIcon(false)
    }

    private fun updateToolbarConnectionIcon(isConnected: Boolean) {
        Timber.e("updateToolbarConnectionIcon, is connected : %s", isConnected)
        if (!LabCompatibilityManager.isTablet(this))
            this.runOnUiThread {
                menu?.getItem(0)?.icon =
                    ContextCompat.getDrawable(
                        this@MainActivity,
                        if (isConnected) R.drawable.ic_wifi else R.drawable.ic_wifi_off
                    )
            }
    }

    override fun gpsStatus(isGPSEnable: Boolean) {
        Timber.d("gpsStatus()")
        Timber.d("turn on/off GPS - isGPSEnable : $isGPSEnable")
        isGPS = isGPSEnable

        if (isGPS) menu?.findItem(R.id.action_location_settings)?.icon =
            ContextCompat.getDrawable(this@MainActivity, R.drawable.ic_location_on)
    }

}