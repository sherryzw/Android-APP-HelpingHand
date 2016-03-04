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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.ti.ble.sensortag.R;
import com.example.wenzhao.helpinghand.ble.pro.ACCInfo.SensorTagGatt;
import com.example.wenzhao.helpinghand.ble.pro.BLEManager.BluetoothLeService;
import com.example.wenzhao.helpinghand.ble.pro.BLEManager.GenericBluetoothProfile;
import com.example.wenzhao.helpinghand.ble.pro.Fragment.InputFragment;

import java.util.ArrayList;
import java.util.List;

public class DeviceActivity extends Activity {
	// BLE
	private BluetoothLeService mBtLeService = null;
	private ArrayList<BluetoothDevice> mBluetoothDevice = null;
	private ArrayList<BluetoothGatt> mBtGatt = null;
	private List<GenericBluetoothProfile> mProfiles;
	private boolean ActivateTwo;
	private  double realtimesum1;
	private  double realtimesum2;

	//GUI
	private TextView infoText = null;
	private TextView ratioText1 = null;
	private ImageView imageView1 = null;
	private ImageView imageView2 = null;
	private ImageView imageView3 = null;

	private Button btnFinish;
	public double ratio;
	public static List<Double> M1OverTime;
	public static List<Double> M2OverTime;

