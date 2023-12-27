package com.riders.thelab.feature.bluetooth

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.riders.thelab.core.common.bus.KotlinBus
import com.riders.thelab.core.common.bus.Listen
import com.riders.thelab.core.common.utils.LabCompatibilityManager
import com.riders.thelab.core.permissions.DexterPermissionManager
import com.riders.thelab.core.ui.compose.base.BaseComponentActivity
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.launch
import timber.log.Timber


class BluetoothActivity : BaseComponentActivity() {

    private val mViewModel: BluetoothViewModel by viewModels()

    private var activityResultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                Timber.d("OK")
                // There are no request codes
                val data = result.data
                mViewModel.fetchBoundedDevices()
            }
        }

    private val mLabBluetoothReceiver = LabBluetoothReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                setContent {
                    TheLabTheme {
                        // A surface container using the 'background' color from the theme
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            BluetoothContent(viewModel = mViewModel)
                        }
                    }
                }
            }
        }

        checkPermissions()
    }

    override fun onResume() {
        super.onResume()

        // Register for broadcasts when a device is discovered.
        val filter = IntentFilter()
        filter.addAction(BluetoothDevice.ACTION_FOUND)
        // State
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
        // Discoverability
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        filter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED)
        registerReceiver(mLabBluetoothReceiver, filter)
    }

    override fun backPressed() {
        Timber.e("backPressed()")
        finish()
    }

    override fun onStop() {
        super.onStop()
        Timber.e("onStop()")

        mViewModel.stopDiscovery()
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.e("onDestroy()")

        // Don't forget to unregister the ACTION_FOUND receiver.
        unregisterReceiver(mLabBluetoothReceiver)
    }

    @Listen
    fun onBluetoothDeviceFoundEvent(device: BluetoothDevice) {
        Timber.d("Listen | onBluetoothDeviceFoundEvent() | $device")
    }

    fun checkPermissions() {
        Timber.d("checkPermissions()")

        if (LabCompatibilityManager.isTiramisu()) {
            val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            activityResultLauncher.launch(intent)
        } else {
            val permissionManager = DexterPermissionManager(this@BluetoothActivity)
            if (!LabCompatibilityManager.isS()) {
                permissionManager.checkPermission(
                    Manifest.permission.BLUETOOTH,
                    onPermissionDenied = {
                        finish()
                    },
                    onPermissionGranted = {
                        mViewModel.initBluetoothManager(this@BluetoothActivity)
                        observeBluetoothDeviceFetchedEvents()
                    },
                    onShouldShowRationale = {})
            } else {
                permissionManager.checkPermissions(
                    Manifest.permission.BLUETOOTH,
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_CONNECT,
                    onPermissionDenied = {
                        finish()
                    },
                    onPermissionGranted = {
                        mViewModel.initBluetoothManager(this@BluetoothActivity)
                        observeBluetoothDeviceFetchedEvents()
                    },
                    onShouldShowRationale = {}
                )
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun observeBluetoothDeviceFetchedEvents() {
        Timber.d("observeBluetoothDeviceFetchedEvents()")

        lifecycleScope.launch {
            KotlinBus.subscribe<BluetoothDevice> {
                Timber.d("KotlinBus | subscribe | $it")
                mViewModel.addNewBluetoothAvailableDevices(it)
            }
        }
    }

    fun updateBluetoothEnabled(enable: Boolean) = mViewModel.updateBluetoothEnabled(enable)
    fun updateBluetoothSearchProgress(visible: Boolean) =
        mViewModel.updateBluetoothSearchProgress(visible)
    fun updateIsBluetoothSearching(isSearching: Boolean) =
        mViewModel.updateIsBluetoothSearching(isSearching)
}