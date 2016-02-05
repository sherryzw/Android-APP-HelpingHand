package com.example.wenzhao.helpinghand;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.wenzhao.helpinghand.BLEHandler.BleDeviceInfo;
import com.example.wenzhao.helpinghand.BLEHandler.BluetoothLeService;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //Constant variables for activity result handling
    private static final int REQ_ENABLE_BT = 0;
    private static final int REQ_DEVICE_ACT = 1;

    //BLE management
    private List<BleDeviceInfo> mDeviceInfoList;
    private BluetoothLeService mBluetoothLeService = null;
    private BluetoothAdapter mBtAdapter = null;
    private boolean mScanning = false;
    private IntentFilter mFilter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize device list container
        mDeviceInfoList = new ArrayList<BleDeviceInfo>();

        // Register the BroadcastReceiver
        mFilter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        mFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        mFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBtAdapter = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.opt_bt:
                onBluetooth();
                break;
            case R.id.opt_exit:
                Toast.makeText(this, "Exit...", Toast.LENGTH_SHORT).show();
                finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void onBluetooth() {
        Intent settingsIntent = new Intent(
                android.provider.Settings.ACTION_BLUETOOTH_SETTINGS);
        startActivity(settingsIntent);
    }

    public void onBtnScan(View view) {
        if (mScanning) {
 //           stopScan();
        } else {
 //           startScan();
        }
    }


}
