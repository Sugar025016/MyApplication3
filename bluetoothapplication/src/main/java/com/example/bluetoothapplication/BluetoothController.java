package com.example.bluetoothapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("MissingPermission")
public class BluetoothController {
    private BluetoothAdapter bluetoothAdapter;

    public BluetoothController(BluetoothAdapter bluetoothAdapter) {
        this.bluetoothAdapter = bluetoothAdapter;
    }

    public void turnOnBluetooth(Activity activity, int requestCode) {
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);

        try {
            activity.startActivityForResult(intent, requestCode);
        } catch (SecurityException e) {
            Log.e("SecurityException", e.getMessage());
        }

    }


    public void enableVisibly(Context context) {
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);

        context.startActivity(discoverableIntent);
    }
    public void findDevice() {
        assert (bluetoothAdapter != null);
        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
        } else {

            if (bluetoothAdapter.isEnabled()) {
            /** bluetoothAdapter.startDiscovery();需要位置權限，但即使授予了位置權限，當它被禁用時，藍牙 API 也不起作用。
             所以建議大家添加如，確保權限生效： **/
                bluetoothAdapter.startDiscovery();

            }
        }
    }


    public boolean isSupportBluetooth() {
        if (bluetoothAdapter != null) {
            return true;
        } else {
            return false;
        }
    }

    public List<BluetoothDevice> getBondedDeviceList(){
        return new ArrayList<>(bluetoothAdapter.getBondedDevices());
    }

    public boolean getBluetoothStatus() {
        assert (bluetoothAdapter != null);
        return bluetoothAdapter.isEnabled();
    }


    public void turnOffBluetooth() {
        try {
            bluetoothAdapter.disable();
        } catch (SecurityException e) {
            Log.e("SecurityException", e.getMessage());
        }
    }
}
