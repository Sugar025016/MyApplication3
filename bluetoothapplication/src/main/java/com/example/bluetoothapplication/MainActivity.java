package com.example.bluetoothapplication;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.media.AudioManager;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<BluetoothDevice> mDeviceList = new ArrayList<>();
    private List<BluetoothDevice> mBluetoothDeviceList = new ArrayList<>();
    private BluetoothController bluetoothController;
    private Toast toast;
    private int REQUEST_CODE = 0;
    private BluetoothAdapter bluetoothAdapter;
    private ListView listView;
    private DeviceAdapter deviceAdapter;
    private ProgressBar progressBar;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.BLUETOOTH_PRIVILEGED
    };
    private static String[] PERMISSIONS_LOCATION = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.BLUETOOTH_PRIVILEGED
    };

    private final BroadcastReceiver mHeadsetReceiver2 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("BroadcastReceiver", "廣播");
            Log.d("intent.getAction()", intent.getAction());
            String state = intent.getAction();
            switch (state) {
                case BluetoothAdapter.ACTION_DISCOVERY_STARTED:
                    showToast("ACTION_DISCOVERY_STARTED");

                    progressBar.setVisibility(View.VISIBLE);
                    setSupportProgressBarIndeterminateVisibility(true);
                    mDeviceList.clear();
                    deviceAdapter.notifyDataSetChanged();
                    break;
                case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
                    showToast("ACTION_DISCOVERY_FINISHED");
                    progressBar.setVisibility(View.GONE);
                    setSupportProgressBarIndeterminateVisibility(false);
                    break;
                case BluetoothDevice.ACTION_FOUND:
                    showToast("ACTION_FOUND");
                    BluetoothDevice remoteDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    mDeviceList.add(remoteDevice);
                    deviceAdapter.notifyDataSetChanged();
                    break;
                case BluetoothAdapter.ACTION_SCAN_MODE_CHANGED:
                    showToast("ACTION_SCAN_MODE_CHANGED");
                    int scanMode = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, 0);
                    if (scanMode == BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
                        progressBar.setVisibility(View.VISIBLE);
                    } else {
                        progressBar.setVisibility(View.GONE);
                    }
                    break;
                case BluetoothDevice.ACTION_BOND_STATE_CHANGED:
                    showToast("ACTION_BOND_STATE_CHANGED");
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    if (device == null) {
                        showToast("~~~~~ no device ");
                        return;
                    }
                    int status = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, 0);

                    switch (status) {
                        case BluetoothDevice.BOND_BONDED:
                            showToast("BOND_BONDED " + device.toString());
                            break;
                        case BluetoothDevice.BOND_BONDING:
                            showToast("BOND_BONDING " + device.toString());
                            break;
                        case BluetoothDevice.BOND_NONE:
                            showToast("BOND_NONE " + device.toString());
                            break;
                    }
                    break;
                default:
                    showToast("mHeadsetReceiver2 OTHER ");
                    break;
            }
        }

    };

    private void aa() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            int permissionCheck = 0;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                permissionCheck = this.checkSelfPermission(("Manifest.permission.ACCESS_FINE_LOCATION"));
                permissionCheck += this.checkSelfPermission(("Manifest.permission.ACCESS_COARSE_LOCATION"));
                if (permissionCheck != 0) {
                    this.requestPermissions(new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001);
                }
                ;
            } else {
                Log.d(TAG, "確認權限");
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bluetoothController = new BluetoothController(BluetoothAdapter.getDefaultAdapter());
        progressBar = findViewById(R.id.progress_bar);
        initUI();
        /** 廣播過濾器，過濾接廣播條件 **/
//        IntentFilter filter = new IntentFilter();//監聽
//        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
//        registerReceiver(mHeadsetReceiver, filter);
        IntentFilter filter2 = new IntentFilter();//監聽
        filter2.addAction(BluetoothDevice.ACTION_FOUND);
        filter2.addAction(Manifest.permission.BLUETOOTH_SCAN);
        filter2.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        filter2.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter2.addAction(Manifest.permission.ACCESS_FINE_LOCATION);
        filter2.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        filter2.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        registerReceiver(mHeadsetReceiver2, filter2);
        checkPermissions();

        aa();
    }


    private void checkPermissions() {
        int permission1 = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permission2 = ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN);
        if (permission1 != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_STORAGE,
                    1
            );
        } else if (permission2 != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_LOCATION,
                    1
            );
        }
    }

    private void initUI() {
        listView = findViewById(R.id.action_list);
        deviceAdapter = new DeviceAdapter(mDeviceList, this);
        listView.setAdapter(deviceAdapter);
        listView.setOnItemClickListener(bindDeviceClick);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mHeadsetReceiver);
        unregisterReceiver(mHeadsetReceiver2);
    }


    private final BroadcastReceiver mHeadsetReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("BroadcastReceiver", "廣播");
            int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1);
            switch (state) {
                case BluetoothAdapter.STATE_OFF:
                    showToast("STATE_OFF");
                    break;
                case BluetoothAdapter.STATE_ON:
                    showToast("STATE_ON");
                    break;
                case BluetoothAdapter.STATE_TURNING_ON:
                    showToast("STATE_TURNING_ON");
                    break;
                case BluetoothAdapter.STATE_TURNING_OFF:
                    showToast("STATE_TURNING_OFF");
                    break;
                default:
                    showToast(" OTHER ");
                    break;
            }
        }

    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void isSupportBluetooth(View view) {
        if (bluetoothController.isSupportBluetooth()) {
            showToast("支持藍牙");
        } else {
            showToast("不支持藍牙");

        }

    }

    public void isBluetoothEnable(View view) {

        if (bluetoothController.getBluetoothStatus()) {
            showToast("啟用藍牙");
        } else {
            showToast("不啟用藍牙");

        }

    }

    public void requestTurnOnBluetooth(View view) {

        bluetoothController.turnOnBluetooth(this, REQUEST_CODE);
    }

    public void requestTurnOffBluetooth(View view) {

        bluetoothController.turnOffBluetooth();
    }

    private void showToast(String test) {
        if (toast == null) {
            toast = Toast.makeText(this, test, Toast.LENGTH_SHORT);
        } else {
            toast.setText(test);
        }
        toast.show();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.enable_visiblity) {
            bluetoothController.enableVisibly(this);
        } else if (id == R.id.find_device) {

            /** bluetoothAdapter.startDiscovery();需要位置權限，但即使授予了位置權限，當它被禁用時，藍牙 API 也不起作用。
             所以建議大家添加如，確保權限生效： **/
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            boolean isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (!isGpsEnabled) {
                startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
            }
            bluetoothController.findDevice();

            deviceAdapter.refresh(mDeviceList);
            listView.setOnItemClickListener(bindDeviceClick);
        } else if (id == R.id.bonded_device) {
            mBluetoothDeviceList = bluetoothController.getBondedDeviceList();
            deviceAdapter.refresh(mBluetoothDeviceList);
            listView.setOnItemClickListener(null);
        }

        return super.onOptionsItemSelected(item);
    }

    private AdapterView.OnItemClickListener bindDeviceClick = new AdapterView.OnItemClickListener() {

        @SuppressLint("MissingPermission")
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            BluetoothDevice device = mDeviceList.get(position);
            device.createBond();
        }
    };


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        System.out.println("requestCode: " + requestCode);
        System.out.println("resultCode: " + resultCode);
        System.out.println("data: " + data);
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            showToast("藍牙 open ok");
        } else {
            showToast("藍牙 open error");
        }
    }


}