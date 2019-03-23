package com.sasakisan.android_ble_sample

import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.sasakisan.android_ble_sample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val bluetoothManager = applicationContext.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val bluetoothAdapter = bluetoothManager.adapter
        val bluetoothLeScanner = bluetoothAdapter.bluetoothLeScanner

        val scanCallback = object : ScanCallback() {
            override fun onScanResult(callbackType: Int, result: ScanResult?) {
                super.onScanResult(callbackType, result)
                result?.let {
                    if (result.device.name != null) {
                        Log.d("DeviceName: ", result.device.name)
                    }
                    Log.d("DeviceAddress: ", result.device.address)
                }
            }

            override fun onBatchScanResults(results: MutableList<ScanResult>?) {
                super.onBatchScanResults(results)
            }

            override fun onScanFailed(errorCode: Int) {
                super.onScanFailed(errorCode)
            }
        }

        binding.scan.setOnClickListener {
            val scanFilter = ScanFilter.Builder()
                .build()
            val scanFilterList = mutableListOf<ScanFilter>()
            scanFilterList.add(scanFilter)
            val scanSettings = ScanSettings.Builder().setScanMode(ScanSettings.SCAN_MODE_BALANCED).build()
            bluetoothLeScanner.startScan(scanFilterList, scanSettings, scanCallback)
            binding.scanState.text = "scanning"
        }
        binding.stop.setOnClickListener {
            bluetoothLeScanner.stopScan(scanCallback)
            binding.scanState.text = "stop"
        }
    }
}
