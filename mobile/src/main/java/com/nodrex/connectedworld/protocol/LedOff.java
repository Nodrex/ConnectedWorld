package com.nodrex.connectedworld.protocol;

import android.support.v7.widget.SwitchCompat;
import android.view.View;

import com.nodrex.android.tools.Util;
import com.nodrex.connectedworld.helper.Constants;
import com.nodrex.connectedworld.helper.Helper;

public class LedOff extends AsyncTaskParam {

    private final String Error = "IP should not be null or empty";

    private String ip;
    public static final String DATA = "d=6 ";
    private View progressBar;
    private SwitchCompat switchCompat;

    public LedOff(String ip,View progressBar,SwitchCompat switchCompat) {
        super(Protocol.LED_OFF);
        if(ip == null || "".equals(ip)) throw new NullPointerException(ip);
        this.ip = ip;
        this.progressBar = progressBar;
        this.switchCompat =switchCompat;
        start();
    }

    public String getIp(){
        return ip;
    }

    private void start(){
        //if(switchCompat != null) switchCompat.setEnabled(false);
        if(progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    public void done(){
        /*if(switchCompat != null) {
            Helper.recheckByDevice = true;
            switchCompat.setEnabled(true);
            switchCompat.setChecked(false);
        }*/
        if(progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
    }
}
