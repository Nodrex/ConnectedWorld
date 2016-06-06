package com.nodrex.connectedworld.helper;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;

import com.nodrex.android.tools.Util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.UUID;

/**
 * Helper class.
 * @author Nodar Tchumbadze
 * @since 2016
 * @version 1.0
 */
public abstract class Helper {

    public static final String newLineAndTab = "\n\t";
    public static final String newLine = "\n";
    public static final String unknownDevice = "Unknown Device";
    public static final String GEORGIAN = "ge";

    public static class Ping extends AsyncTask<Integer,Void,String>{

        @Override
        protected String doInBackground(Integer... params) {
            try {
                return pingESP();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String data) {
            super.onPostExecute(data);
            Util.log(data);
        }
    }

    public static BluetoothDevice a007;
    public static BluetoothSocket mmSocket;
    public static OutputStream mmOutputStream;

    public static void goB(){
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if(pairedDevices.size() > 0)
        {
            for(BluetoothDevice device : pairedDevices)
            {

               // Util.log(device.getName());

                if(device.getName().equals("HC-06")) //Note, you will need to change this to match the name of your device
                {
                    a007 = device;
                    break;
                }
            }
        }

        Util.log(a007.toString());

        UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"); //Standard SerialPortService ID
        try {
            mmSocket = a007.createRfcommSocketToServiceRecord(uuid);
            mmSocket.connect();
            mmOutputStream = mmSocket.getOutputStream();
            InputStream mmInputStream = mmSocket.getInputStream();
            Util.log("Connected");
        } catch (IOException e) {
            Util.log(e.toString());
        }


    }

    public static void goB0(){
        try {
            mmOutputStream.write("Hellow from android world".getBytes());
        } catch (IOException e) {
            Util.log(e.toString());
        }
    }

    public static void go(){
        new Ping().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public static final String pingESP() throws Exception {
        //String data = /*"/?" +*/ /*URLEncoder.encode("pin", "UTF-8") + "=" +*/ URLEncoder.encode("6", "UTF-8");
        String data = "/?pin=4";
        Util.log("trying to send data: " + data);

        String text = null;
        BufferedReader reader = null;

        // Send data
        try {

            URL url = new URL("http://192.168.2.103:80"+ data);
            // Send POST data request 192.168.2.107

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);

            //conn.setChunkedStreamingMode(0);

            //conn.setConnectTimeout(timeOut);


            Util.log("data sent: " + data);

            String line = null;
            StringBuilder sb = new StringBuilder();
            // Get the server response
            int tryCounter = 1;
            for (int i = 0; i < tryCounter; i++) {
                try {
                    Util.log("GetStringFromUrl try: " + (i + 1));
                    reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    break;
                } catch (Exception e) {
                    Util.log("GetStringFromUrl -> Exception: " + e.toString());
                }
            }

            // Read Server Response
            while ((line = reader.readLine()) != null) {
                // Append server response in string
                sb.append(line + "\n");
            }

            text = sb.toString();
        } catch (Exception ex) {
            throw new Exception("GetStringFromUrl: " + ex.toString());
        } finally {
            try {
                reader.close();
            } catch (Exception ex) {}
        }

        return text;

    }

}
