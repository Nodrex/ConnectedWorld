package com.nodrex.connectedworld.protocol;

import android.app.Activity;
import android.support.v7.widget.SwitchCompat;
import android.view.View;

import com.nodrex.android.tools.UiUtil;
import com.nodrex.android.tools.Util;
import com.nodrex.connectedworld.helper.Constants;
import com.nodrex.connectedworld.helper.Helper;

public class LedOff extends AsyncTaskParam {

    public static final String Error = "IP should not be null or empty";
    public static final String DATA = "d=6 ";

    private Activity activity;
    private String ip;
    private View progressBar;
    private SwitchCompat switchCompat;

    public LedOff(Activity activity, String ip, View progressBar, SwitchCompat switchCompat) {
        super(Protocol.LED_OFF);
        Helper.checkStrContent(ip);
        this.activity = activity;
        this.ip = ip;
        this.progressBar = progressBar;
        this.switchCompat =switchCompat;
        start();
    }

    public String getIp(){
        return ip;
    }

    private void start(){
        UiUtil.disable(switchCompat);
        UiUtil.show(progressBar);
    }

    public void done(){
        UiUtil.enable(switchCompat);
        UiUtil.hide(progressBar);
    }

    public void failed(){
        if(switchCompat != null) {
            Helper.recheckByDevice = true;
            switchCompat.setEnabled(true);
            switchCompat.setChecked(false);
        }
        UiUtil.hide(progressBar);
        Util.toast(activity,"Unfortunately can not turn lig off");
    }
}
