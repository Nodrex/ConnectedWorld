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

    public static boolean recheckByDevice;

}
