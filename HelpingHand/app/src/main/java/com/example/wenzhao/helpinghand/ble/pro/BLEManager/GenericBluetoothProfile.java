package com.example.wenzhao.helpinghand.ble.pro.BLEManager;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.util.Log;


import com.example.wenzhao.helpinghand.ble.pro.ACCInfo.Point3D;
import com.example.wenzhao.helpinghand.ble.pro.ACCInfo.SensorTagGatt;

import java.util.List;

public class GenericBluetoothProfile {
	public static Point3D accData1;
	public static Point3D accData2;

	protected BluetoothDevice mBTDevice;
	protected BluetoothLeService mBTLeService;

	protected BluetoothGattService mBTService;
	protected BluetoothGattCharacteristic dataC;
	protected BluetoothGattCharacteristic configC;
	protected BluetoothGattCharacteristic periodC;

	public GenericBluetoothProfile(final Context con,BluetoothDevice device,BluetoothGattService service,BluetoothLeService controller) {
		super();
		this.mBTDevice = device;
		this.mBTService = service;
		this.mBTLeService = controller;
		this.dataC = null;
		this.periodC = null;
		this.configC = null;

		List<BluetoothGattCharacteristic> characteristics = this.mBTService.getCharacteristics();

		for (BluetoothGattCharacteristic c : characteristics) {
			if (c.getUuid().toString().equals(SensorTagGatt.UUID_MOV_DATA.toString())) {
				this.dataC = c;
			}
			if (c.getUuid().toString().equals(SensorTagGatt.UUID_MOV_CONF.toString())) {
				this.configC = c;
			}
			if (c.getUuid().toString().equals(SensorTagGatt.UUID_MOV_PERI.toString())) {
				this.periodC = c;
			}
		}
		accData1 = new Point3D(0,0,0);
		accData2 = new Point3D(0,0,0);
	}

	public void enableService() {
		mBTLeService.writeCharacteristic(this.configC, new byte[]{0x7F, 0x02});
		this.mBTLeService.setCharacteristicNotification(this.dataC, true);
		this.periodWasUpdated(900);
	}

	public boolean isDataC(BluetoothGattCharacteristic c) {
		if (this.dataC == null) return false;
		if (c.equals(this.dataC)) return true;
		else return false;
	}

	public void didUpdateValueForCharacteristic(BluetoothGattCharacteristic c,int dnum) {
		byte[] value = c.getValue();
		if (c.equals(this.dataC)){
			if ( dnum == 0) {
				Point3D v;
				v = convert(value);
				accData1.x = v.x;
				accData1.y = v.y;
				accData1.z = v.z;
				Log.e("acc", String.format("X:%.2fG, Y:%.2fG, Z:%.2fG", accData1.x, accData1.y, accData1.z));
			}
			else if(dnum ==1){
				Point3D v;
				v = convert(value);
				accData2.x = v.x;
				accData2.y = v.y;
				accData2.z = v.z;
				Log.e("acc", String.format("X:%.2fG, Y:%.2fG, Z:%.2fG", accData2.x, accData2.y, accData2.z));
			}
		}
	}

	public Point3D convert(final byte[] value) {
		final float SCALE = (float) 4096.0;
		int x = (value[7]<<8) + value[6];
		int y = (value[9]<<8) + value[8];
		int z = (value[11]<<8) + value[10];
		return new Point3D(((x / SCALE) * -1), y / SCALE, ((z / SCALE)*-1));
	}

	public void periodWasUpdated(int period) {
		if (period > 2450) period = 2450;
		if (period < 100) period = 100;
		byte p = (byte)((period / 10) + 10);
		mBTLeService.writeCharacteristic(this.periodC, p);
	}
}