	public static float time = 0;
	long startTime;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		startTime = System.currentTimeMillis();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acc_view);
		// BLE
		ActivateTwo = false;
		mBtLeService = BluetoothLeService.getInstance();
		mBluetoothDevice = BluetoothLeService.getDevice();
		mBtGatt = BluetoothLeService.getBtGatt();
		mProfiles = new ArrayList<GenericBluetoothProfile>();
		M1OverTime = new ArrayList<Double>();
		M2OverTime = new ArrayList<Double>();
		realtimesum1 = 0;
		realtimesum1 = 0;
		ratioText1 = (TextView) findViewById(R.id.ratio1);
		infoText = (TextView) findViewById(R.id.textView18);
		btnFinish = (Button) findViewById(R.id.btn_finish);

		imageView1 = (ImageView) findViewById(R.id.imageView3);
		imageView2 = (ImageView) findViewById(R.id.imageView4);
		imageView3 = (ImageView) findViewById(R.id.imageView5);

		imageView1.setImageResource(R.drawable.tabletop1);
		imageView2.setImageResource(R.drawable.tabletop2);
		imageView3.setImageResource(R.drawable.tabletop3);

		infoText.setText("Keep going, " + InputFragment.ChildName +
				". Use both hands to make these towers.");

		Thread worker = new Thread(new Runnable() {
			@Override
			public void run() {
				for (BluetoothGattService s : MainActivity.serviceList1) {
					if (s.getUuid().toString().compareTo(SensorTagGatt.UUID_MOV_SERV.toString()) == 0) {
						GenericBluetoothProfile mov1 = new GenericBluetoothProfile(DeviceActivity.this, mBluetoothDevice.get(0),mBtGatt.get(0), s, mBtLeService);
						mProfiles.add(mov1);
					}
				}
				for (BluetoothGattService s : MainActivity.serviceList2) {
					if (s.getUuid().toString().compareTo(SensorTagGatt.UUID_MOV_SERV.toString()) == 0) {
						GenericBluetoothProfile mov2 = new GenericBluetoothProfile(DeviceActivity.this, mBluetoothDevice.get(1),mBtGatt.get(1), s, mBtLeService);
						mProfiles.add(mov2);
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

		final Intent mResultIntent = new Intent(this, ResultActivity.class);
		btnFinish.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				long endTime = System.currentTimeMillis() - startTime;
				time = (float) endTime / 1000;
				unregisterReceiver(mGattUpdateReceiver1);
				startActivity(mResultIntent);
			}
		});
	}


	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mBtGatt.size() != 0) {
			for (BluetoothGatt gatt : mBtGatt) {
				gatt.close();
				gatt = null;
			}
		}
		mBtGatt = null;
		mBluetoothDevice =null;
		unregisterReceiver(mGattUpdateReceiver1);
		this.mProfiles = null;
	}

	@Override
	protected void onResume() {
		super.onResume();
		if(ResultActivity.Finish == 1){
			if (mBtGatt.size() != 0) {
				for (BluetoothGatt gatt : mBtGatt) {
					gatt.close();
					gatt = null;
				}
			}
			mBtGatt = null;
			if(this.mBluetoothDevice !=null) this.mBluetoothDevice =null;
			if (this.mProfiles != null)this.mProfiles = null;
			finish();
		}
		startTime = System.currentTimeMillis();
		M1OverTime.clear();
		M2OverTime.clear();
		final IntentFilter fi = new IntentFilter();
		fi.addAction(BluetoothLeService.ACTION_DATA_READ);
		fi.addAction(BluetoothLeService.ACTION_DATA_NOTIFY);
		fi.addAction(BluetoothLeService.ACTION_DATA_NOTIFY1);
		registerReceiver(mGattUpdateReceiver1, fi);
	}

	private final BroadcastReceiver mGattUpdateReceiver1 = new BroadcastReceiver() {
		@Override
		public void onReceive(final Context context, final Intent intent) {
			final String action = intent.getAction();
			final int status = intent.getIntExtra(BluetoothLeService.EXTRA_STATUS,
					BluetoothGatt.GATT_SUCCESS);

			if (BluetoothLeService.ACTION_DATA_NOTIFY.equals(action)) {
				// Notification
				String uuidStr = intent.getStringExtra(BluetoothLeService.EXTRA_UUID);
				for (BluetoothGattCharacteristic tempC : MainActivity.charList1) {
					if ((tempC.getUuid().toString().equals(uuidStr))) {
						GenericBluetoothProfile p = mProfiles.get(0);
						if (p.isDataC(tempC)) {
							p.didUpdateValueForCharacteristic(tempC, 0);
							double ax1 = GenericBluetoothProfile.accData1.x;
							double ay1 = GenericBluetoothProfile.accData1.y;
							double az1 = 1+GenericBluetoothProfile.accData1.z;
							if(ActivateTwo){


							}
						}
						break;
					}
				}
			}

			if (BluetoothLeService.ACTION_DATA_NOTIFY1.equals(action)) {
				// Notification
				ActivateTwo = true;
				String uuidStr = intent.getStringExtra(BluetoothLeService.EXTRA_UUID);
				for (BluetoothGattCharacteristic tempC : MainActivity.charList2) {
					if ((tempC.getUuid().toString().equals(uuidStr))) {
						GenericBluetoothProfile p = mProfiles.get(1);
						if (p.isDataC(tempC)) {
							p.didUpdateValueForCharacteristic(tempC, 1);
							double ax2 = GenericBluetoothProfile.accData2.x;
							double ay2 = GenericBluetoothProfile.accData2.y;
							double az2 = 1+GenericBluetoothProfile.accData2.z;
							double ax1 = GenericBluetoothProfile.accData1.x;
							double ay1 = GenericBluetoothProfile.accData1.y;
							double az1 = 1+GenericBluetoothProfile.accData1.z;
							//ratio = Math.sqrt(ax1*ax1 + ay1*ay1 + az1*az1) / (Math.sqrt(ax1*ax1 + ay1*ay1 + az1*az1) + Math.sqrt(ax2*ax2 + ay2*ay2 + az2*az2));
							//ratio = ratio * 100;

							if(Math.sqrt(ax2*ax2 + ay2*ay2 + az2*az2)>0.06){
								M2OverTime.add(Math.sqrt(ax2 * ax2 + ay2 * ay2 + az2 * az2));
								realtimesum2 += Math.sqrt(ax2 * ax2 + ay2 * ay2 + az2 * az2);
							}else{
								M2OverTime.add(0.0);
							}

							if (Math.sqrt(ax1*ax1 + ay1*ay1 + az1*az1)>0.06){
								M1OverTime.add(Math.sqrt(ax1*ax1 + ay1*ay1 + az1*az1));
								realtimesum1 +=Math.sqrt(ax1*ax1 + ay1*ay1 + az1*az1);
							}else{
								M1OverTime.add(0.0);
							}
							if (realtimesum1<realtimesum2) {
								ratio = 100 *realtimesum1/(realtimesum1+realtimesum2);
							}else {
								ratio = 100 *realtimesum2/(realtimesum1+realtimesum2);
							}
							ratioText1.setText("Weak arm(" + InputFragment.WeakArm
									+ ")" + String.format(":%.2f", ratio) + "%");
						}
						break;
					}
				}
			}
		}
	};
}
