package com.riders.thelab.ui.mainactivity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.*
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import com.google.android.material.textview.MaterialTextView
import com.riders.thelab.R
import com.riders.thelab.TheLabApplication
import com.riders.thelab.core.broadcast.LocationBroadcastReceiver
import com.riders.thelab.core.bus.LocationFetchedEvent
import com.riders.thelab.core.interfaces.ConnectivityListener
import com.riders.thelab.core.location.GpsUtils
import com.riders.thelab.core.location.OnGpsListener
import com.riders.thelab.core.utils.*
import com.riders.thelab.core.views.ItemSnapHelper
import com.riders.thelab.data.local.model.app.App
import com.riders.thelab.databinding.ActivityMainBinding
import com.riders.thelab.navigator.Navigator
import com.riders.thelab.ui.mainactivity.fragment.bottomsheet.BottomSheetFragment
import com.riders.thelab.ui.mainactivity.fragment.home.HomeFragment
import com.riders.thelab.ui.mainactivity.fragment.news.NewsFragment
import com.riders.thelab.ui.mainactivity.fragment.time.TimeFragment
import com.riders.thelab.ui.mainactivity.fragment.weather.WeatherFragment
import com.riders.thelab.ui.weather.WeatherUtils
import com.riders.thelab.utils.Constants.Companion.GPS_REQUEST
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.CoroutineContext
import kotlin.system.exitProcess

