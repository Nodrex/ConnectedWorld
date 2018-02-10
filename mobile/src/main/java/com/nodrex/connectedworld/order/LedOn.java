package com.nodrex.connectedworld.order;

import android.app.Activity;
import android.support.v7.widget.SwitchCompat;
import android.view.View;

import com.nodrex.android.tools.UiUtil;
import com.nodrex.android.tools.Util;
import com.nodrex.connectedworld.helper.Helper;
import com.nodrex.generic.server.protocol.Param;
import com.nodrex.generic.server.protocol.Protocol;

public class LedOn extends Param {

    public static final String Error = "IP should not be null or empty";
    public static final String DATA = "d=8 ";

    private Activity activity;
    private String ip;
    private View progressBar;
    private boolean failed;
    private SwitchCompat switchCompat;

    public LedOn(Activity activity,String ip, View progressBar, SwitchCompat switchCompat){
        //super(Protocol.LED_ON);
        Helper.checkStrContent(ip);
        this.activity = activity;
        this.ip = ip;
        this.progressBar = progressBar;
        this.switchCompat = switchCompat;
        start();
    }

    public String getIp(){
        return ip;
    }

    public boolean isFailed() {
        return failed;
    }

    public void setFailed(boolean failed) {
        this.failed = failed;
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
        Util.toast(activity,"Unfortunately can not turn lig on");
    }

}
