package com.example.wenzhao.helpinghand.ble.pro.HelpingHand;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.TextView;


import com.example.ti.ble.sensortag.R;
import com.example.wenzhao.helpinghand.ble.pro.ACCInfo.SensorTagGatt;
import com.example.wenzhao.helpinghand.ble.pro.BLEManager.BluetoothLeService;
import com.example.wenzhao.helpinghand.ble.pro.BLEManager.GenericBluetoothProfile;

import java.util.ArrayList;
import java.util.List;

public class DeviceActivity extends Activity {
	public static final String EXTRA_DEVICE = "EXTRA_DEVICE";
	public final static String EXTRA_ADDRESS = "com.example.ti.ble.common.EXTRA_ADDRESS";
	public static String deviceAddress = null;
	// BLE
	private BluetoothLeService mBtLeService = null;
	private ArrayList<BluetoothDevice> mBluetoothDevice = null;
	private ArrayList<BluetoothGatt> mBtGatt = null;
	private List<BluetoothGattService> mServiceList = null;
	private List<GenericBluetoothProfile> mProfiles;
	//GUI
	private TextView accText1 = null;
	private TextView accText2 = null;
	private int dnum = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acc_view);
		Intent intent = getIntent();
		// BLE
		mBtLeService = BluetoothLeService.getInstance();
		mBluetoothDevice =BluetoothLeService.getDevice();
				//mBluetoothDevice = intent.getParcelableExtra(EXTRA_DEVICE);
		mBtGatt = BluetoothLeService.getBtGatt();

		mServiceList = new ArrayList<BluetoothGattService>();
		mProfiles = new ArrayList<GenericBluetoothProfile>();

		accText1 = (TextView)findViewById(R.id.acc_data1);
		accText2 = (TextView)findViewById(R.id.acc_data2);
		mBtGatt.get(0).discoverServices();
		mBtGatt.get(1).discoverServices();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mGattUpdateReceiver);
        this.mProfiles = null;
	}

	@Override
	protected void onResume() {
		super.onResume();
		final IntentFilter fi = new IntentFilter();
		fi.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
		fi.addAction(BluetoothLeService.ACTION_DATA_READ);
		fi.addAction(BluetoothLeService.ACTION_DATA_NOTIFY);
		registerReceiver(mGattUpdateReceiver, fi);
	}

	private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        List<BluetoothGattService> serviceList;
        List<BluetoothGattCharacteristic> charList = new ArrayList<BluetoothGattCharacteristic>();

		@Override
		public void onReceive(final Context context, final Intent intent) {
			final String action = intent.getAction();
			final int status = intent.getIntExtra(BluetoothLeService.EXTRA_STATUS,
					BluetoothGatt.GATT_SUCCESS);
            if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    serviceList = mBtLeService.getSupportedGattServices();
                    if (serviceList.size() > 0) {
                        for (BluetoothGattService s : serviceList) {
                            List<BluetoothGattCharacteristic> c = s.getCharacteristics();
							charList.addAll(c);
                        }
                    }
                    Thread worker = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            for (BluetoothGattService s : serviceList) {
                                if (s.getUuid().toString().compareTo(SensorTagGatt.UUID_MOV_SERV.toString()) == 0) {
									if(deviceAddress == null){
										GenericBluetoothProfile mov1 = new GenericBluetoothProfile(context,mBluetoothDevice.get(0),s,mBtLeService);
										mProfiles.add(mov1);
										deviceAddress = intent.getStringExtra(EXTRA_ADDRESS);
									}
									else if(deviceAddress != null){
										GenericBluetoothProfile mov2 = new GenericBluetoothProfile(context,mBluetoothDevice.get(1),s,mBtLeService);
										mProfiles.add(mov2);

									}
                                }
                            }
                            for (final GenericBluetoothProfile p : mProfiles) {
                                runOnUiThread(new Runnable() {
									@Override
									public void run() {
										p.enableService();
									}
								});
                            }

                        }
                    });
                    worker.start();
                }
			} else if (BluetoothLeService.ACTION_DATA_NOTIFY.equals(action)) {
				dnum = 0;
				// Notification
				String uuidStr = intent.getStringExtra(BluetoothLeService.EXTRA_UUID);
				for (BluetoothGattCharacteristic tempC : charList) {
					if ((tempC.getUuid().toString().equals(uuidStr))) {
						for (GenericBluetoothProfile p : mProfiles) {
							if (p.isDataC(tempC)) {
								p.didUpdateValueForCharacteristic(tempC , dnum);
								dnum ++;
								accText1.setText(String.format("X:%.2fG, Y:%.2fG, Z:%.2fG", GenericBluetoothProfile.accData1.x, GenericBluetoothProfile.accData1.y, GenericBluetoothProfile.accData1.z));
								accText2.setText(String.format("X:%.2fG, Y:%.2fG, Z:%.2fG", GenericBluetoothProfile.accData2.x, GenericBluetoothProfile.accData2.y, GenericBluetoothProfile.accData2.z));
							}
						}
						break;
					}
				}
			}
		}
	};
}
