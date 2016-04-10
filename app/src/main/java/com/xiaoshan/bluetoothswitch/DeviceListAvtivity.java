package com.xiaoshan.bluetoothswitch;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by chudong on 2016/4/10.
 */
public class DeviceListAvtivity extends AppCompatActivity {

    ArrayList<BluetoothDevice> newsDevices =new  ArrayList<BluetoothDevice>();
    ArrayList<BluetoothDevice> pairedDevices;

    BaseAdapter newsAdapter;
    BluetoothAdapter mBTAdaoter;

    // Return Intent extra
    public static String EXTRA_DEVICE_ADDRESS = "device_address";

    @Bind(R.id.listview_paired)
    ListView listviewPaired;
    @Bind(R.id.listview_news)
    ListView listviewNews;
    @Bind(R.id.btn_scan)
    Button btnScan;
    @Bind(R.id.tv_newdevicestitle)
    TextView tvNewdevicestitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devicelist);
        ButterKnife.bind(this);
        setResult(Activity.RESULT_CANCELED);

        mBTAdaoter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedDeviceSet = mBTAdaoter.getBondedDevices();
        if (pairedDeviceSet.size() > 0) {
            for (BluetoothDevice device : pairedDeviceSet) {
                if (pairedDevices == null) {
                    pairedDevices = new ArrayList<BluetoothDevice>();
                }
                pairedDevices.add(device);
            }
            listviewPaired.setAdapter(new DeviceListAdapter(this, pairedDevices));
            listviewPaired.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    BluetoothDevice device = mBTAdaoter.getRemoteDevice(pairedDevices.get(position).getAddress());
                    // Attempt to connect to the device
                    Intent intent = new Intent();
                    intent.putExtra(EXTRA_DEVICE_ADDRESS, device.getAddress());

                    // Set result and finish this Activity
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            });
        }

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                discoveryNewDevices();
            }
        });

        listviewNews.setAdapter(newsAdapter =new DeviceListAdapter(this,newsDevices) );
        listviewNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                BluetoothDevice device = mBTAdaoter.getRemoteDevice(newsDevices.get(position).getAddress());
                // Attempt to connect to the device
                Intent intent = new Intent();
                intent.putExtra(EXTRA_DEVICE_ADDRESS, device.getAddress());

                // Set result and finish this Activity
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });

        this.registerReceiver(mReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
        this.registerReceiver(mReceiver, new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED));


    }

    private void discoveryNewDevices() {
        tvNewdevicestitle.setText("正在搜索新设备...");
        if (mBTAdaoter.isDiscovering()) {
            mBTAdaoter.cancelDiscovery();
        }
        mBTAdaoter.startDiscovery();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        if (mBTAdaoter != null) {
            mBTAdaoter.cancelDiscovery();
        }
        this.unregisterReceiver(mReceiver);
    }


    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent
                        .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {

                    newsDevices.add(device);
                    newsAdapter.notifyDataSetChanged();
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                tvNewdevicestitle.setText("新设备");
            }
        }
    };

}
