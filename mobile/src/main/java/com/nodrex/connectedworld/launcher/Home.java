package com.nodrex.connectedworld.launcher;

import android.app.Activity;
import android.os.Bundle;

import com.nodrex.android.tools.Util;

/**
 * This class is used to start when ok google hears start home.
 * @author Nodar Tchumbadze
 * @since 2016
 * @version 1.0
 */
public class Home extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Util.log("Home");

    }
}
