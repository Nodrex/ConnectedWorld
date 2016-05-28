package com.nodrex.connectedworld.helper;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.nodrex.android.tools.Util;
import com.nodrex.connectedworld.MainActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Set;
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
                return pingESP(params[0]);
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
            mmOutputStream.write("Hellow from fucked world".getBytes());
        } catch (IOException e) {
            Util.log(e.toString());
        }
    }

    public static void go(int index){
        new Ping().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,index);
    }

    public static final String pingESP(int index) throws Exception {
        Util.log("int index: " + index);
        String data = "/?" /*+ URLEncoder.encode("pin", "UTF-8") + "="*/ + URLEncoder.encode(""+ index/*"11"*/, "UTF-8");

        String text = null;
        BufferedReader reader = null;

        // Send data
        try {

            URL url = new URL("http://192.168.0.4:80");
            // Send POST data request

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setChunkedStreamingMode(0);
            //conn.setConnectTimeout(timeOut);

            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();

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
