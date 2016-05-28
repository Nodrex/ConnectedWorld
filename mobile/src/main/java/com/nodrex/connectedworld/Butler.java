package com.nodrex.connectedworld;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;

import com.nodrex.android.tools.Util;

import java.util.Locale;

/**
 * Useful class for manage butler like Jarvis, home or smart home. (Voice recognition)
 * @author Nodar Tchumbadze
 * @since 2016
 * @version 1.0
 */
public class Butler {

    public interface Types {
        int Jarvis = 0;
        int Home = 1;
        int SmartHome = 2;
    }

    public static final String KEY = "ButlerTypeKey";
    public static final int RESULT_CODE = 1000;
    private static boolean fromLauncher;

    public static boolean isFromLauncher() {
        return fromLauncher;
    }

   /* public static void setFromLauncher(boolean fromLauncher) {
        Butler.fromLauncher = fromLauncher;
    }*/

    public static void start(Activity activity,int type){
        if(activity == null) return;
        //setFromLauncher(false);

        fromLauncher = false;

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        String prompt = "Jarvis is ready";
        switch (type){
            case Types.Home: prompt= "Home is ready"; break;
            case Types.SmartHome: prompt = "Smart home is ready"; break;
        }
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, prompt);

        try {
            activity.startActivityForResult(intent, RESULT_CODE);
        } catch (ActivityNotFoundException a) {
            Util.toast(activity, "Sorry! Your device does not support speech input");
        } catch (Exception e){
            Util.toast(activity,"Can not start speech recognition, something went wrong");
        }
    }

    public static void startAppFromOkGoogle(Activity activity,int butlerType){
        if(activity == null) return;
        Intent intent = new Intent(activity, MainActivity.class);
        if(intent == null) return;
        Bundle bundle = new Bundle();
        if(bundle == null) return;
        bundle.putInt(KEY, butlerType);
        intent.putExtras(bundle);
        fromLauncher = true;
        activity.startActivity(intent);
        activity.finish();
    }

}
