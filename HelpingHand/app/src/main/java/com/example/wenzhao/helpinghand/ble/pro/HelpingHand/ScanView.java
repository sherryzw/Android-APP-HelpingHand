package com.example.wenzhao.helpinghand.ble.pro.HelpingHand;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ti.ble.sensortag.R;

import java.util.List;

public class ScanView extends Fragment {

  private MainActivity mActivity = null;
  private DeviceListAdapter mDeviceAdapter = null;
  private Button mBtnScan = null;
  private ListView mDeviceListView = null;

  public static ScanView newInstance() {
    ScanView fragment = new ScanView();
    Bundle args = new Bundle();
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // The last two arguments ensure LayoutParams are inflated properly.
    View view = inflater.inflate(R.layout.fragment_scan, container, false);
    mActivity = (MainActivity)getActivity();
    // Initialize widgets
    mBtnScan = (Button) view.findViewById(R.id.btn_scan);
    mDeviceListView = (ListView) view.findViewById(R.id.device_list);
    mDeviceListView.setClickable(true);
    mDeviceListView.setOnItemClickListener(mDeviceClickListener);
    // Alert parent activity
    return view;
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
  }

  void notifyDataSetChanged() {
    List<BluetoothDevice> deviceList = mActivity.getDeviceList();
    if (mDeviceAdapter == null) {
      mDeviceAdapter = new DeviceListAdapter(mActivity,deviceList);
    }
    mDeviceListView.setAdapter(mDeviceAdapter);
    mDeviceAdapter.notifyDataSetChanged();
  }

  // Listener for device list
  private OnItemClickListener mDeviceClickListener = new OnItemClickListener() {
    public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
      mActivity.onDeviceClick(pos);
    }
  };

  //
  // CLASS DeviceAdapter: handle device list
  //
  class DeviceListAdapter extends BaseAdapter {
    private List<BluetoothDevice> mDevices;
    private LayoutInflater mInflater;

    public DeviceListAdapter(Context context, List<BluetoothDevice> devices) {
      mInflater = LayoutInflater.from(context);
      mDevices = devices;
    }

    public int getCount() {
      return mDevices.size();
    }

    public Object getItem(int position) {
      return mDevices.get(position);
    }

    public long getItemId(int position) {
      return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
      ViewGroup vg;

      if (convertView != null) {
        vg = (ViewGroup) convertView;
      } else {
        vg = (ViewGroup) mInflater.inflate(R.layout.element_device, null);
      }

      BluetoothDevice device = mDevices.get(position);

      String name;
      name = device.getName();
      if (name == null) {
      	name = new String("Unknown device");
      }

      String descr = name + "\n" + device.getAddress();
      ((TextView) vg.findViewById(R.id.descr)).setText(descr);

      return vg;
    }
  }

}
