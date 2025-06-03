package com.example.roboticarm;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import androidx.appcompat.app.AppCompatActivity;
public class DeviceList extends AppCompatActivity {
    List<String> DeviceIPs = new ArrayList();
    List<String> DeviceNames = new ArrayList();
    TextView availableDevices;
    ListView devicesList;
    setDeviceList setIt;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_device_list);
        this.devicesList = (ListView) findViewById(R.id.list);
        this.availableDevices = (TextView) findViewById(R.id.tv_availableDevices);
        this.availableDevices.setText("scanning for devices...");
        new DiscoverTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
        this.setIt = new setDeviceList();
        this.devicesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (DeviceList.this.availableDevices.getText().equals("Available Devices")) {
                    Intent select = new Intent(DeviceList.this, Select.class);
                    select.putExtra("ip_add", DeviceList.this.DeviceIPs.get(i));
                    DeviceList.this.startActivity(select);
                    DeviceList.this.finish();
                }
            }
        });
    }

    class setDeviceList extends BaseAdapter {
        setDeviceList() {
        }

        public int getCount() {
            DeviceList.this.availableDevices.setText("Available Devices");
            if (DeviceList.this.DeviceNames.size() < 1) {
                DeviceList.this.availableDevices.setText("No Devices Found");
            }
            return DeviceList.this.DeviceNames.size();
        }

        public Object getItem(int i) {
            return null;
        }

        public long getItemId(int i) {
            return 0;
        }

        public View getView(int i, View view, ViewGroup viewGroup) {
            View view2 = DeviceList.this.getLayoutInflater().inflate(R.layout.list_layout, (ViewGroup) null);
            ((TextView) view2.findViewById(R.id.tv_name)).setText(DeviceList.this.DeviceNames.get(i));
            ((TextView) view2.findViewById(R.id.tv_ip)).setText(DeviceList.this.DeviceIPs.get(i));
            return view2;
        }
    }

    private class DiscoverTask extends AsyncTask<Void, Void, Void> {
        private DiscoverTask() {
        }

        /* access modifiers changed from: protected */
        public Void doInBackground(Void... voids) {
            try {
                DeviceList.this.DeviceNames = new ArrayList();
                DeviceList.this.DeviceIPs = new ArrayList();
                InetAddress group = InetAddress.getByName("239.255.255.250");
                MulticastSocket s = new MulticastSocket(1800);
                s.joinGroup(group);
                s.send(new DatagramPacket("M-SEARCH * HTTP/1.1\\r\\n".getBytes(), "M-SEARCH * HTTP/1.1\\r\\n".length(), group, 1800));
                byte[] buf = new byte[1000];
                s.setSoTimeout(1000);
                DatagramPacket recv = new DatagramPacket(buf, buf.length);
                s.receive(recv);
                boolean finished = false;
                while (!finished) {
                    try {
                        s.receive(recv);
                        String received = new String(recv.getData()).substring(0, recv.getLength());
                        if (received.contains("DeviceName:")) {
                            String DeviceName = received.split("DeviceName:")[1].split("\r\n")[0];
                            Log.d("DeviceName", DeviceName);
                            DeviceList.this.DeviceNames.add(DeviceName);
                            DeviceList.this.DeviceIPs.add(recv.getAddress().toString().replace("/", ""));
                        }
                    } catch (SocketTimeoutException e) {
                        finished = true;
                    }
                }
                return null;
            } catch (UnknownHostException e2) {
                e2.printStackTrace();
                return null;
            } catch (IOException e3) {
                e3.printStackTrace();
                return null;
            }
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            DeviceList.this.devicesList.setAdapter(DeviceList.this.setIt);
        }
    }
}
