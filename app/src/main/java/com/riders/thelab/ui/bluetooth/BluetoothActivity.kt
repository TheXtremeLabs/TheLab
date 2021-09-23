package com.riders.thelab.ui.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.CompoundButton
import android.widget.ListView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.riders.thelab.R
import com.riders.thelab.databinding.ActivityBluetoothBinding
import timber.log.Timber

class BluetoothActivity : AppCompatActivity(),
    CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    private var _viewBinding: ActivityBluetoothBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _viewBinding!!

    private val mViewModel: BluetoothViewModel by viewModels()

    private lateinit var bluetoothDevicesSearchList: ArrayList<String>


    // Create a BroadcastReceiver for ACTION_FOUND.
    private val receiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action.toString()) {
                BluetoothDevice.ACTION_FOUND -> {
                    Timber.e("BluetoothDevice.ACTION_FOUND")
                    // Discovery has found a device. Get the BluetoothDevice
                    // object and its info from the Intent.
                    val device: BluetoothDevice =
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)!!
                    val deviceName = device.name
                    val deviceHardwareAddress = device.address // MAC address

                    Timber.e("name : $deviceName, hardware : $deviceHardwareAddress")

                    bluetoothDevicesSearchList.add("name : $deviceName, hardware : $deviceHardwareAddress")

                }
                BluetoothAdapter.ACTION_STATE_CHANGED -> {
                    when (intent.getIntExtra(
                        BluetoothAdapter.EXTRA_STATE,
                        BluetoothAdapter.ERROR
                    )) {
                        BluetoothAdapter.STATE_OFF -> {
                            // Bluetooth has been turned off;
                            Timber.e("BluetoothAdapter.STATE_OFF")

                            changeButton(
                                getString(R.string.bluetooth_unable_to_discover),
                                ContextCompat.getDrawable(
                                    this@BluetoothActivity,
                                    R.drawable.ic_bluetooth_disabled
                                )!!,
                                false
                            )
                        }
                        BluetoothAdapter.STATE_TURNING_OFF -> {
                            // Bluetooth is turning off;
                            Timber.e("BluetoothAdapter.STATE_TURNING_OFF")
                            binding.progressSearchedDevices.visibility = View.GONE
                        }
                        BluetoothAdapter.STATE_ON -> {
                            // Bluetooth is on
                            Timber.e("BluetoothAdapter.STATE_ON")
                        }
                        BluetoothAdapter.STATE_TURNING_ON -> {
                            // Bluetooth is turning on
                            Timber.e("BluetoothAdapter.STATE_TURNING_ON")
                        }
                    }
                }

                // Discoverability changes
                BluetoothAdapter.ACTION_DISCOVERY_STARTED -> {
                    Timber.e("BluetoothAdapter.ACTION_DISCOVERY_STARTED")

                }
                BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> {
                    Timber.e("BluetoothAdapter.ACTION_DISCOVERY_FINISHED")

                    binding.progressSearchedDevices.visibility = View.GONE

                    bindListView(binding.lvSearchedDevices, bluetoothDevicesSearchList)


                    changeButton(
                        getString(R.string.bluetooth_start_discovery),
                        ContextCompat.getDrawable(
                            this@BluetoothActivity,
                            R.drawable.ic_bluetooth
                        )!!,
                        true
                    )
                }
                BluetoothAdapter.ACTION_SCAN_MODE_CHANGED -> {
                    when (intent.getIntExtra(
                        BluetoothAdapter.EXTRA_SCAN_MODE,
                        BluetoothAdapter.ERROR
                    )) {
                        BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE -> {
                            Timber.e("BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE")
                        }
                        BluetoothAdapter.SCAN_MODE_CONNECTABLE -> {
                            Timber.e("BluetoothAdapter.SCAN_MODE_CONNECTABLE")
                        }
                        BluetoothAdapter.SCAN_MODE_NONE -> {
                            Timber.e("BluetoothAdapter.SCAN_MODE_NONE")
                        }
                    }

                }
                BluetoothDevice.ACTION_ACL_CONNECTED -> {
                    Timber.e("BluetoothDevice.ACTION_ACL_CONNECTED")

                }
                BluetoothDevice.ACTION_ACL_DISCONNECTED -> {
                    Timber.e("BluetoothDevice.ACTION_ACL_DISCONNECTED")

                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _viewBinding = ActivityBluetoothBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bluetoothDevicesSearchList = mutableListOf<String>() as ArrayList<String>

        binding.switchBluetooth.setOnCheckedChangeListener(this)
        binding.btnDiscovery.setOnClickListener(this)

        initViewModelsObservers()

        initSwitch()
        initButton()

        val pairedDevices = BluetoothAdapter.getDefaultAdapter().bondedDevices
        if (pairedDevices.size > 0) {

            val boundedDevices: ArrayList<String> = mutableListOf<String>() as ArrayList<String>

            for (d in pairedDevices) {
                val deviceName = d.name
                val macAddress = d.address
                Timber.i("paired device: $deviceName at $macAddress")
                // do what you need/want this these list items

                boundedDevices.add(deviceName)
            }

            bindListView(binding.lvBoundedDevices, boundedDevices)
        }
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
        registerReceiver(receiver, filter)
    }

    override fun onStop() {
        super.onStop()

        mViewModel.stopDiscovery()
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.e("onDestroy()")
        _viewBinding = null

        // Don't forget to unregister the ACTION_FOUND receiver.
        unregisterReceiver(receiver)
    }

    private fun initViewModelsObservers() {
        mViewModel.getBluetoothEnabled().observe(this, {
            Timber.d("getBluetoothEnabled().observe : $it")
            binding.switchBluetooth.isChecked = it
            binding.switchBluetooth.text =
                if (!it) getString(R.string.bluetooth_off) else getString(R.string.bluetooth_on)

            changeButton(
                if (!it) getString(R.string.bluetooth_unable_to_discover) else getString(R.string.bluetooth_start_discovery),
                if (!it) ContextCompat.getDrawable(
                    this@BluetoothActivity,
                    R.drawable.ic_bluetooth_disabled
                )!! else ContextCompat.getDrawable(
                    this@BluetoothActivity,
                    R.drawable.ic_bluetooth
                )!!,
                it
            )
        })
    }

    private fun initSwitch() {
        binding.switchBluetooth.text =
            if (!mViewModel.getBluetoothState()) getString(R.string.bluetooth_off) else getString(R.string.bluetooth_on)
        binding.switchBluetooth.isChecked = mViewModel.getBluetoothState()
    }

    private fun initButton() {
        val isBluetoothEnabled = mViewModel.getBluetoothState()
        changeButton(
            if (!isBluetoothEnabled) getString(R.string.bluetooth_unable_to_discover) else getString(
                R.string.bluetooth_start_discovery
            ),
            if (!isBluetoothEnabled) ContextCompat.getDrawable(
                this@BluetoothActivity,
                R.drawable.ic_bluetooth_disabled
            )!! else ContextCompat.getDrawable(
                this@BluetoothActivity,
                R.drawable.ic_bluetooth
            )!!,
            isBluetoothEnabled
        )
    }

    private fun changeButton(buttonText: String, icon: Drawable, enabled: Boolean) {
        binding.btnDiscovery.isEnabled = enabled
        binding.btnDiscovery.text = buttonText
        binding.btnDiscovery.icon = icon
    }

    private fun bindListView(listView: View, data: ArrayList<String>) {
        (listView as ListView).adapter =
            ArrayAdapter(
                this@BluetoothActivity,
                android.R.layout.simple_list_item_1,
                data
            )
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        if (binding.switchBluetooth.isShown) {
            mViewModel.setBluetooth(isChecked)
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {

            R.id.btn_discovery -> {
                val bluetoothAdapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

                if (!bluetoothAdapter.isEnabled) {
                    Timber.e("Bluetooth not enable cannot start discovery")
                } else {

                    if (!bluetoothAdapter.isDiscovering) {
                        bluetoothAdapter.startDiscovery()
                        changeButton(
                            getString(R.string.bluetooth_cancel_discovery),
                            ContextCompat.getDrawable(
                                this@BluetoothActivity,
                                R.drawable.ic_bluetooth_searching
                            )!!,
                            true
                        )
                        binding.progressSearchedDevices.visibility = View.VISIBLE
                    } else {
                        bluetoothAdapter.cancelDiscovery()
                        changeButton(
                            getString(R.string.bluetooth_start_discovery),
                            ContextCompat.getDrawable(
                                this@BluetoothActivity,
                                R.drawable.ic_bluetooth
                            )!!,
                            true
                        )
                        binding.progressSearchedDevices.visibility = View.GONE
                    }
                }
            }
        }
    }
}