@AndroidEntryPoint
class MainActivity : AppCompatActivity(),
    CoroutineScope,
    View.OnClickListener, SearchView.OnQueryTextListener,
    Toolbar.OnMenuItemClickListener, OnOffsetChangedListener,
    ConnectivityListener, LocationListener, OnGpsListener,
    MainActivityAppClickListener {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()


    private var _viewBinding: ActivityMainBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _viewBinding!!

    private val mViewModel: MainActivityViewModel by viewModels()

    // Toolbar
    // Collapsing Toolbar
    private var isShow = false
    private var scrollRange = -1

    // Menu
    private var menu: Menu? = null
    // ViewPager
    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private var mViewPagerAdapter: ViewPager2Adapter? = null
    private var mFragmentList: MutableList<Fragment>? = null
    private var mRecentApps: List<App>? = null

    // Location
    private lateinit var locationReceiver: LocationBroadcastReceiver
    private lateinit var mGpsUtils: GpsUtils
    private var isGPS: Boolean = false
    private var lastKnowLocation: Location? = null

    // Network
    private var mConnectivityManager: ConnectivityManager? = null
    private lateinit var networkManager: LabNetworkManagerNewAPI

    // Time
    private var isTimeUpdatedStarted: Boolean = false
    private var isConnected: Boolean = true

    // Content
    private var adapter: RecyclerView.Adapter<*>? = null
    private var mFetchedApps: List<App>? = null
    private var isStaggeredLayout: Boolean = false


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

        _viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
        mViewModel.retrieveApplications(TheLabApplication.getInstance().getContext())
    }

    override fun onStart() {
        super.onStart()
        mConnectivityManager = this.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager?
        networkManager = LabNetworkManagerNewAPI(this)
    }

    override fun onPause() {
        Timber.e("onPause()")
        EventBus.getDefault().unregister(this)

        mConnectivityManager!!.unregisterNetworkCallback(networkManager)

        // View Models implementation
        // don't forget to remove receiver data source
        mViewModel.removeDataSource(locationReceiver.getLocationStatus())
        unregisterReceiver(locationReceiver)

        if (isTimeUpdatedStarted) {
            isTimeUpdatedStarted = false
        }
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        Timber.i("onResume()")

        EventBus.getDefault().register(this)

        // register connection status listener
        val request = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        mConnectivityManager!!.registerNetworkCallback(request, networkManager)

        val labLocationManager = LabLocationManager(this@MainActivity, this@MainActivity, this)

        if (!labLocationManager.canGetLocation()) {
            Timber.e("Cannot get location please enable position")

            binding.includeToolbarLayout?.ivLocationStatus?.setBackgroundResource(
                R.drawable.ic_location_off
            )
            labLocationManager.showSettingsAlert()
        } else {
            labLocationManager.setLocationListener()
            labLocationManager.getLocation()

            binding.includeToolbarLayout?.ivLocationStatus?.setBackgroundResource(
                R.drawable.ic_location_on
            )
        }

        updateTime()

        val intentFilter = IntentFilter()
        intentFilter.addAction(LocationManager.PROVIDERS_CHANGED_ACTION)

        // View Models implementation
        // add data source
        mViewModel.addDataSource(locationReceiver.getLocationStatus())
        registerReceiver(locationReceiver, intentFilter)

        launchProgressBars()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GPS_REQUEST) {
                isGPS = true // flag maintain before get location
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_connection_settings -> {
                Timber.e("Internet wifi icon status clicked")

                toggleWifi()
                return true
            }
            R.id.action_location_settings -> {
                Timber.e("Location icon status clicked")
                toggleLocation()
                return true
            }
        }
        return false
    }

    override fun onBackPressed() {
        super.onBackPressed()
        exitProcess(0)
    }

    override fun onDestroy() {
        Timber.d("onDestroy()")
        Timber.d("unregister network callback()")
        try {
            networkManager.let { mConnectivityManager?.unregisterNetworkCallback(it) }
        } catch (exception: RuntimeException) {
            Timber.e("NetworkCallback was already unregistered")
        }
        super.onDestroy()

        _viewBinding = null
    }


    /////////////////////////////////////
    //
    // BUS
    //
    /////////////////////////////////////
    @DelicateCoroutinesApi
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onLocationFetchedEventResult(event: LocationFetchedEvent) {
        Timber.e("onLocationFetchedEvent()")
        val location: Location = event.location
        val latitude = location.latitude
        val longitude = location.longitude
        Timber.e("$latitude, $longitude")

        lastKnowLocation = location

        if (this.isConnected) {
            mViewModel.fetchWeather(this@MainActivity, latitude, longitude)
        } else {
            Timber.e("Not connected to the internet. Cannot perform network action")
        }
    }


    /////////////////////////////////////
    //
    // CLASS METHODS
    //
    /////////////////////////////////////
    private fun updateTime() = CoroutineScope(Dispatchers.Main).launch {
        Timber.d("updateTime()")

        isTimeUpdatedStarted = true

        while (isTimeUpdatedStarted) {
            val date = Date(System.currentTimeMillis())
            val time = SimpleDateFormat("HH:mm", Locale.getDefault()).format(date)
            binding.includeToolbarLayout?.tvTime?.text = time

            delay(1000)
        }
    }

    @DelicateCoroutinesApi
    private fun launchProgressBars() {
        Timber.d("launchProgressBars()")
        GlobalScope.launch(Dispatchers.Main) {
            binding.includeToolbarLayout?.llProgressBarContainer?.children?.let { linearContainer ->
                linearContainer.forEachIndexed { index, view ->
                    if (view is LinearLayout) {
                        val materialTextView: MaterialTextView =
                            view.getChildAt(0) as MaterialTextView
                        val progressBar: ProgressBar =
                            view.getChildAt(1) as ProgressBar

                        progressBar.progress = 0

                        setProgressBarText(index, materialTextView)

                        runCatching {

                            for (i in 0 until 100) {
                                progressBar.progress = i
                                delay(100)
                            }

                            // Check if "current position" equal "number of elements
                            if (binding.includeToolbarLayout?.viewPager?.currentItem
                                != binding.includeToolbarLayout?.viewPager?.adapter?.itemCount?.minus(
                                    1
                                )
                            ) {
                                binding.includeToolbarLayout?.viewPager?.let { viewPager ->
                                    viewPager.currentItem = viewPager.currentItem + 1
                                }

                            }
                            materialTextView.text = ""
                        }
                    }
                }
                val job = resetProgressBars()
                job.join()

                binding.includeToolbarLayout?.viewPager?.currentItem = 0
                launchProgressBars()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setProgressBarText(index: Int, materialTextView: MaterialTextView) {
        when (index) {
            0 -> materialTextView.text = "Home"
            1 -> materialTextView.text = mRecentApps!![0].appTitle
            2 -> materialTextView.text = mRecentApps!![1].appTitle
            3 -> materialTextView.text = mRecentApps!![2].appTitle
        }
    }


    private fun resetProgressBars() =
        GlobalScope.launch(Dispatchers.Main) {
            Timber.d("resetProgressBars()")

            binding.includeToolbarLayout?.llProgressBarContainer?.children?.let {
                val size = it.count() - 1

                for (i in size downTo 0) {
                    Timber.d("View number : $i, contains : ${it.toList().toString()}")
                    if (it.toList()[i] is LinearLayout) {
                        val linearLayout = it.toList()[i] as LinearLayout
                        val progressBar: ProgressBar =
                            linearLayout.getChildAt(1) as ProgressBar

                        progressBar.progress = 100

                        for (j in 100 downTo 0) {
                            progressBar.progress = j
                            delay(50)
                        }
                    }
                }
            }

        }


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

        mViewModel.getWeather().observe(
            this,
            {
                Timber.d("getWeather().observe : $it")

                LabGlideUtils.getInstance().loadImage(
                    this@MainActivity,
                    WeatherUtils.getWeatherIconFromApi(it.weatherIconUrl),
                    binding.includeToolbarLayout?.ivWeatherIcon!!
                )

                val sb: StringBuilder =
                    StringBuilder()
                        .append(it.temperature)
                        .append(" °c,")
                        .append(" ")
                        .append(it.city)

                // bind data
                binding.includeToolbarLayout?.tvWeather?.text = sb.toString()
            })

        mViewModel
            .getApplications().observe(
                this,
                { appList ->
                    Timber.d("onSuccessPackageList()")

                    this.mFetchedApps = appList

                    setupLastFeaturesApps()
                    initViewPager()

                    if (appList.isEmpty()) {
                        Timber.d("App list is empty")
                    } else {
                        bindApps()
                    }
                })
    }


    /**
     * Set up views (recycler views, spinner, etc...)
     */
    private fun initViews() {
        initCollapsingToolbar()
        initToolbar()
        setListeners()
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
        binding.includeToolbarLayout?.collapsingToolbar?.title = " "
        binding.includeToolbarLayout?.appbar?.setExpanded(true)

        // hiding & showing the txtPostTitle when toolbar expanded & collapsed
        binding.includeToolbarLayout?.appbar?.addOnOffsetChangedListener(this)
    }


    /**
     * Setup Toolbar menu icon differently than the basic way because of the collapsing toolbar
     * <p>
     * We want the button to show up only when the toolbar is collapsed
     * <p>
     * https://stackoverflow.com/questions/10692755/how-do-i-hide-a-menu-item-in-the-actionbar#:~:text=The%20best%20way%20to%20hide,menu%20inside%20the%20same%20group.&text=Then%2C%20on%20your%20activity%20(preferable,visibility%20to%20false%20or%20true.
     */
    private fun initToolbar() {
        binding.includeToolbarLayout?.toolbar?.inflateMenu(R.menu.menu_main)
        menu = binding.includeToolbarLayout?.toolbar?.menu

        menu?.let { menu -> UIManager.hideMenuButtons(menu) }

        binding.includeToolbarLayout?.toolbar?.setOnMenuItemClickListener(this)
    }

    private fun setupLastFeaturesApps() {
        Timber.d("setupLastFeaturesApps()")

        val recentAppsNames = arrayOf("Music", "Google", "Weather")
        mRecentApps = mViewModel.fetchRecentApps(this, recentAppsNames)

        mFragmentList = mutableListOf()

        mFragmentList?.let { fragmentList ->
            mRecentApps?.let { recentApps ->
                fragmentList.add(HomeFragment())
                fragmentList.add(NewsFragment.newInstance(recentApps[0]))
                fragmentList.add(NewsFragment.newInstance(recentApps[1]))
                fragmentList.add(NewsFragment.newInstance(recentApps[2]))
            }
        }
    }

    private fun initViewPager() {
        Timber.d("initViewPager()")

        mFragmentList?.let {
            // Instantiate a ViewPager2 and a PagerAdapter.
            mViewPagerAdapter = ViewPager2Adapter(this, it)
            binding.includeToolbarLayout?.viewPager?.adapter = mViewPagerAdapter

            binding.includeToolbarLayout?.viewPager?.registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
//                    val iPage = position + 1
//                    val pages = iPage.toString() + ""
//                    Timber.d(" changeinfopage : $pages")
                    // Ignored
                }

                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                    // Ignored
                }

                override fun onPageScrollStateChanged(state: Int) {
                    // Ignored
                }
            })

