package com.example.bluetoothserverapplication.connect;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.AdapterView;

import androidx.annotation.RequiresPermission;
import androidx.core.app.ActivityCompat;

import com.example.bluetoothserverapplication.MainActivity;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.UUID;

public class ConnectThread extends Thread {

    private static final UUID MY_UUID = UUID.fromString(Constant.CONNECTTION_UUID);
    //    private final UUID MY_UUID = ParcelUuid.fromString(Constant.CONNECTTION_UUID).getUuid();
    private BluetoothSocket mmSocket;

    private final BluetoothDevice mmDevice;
    private BluetoothAdapter mBluetoothAdapter;
    private final Handler mHandler;
    private ConnectedThread mConnectedThread;
    private MainActivity mMainActivity;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.BLUETOOTH_PRIVILEGED,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION
    };

    //    private final InputStream mmInputStream ;
//    private final OutputStream mmOutputStream;
//    private Handler mHandler;
//
//    private final BluetoothServerSocket mmServerSocket;
    @SuppressLint("MissingPermission")
    @RequiresPermission(value = "android.permission.PERMISSIONS_STORAGE")
    public ConnectThread(MainActivity mainActivity,BluetoothDevice device, BluetoothAdapter adapter, Handler handler) {
        this.mMainActivity = mainActivity;
        this.mmDevice = device;
        this.mBluetoothAdapter = adapter;
        this.mHandler = handler;
        BluetoothSocket tmp = null;

        try {
//            tmp = device.createInsecureRfcommSocketToServiceRecord(MY_UUID);
//            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
//                // TODO: Consider calling
//                //    ActivityCompat#requestPermissions
//                // here to request the missing permissions, and then overriding
//                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                //                                          int[] grantResults)
//                // to handle the case where the user grants the permission. See the documentation
//                // for ActivityCompat#requestPermissions for more details.
//                return TODO;
//            }
//            if (Build.VERSION.SDK_INT >= 6.0) {
////                ActivityCompat.requestPermissions(this, new String(){
////                    Manifest.permission.ACCESS_FINE_LOCATION,
////                    Manifest.permission.ACCESS_COARSE_LOCATION,
////                    Manifest.permission.ACCESS_BACKGROUND_LOCATION},1);
//                ActivityCompat.requestPermissions(MainActivity., PERMISSIONS_STORAGE, 1);
//            }
//            if (ActivityCompat.checkSelfPermission(mMainActivity, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(
//                        mMainActivity,
//                        PERMISSIONS_STORAGE,
//                        1
//                );
//                // TODO: Consider calling
//                //    ActivityCompat#requestPermissions
//                // here to request the missing permissions, and then overriding
//                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                //                                          int[] grantResults)
//                // to handle the case where the user grants the permission. See the documentation
//                // for ActivityCompat#requestPermissions for more details.
//            }
            tmp = device.createInsecureRfcommSocketToServiceRecord(Constant.MY_UUID_INSECURE);
//            tmp = mmDevice.createRfcommSocketToServiceRecord(mmDevice.getUuids()[0].getUuid());
            Log.d("createInsecureRfcommSocketToServiceRecord", mmDevice.getUuids()[0].getUuid().toString());
//            BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
//            if ( mAllowInsecureConnections ) {
            Method method;

//                method = device.getClass().getMethod("createRfcommSocket", new Class[] { int.class } );
//                tmp = (BluetoothSocket) method.invoke(device, 1);
//            }
//            else {
//                tmp = device.createRfcommSocketToServiceRecord( SerialPortServiceClass_UUID );
//            }
        } catch (IOException e) {
            Log.e(TAG, "Socket's listen() method failed", e);
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        mmSocket = tmp;
//        Log.d("mmSocket", tmp.toString());
        run();
    }
    @RequiresPermission(allOf = {
            android.Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.BLUETOOTH_SCAN
    })
    @SuppressLint("MissingPermission")
    public void run() {
        System.out.println("~~~~~~~~ConnectThread");
        BluetoothSocket socket = null;
//        if (ActivityCompat.checkSelfPermission( ConnectThread.this , Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(
//                    mMainActivity,
//                    PERMISSIONS_STORAGE,
//                    1
//            );
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//        }

//        if (ActivityCompat.checkSelfPermission(mMainActivity, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            Log.e("!!!!!!!!!!!","BLUETOOTH_SCAN");
//            return;
//        }
        Log.d("mmSocket", "連接?" + mmDevice.getName());

        mBluetoothAdapter.cancelDiscovery();
//        while (true) {
            try {
                if (!mmSocket.isConnected()) {
                    Log.d("isConnected", "NO");

                    mmSocket.connect();
                    mHandler.sendMessage(mHandler.obtainMessage(Constant.MSG_GOT_A_CLINET, mmDevice.getName()));
                }
                Log.d("mmSocket", "連接");
            } catch (IOException e) {
                if (mmSocket != null) {
                    try {
                        mmSocket.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    mmSocket = null;
                }
                Log.d("mmSocket", "連接:" + e);
                mHandler.sendMessage(mHandler.obtainMessage(Constant.MSG_ERROR, e));
//                try {
//                    mmSocket.close();
//                } catch (IOException ex) {
//                    ex.printStackTrace();
//                }
//                try {
//                    mmDevice.getClass().getMethod("createRfcommSocketToServiceRecord",new  Class[]{int.class}).invoke(mmDevice,mmSocket.connect()) ;
//                } catch (NoSuchMethodException ex) {
//                    ex.printStackTrace();
//                } catch (IOException ex) {
//                    ex.printStackTrace();
//                }
                return;
//            }
        }

        manageConnectedSocket(mmSocket);
    }

    private void manageConnectedSocket(BluetoothSocket mmSocket) {
        mHandler.sendEmptyMessage(Constant.MSG_CONNECTED_TO_SERVER);
        mConnectedThread = new ConnectedThread(mmSocket, mHandler);
        System.out.println("~~~~~~~~ConnectedThread  2  ");
        mConnectedThread.start();
    }

    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void sendData(byte[] data) {
        if (mConnectedThread != null) {
            mConnectedThread.write(data);
        }
    }
}
