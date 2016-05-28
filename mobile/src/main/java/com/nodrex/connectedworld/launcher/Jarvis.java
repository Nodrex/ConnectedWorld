package com.nodrex.connectedworld.launcher;

import android.app.Activity;
import android.os.Bundle;

import com.nodrex.connectedworld.Butler;

/**
 * This class is used to start when ok google hears start jarvis.
 * @author Nodar Tchumbadze
 * @since 2016
 * @version 1.0
 */
public class Jarvis extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Butler.startAppFromOkGoogle(this, Butler.Types.Jarvis);
    }
}