//        binding.contentMainHeader.viewPager2.setOnTouchListener(OnTouchListener { arg0, arg1 -> true })
            // Ref : https://stackoverflow.com/questions/7814017/is-it-possible-to-disable-scrolling-on-a-viewpager
            //  binding.contentMainHeader.viewPager2.isUserInputEnabled = false


            binding.includeToolbarLayout?.viewPager?.setPageTransformer { page, position ->
//                Timber.d("setPageTransformer()")
                page.alpha = 0f
                page.visibility = View.VISIBLE

                // Start Animation for a short period of time
                page.animate()
                    .alpha(1f).duration =
                    page.resources.getInteger(android.R.integer.config_shortAnimTime).toLong()
            }
        }
    }

    private fun setListeners() {
        Timber.d("setListeners()")
        binding.includeToolbarLayout?.ivInternetStatus?.setOnClickListener(this)
        binding.includeToolbarLayout?.ivLocationStatus?.setOnClickListener(this)
        binding.includeToolbarLayout?.searchView?.setOnQueryTextListener(this)
        binding.includeContentLayout?.ivLinearLayout?.setOnClickListener(this)
        binding.includeContentLayout?.ivStaggeredLayout?.setOnClickListener(this)
    }

    private fun bindApps() {
        Timber.d("bindApps()")

        binding.includeContentLayout?.appRecyclerView?.setHasFixedSize(true)
        if (LabCompatibilityManager.isTablet(this)) {
            val helper = ItemSnapHelper()
            helper.attachToRecyclerView(binding.includeContentLayout?.appRecyclerView)
            binding.includeContentLayout?.appRecyclerView?.itemAnimator = DefaultItemAnimator()
        }

        applyRecycler()
    }


    private fun applyRecycler() {
        Timber.d("applyRecycler()")
        var layoutManager: RecyclerView.LayoutManager?

        if (!LabCompatibilityManager.isTablet(this)) {

            if (!isStaggeredLayout) {
                adapter = mFetchedApps?.let { MainActivityAdapter(this, it, this) }
                // Classic Linear layout manager
                layoutManager = LinearLayoutManager(
                    this,
                    if (!LabCompatibilityManager.isTablet(this)) LinearLayoutManager.VERTICAL
                    else LinearLayoutManager.HORIZONTAL, false
                )

                with(binding.includeContentLayout?.appRecyclerView?.layoutParams) {
                    this!!.width = ViewGroup.LayoutParams.MATCH_PARENT
                    this.height = ViewGroup.LayoutParams.MATCH_PARENT
                }
            } else {
                adapter = mFetchedApps?.let { MainActivityStaggeredAdapter(this, it, this) }
                // set a StaggeredGridLayoutManager with 3 number of columns and vertical orientation
                layoutManager =
                    StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)

                with(binding.includeContentLayout?.appRecyclerView?.layoutParams) {
                    this!!.width = ViewGroup.LayoutParams.WRAP_CONTENT
                    this.height = ViewGroup.LayoutParams.MATCH_PARENT
                }
            }
            // set LayoutManager to RecyclerView
            binding.includeContentLayout?.appRecyclerView?.layoutManager = layoutManager
            adapter?.notifyItemRangeChanged(0, adapter?.itemCount ?: 0)
        }

        binding.includeContentLayout?.appRecyclerView?.adapter = adapter
    }


    private fun showBottomSheetDialogFragment() {
        val bottomSheetFragment = BottomSheetFragment()
        bottomSheetFragment.show(this.supportFragmentManager, bottomSheetFragment.tag)
    }


    private fun showItemDetail(app: App) {
        binding.app = app

        if (View.INVISIBLE == binding.clDetailItem?.visibility)
            binding.clDetailItem?.visibility = View.VISIBLE
        binding.clDetailItem?.let { UIManager.showView(it) }

        binding.ivItemDetail?.let {
            UIManager.loadImage(
                this,
                app.appDrawableIcon,
                it,
                LabGlideListener(
                    onLoadingSuccess = { resource ->
                        if (binding.itemDetailBtn?.visibility == View.GONE) {
                            binding.itemDetailBtn?.visibility = View.VISIBLE
                        }

                        if (app.appTitle == "Palette") {
                            val myBitmap: Bitmap = UIManager.drawableToBitmap(resource!!)
                            val newBitmap =
                                UIManager.addGradientToImageView(this@MainActivity, myBitmap)

                            binding.ivItemDetail?.setImageDrawable(
                                BitmapDrawable(this@MainActivity.resources, newBitmap)
                            )
                            return@LabGlideListener true
                        }
                        if (app.appTitle == "WIP") {
                            binding.ivItemDetail?.setImageDrawable(
                                ContextCompat.getDrawable(
                                    this@MainActivity,
                                    R.drawable.logo_testing
                                )
                            )
                            binding.itemDetailBtn?.visibility = View.GONE
                            return@LabGlideListener true
                        }

                        false

                    })
            )
        }
    }

    private fun toggleLocation() {
        Timber.e("toggleLocation()")
        if (!isGPS) mGpsUtils.turnGPSOn(this)
    }

    private fun toggleWifi() {
        Timber.d("toggleWifi()")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val panelIntent = Intent(Settings.Panel.ACTION_INTERNET_CONNECTIVITY)
            startActivityForResult(panelIntent, 0)
        } else {
            // use previous solution, add appropriate permissions to AndroidManifest file (see answers above)
            (this.applicationContext.getSystemService(Context.WIFI_SERVICE) as? WifiManager)
                ?.apply {
                    // isWifiEnabled = true /*or false*/
                    if (!isWifiEnabled) {
                        Timber.d("(this.applicationContext.getSystemService(Context.WIFI_SERVICE) as? WifiManager) $isWifiEnabled")
                        Timber.d("This should activate wifi")
                        isWifiEnabled = true
                        menu?.findItem(R.id.action_connection_settings)?.icon =
                            ContextCompat.getDrawable(this@MainActivity, R.drawable.ic_wifi)
                    } else {
                        Timber.d("(this.applicationContext.getSystemService(Context.WIFI_SERVICE) as? WifiManager) $isWifiEnabled")
                        Timber.d("This should disable wifi")
                        isWifiEnabled = false
                        menu?.findItem(R.id.action_connection_settings)?.icon =
                            ContextCompat.getDrawable(this@MainActivity, R.drawable.ic_wifi_off)
                    }
                    this.isWifiEnabled = !isWifiEnabled
                }
        }
    }

    private fun toggleRecyclerViewLinearLayout() {
        Timber.d("toggleRecyclerViewLinearLayout()")
        this.isStaggeredLayout = false

        // clear staggered icon
        UIManager.setBackgroundColor(
            this,
            binding.includeContentLayout?.ivStaggeredLayout!!,
            R.color.transparent
        )

        //Apply selected background color
        UIManager.setBackgroundColor(
            this,
            binding.includeContentLayout?.ivLinearLayout!!,
            R.color.teal_700
        )
        applyRecycler()
    }

    private fun toggleRecyclerViewStaggeredLayout() {
        Timber.d("toggleRecyclerViewStaggeredLayout()")
        this.isStaggeredLayout = true

        // clear staggered icon
        UIManager.setBackgroundColor(
            this,
            binding.includeContentLayout?.ivLinearLayout!!,
            R.color.transparent
        )

        //Apply selected background color
        UIManager.setBackgroundColor(
            this,
            binding.includeContentLayout?.ivStaggeredLayout!!,
            R.color.teal_700
        )
        applyRecycler()
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
            binding.includeToolbarLayout?.collapsingToolbar?.title =
                this@MainActivity.resources.getString(R.string.app_name)
            menu?.let { menu -> UIManager.showMenuButtons(menu) }
            isShow = true
        } else if (isShow) {
            // Toolbar is expanded
            binding.includeToolbarLayout?.collapsingToolbar?.title = " "
            menu?.let { menu -> UIManager.hideMenuButtons(menu) }
            isShow = false
        }
    }

    /////////// OnClick Listener ///////////
    override fun onClick(view: View?) {
        when (view?.id) {

            /*R.id.iv_internet_status -> {
                Timber.e("Internet wifi icon status clicked")
            }

            R.id.iv_location_status -> {
                Timber.e("Location icon status clicked")
            }*/

            R.id.iv_linear_layout -> toggleRecyclerViewLinearLayout()
            R.id.iv_staggered_layout -> toggleRecyclerViewStaggeredLayout()
        }
    }

    /////////// Search View Listener ///////////
    override fun onQueryTextSubmit(query: String?): Boolean {
        if (!query.isNullOrEmpty()) {
            Timber.d(query.toString())
            UIManager.hideKeyboard(this@MainActivity, binding.root)
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        Timber.d(newText.toString())
        (if (!isStaggeredLayout) adapter as MainActivityAdapter else adapter as MainActivityStaggeredAdapter).filter
            ?.filter(
                newText.toString()
            )
        return true
    }

    override fun onAppItemClickListener(cardView: View, item: App) {
        Timber.d("element : $item")

        //TODO : Please check this functionality later. Problem using Drive REST API v3
        if (item.appTitle.lowercase().contains("drive")) {
            UIManager.showActionInToast(
                this@MainActivity,
                "Please check this functionality later. Problem using Drive REST API v3"
            )

            return
        } else if (!LabCompatibilityManager.isTablet(this@MainActivity) and (-1L != item.id)) {
            mViewModel.launchActivityOrPackage(Navigator(this@MainActivity), item)
        } else {
            Timber.e("Item id == -1 , not app activity. Should launch package intent.")
            showItemDetail(item)
            binding.itemDetailBtn?.setOnClickListener {
                mViewModel.launchActivityOrPackage(Navigator(this@MainActivity), item)
            }
        }
    }


    override fun onAppItemCLickListener(view: View, item: App, position: Int) {
        Timber.d("Clicked item : $item, at position : $position")

        //TODO : Please check this functionality later. Problem using Drive REST API v3
        if (item.appTitle.lowercase().contains("drive")) {
            UIManager.showActionInToast(
                this@MainActivity,
                "Please check this functionality later. Problem using Drive REST API v3"
            )

            return
        } else if (!LabCompatibilityManager.isTablet(this@MainActivity)) {
            mViewModel.launchActivityOrPackage(Navigator(this@MainActivity), item)
        } else {
            showItemDetail(item)
            binding.itemDetailBtn?.setOnClickListener {
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

    override fun onLocationChanged(location: Location) {
        Timber.d("$location")
    }

}