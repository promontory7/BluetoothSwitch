package com.xiaoshan.bluetoothswitch;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by chudong on 2016/4/10.
 */
public class DeviceListAdapter extends BaseAdapter {
    Context context;
    private ArrayList<BluetoothDevice> deviceList;

    public DeviceListAdapter(Context context, ArrayList<BluetoothDevice> deviceList) {
        this.context = context;
        this.deviceList = deviceList;

    }

    @Override
    public int getCount() {
        return deviceList == null ? 0 : deviceList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.cell_devicelist, null);
        }
        ((TextView) (convertView.findViewById(R.id.tv_name))).setText(deviceList.get(position).getName());
        ((TextView) (convertView.findViewById(R.id.tv_address))).setText(deviceList.get(position).getAddress());
        return convertView;
    }
}
