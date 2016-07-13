package com.nodrex.connectedworld.protocol;

import android.support.v7.widget.SwitchCompat;
import android.view.View;

import com.nodrex.android.tools.Util;
import com.nodrex.connectedworld.helper.Helper;

public class LedOff extends AsyncTaskParam {

    private final String Error = "IP_PORT should not be null or empty";

    private String value;
    private View progressBar;
    private SwitchCompat switchCompat;

    public String getValue(){
        return value;
    }

    public LedOff(String ipAndPort,View progressBar,SwitchCompat switchCompat) {
        super(Protocol.LED_OFF);
        if(ipAndPort == null || "".equals(ipAndPort)) throw new NullPointerException(ipAndPort);
        this.value = ipAndPort + Protocol.LED_OFF_ARDUINO;
        this.progressBar = progressBar;
        this.switchCompat =switchCompat;
        start();
    }

    private void start(){
        if(switchCompat != null) switchCompat.setEnabled(false);
        if(progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
            Util.log("progressBar is not null...........................");
        }
    }

    public void done(){
        if(switchCompat != null) {
            Helper.recheckByDevice = true;
            switchCompat.setEnabled(true);
            switchCompat.setChecked(false);
        }
        if(progressBar != null) {
            progressBar.setVisibility(View.GONE);
            Util.log("hiding progressbar____________");
        }
    }
}
