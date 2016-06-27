package com.nodrex.connectedworld.helper;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import com.nodrex.android.tools.Util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

}
