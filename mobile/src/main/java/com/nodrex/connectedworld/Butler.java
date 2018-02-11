package com.nodrex.connectedworld;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;

import com.nodrex.android.tools.Util;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Useful class for manage butler(Voice recognition) like Jarvis, home or smart home.
 * @author Nodar Tchumbadze
 * @since 2016
 * @version 1.0
 */
public class Butler {

    /**
     * Butler types
     */
    public interface Types {
        int Jarvis = 0;
        int Home = 1;
        int SmartHome = 2;
    }

    private static final String LIGHT_ON[] = new String[]{"Turn on light",
            "turn on light", "Turn on lights", "turn on lights",
            "Light", "light", "Lights", "lights", "Switch on light",
            "switch on light", "Switch on lights", "switch on lights",
            "Light on", "light on", "Lights on", "lights on", "Let there be light",
            "let there be light", "Let there be lights", "let there be lights",
            "Let it be light", "let it be light", "Let it be lights", "let it be lights",
            "Light please", "light please", "Lights please", "lights please", "turn on the light",
            "turn on the lights", "switch on the light", "switch on the lights", "the light please",
            "the lights please"};

    private static final String LIGHT_OFF[] = new String[]{"Turn off light",
            "turn off light", "Turn off lights", "turn off lights",
            "Light off", "light off", "Switch off light",
            "switch off light", "Switch off lights", "switch off lights",
            "Lights off", "lights off", "turn off the light",
            "turn off the lights", "switch off the light", "switch off the lights"};

    public static final String KEY = "com.nodrex.connectedworld.ButlerTypeKey";
    public static final int REQUEST_CODE = 1000;
    private static boolean fromLauncher;

    public static boolean isFromLauncher() {
        return fromLauncher;
    }

    /**
     * Starts speech recognizer.
     * @param activity
     * @param type
     */
    public static void start(Activity activity,int type){
        if(activity == null) return;
        fromLauncher = false;
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        String prompt = "Home assistant is ready";//"Jarvis is ready";
        switch (type){
            case Types.Home: prompt= "Home is ready"; break;
            case Types.SmartHome: prompt = "Smart home is ready"; break;
        }
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, prompt);

        try {
            activity.startActivityForResult(intent, REQUEST_CODE);
        } catch (ActivityNotFoundException a) {
            Util.toast(activity, "Sorry! Your device does not support speech input");
        } catch (Exception e){
            Util.toast(activity,"Can not start speech recognition, something went wrong");
        }
    }

    /**
     * Starts MainActivity from butler activity.
     * @param activity
     * @param butlerType
     */
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

    /**
     * Handles result, returned in onActivityResult.
     * @param activity
     * @param resultCode
     * @param data
     */
    public static void handleResult(Activity activity,int resultCode,Intent data){
        if (resultCode == Activity.RESULT_OK && data != null) {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if(result == null || result.size() <=0 ) return;
            String text  = result.get(0);
            convertToOrder(text);
        }
    }

    private static void convertToOrder(String text){
        Util.log(text);
        if(checkWord(LIGHT_ON,text)) {
            //ping(this, 1); //TODO
        }else if(checkWord(LIGHT_OFF,text)){
            //ping(this, -1); //TODO
        }else{
            //Util.toast(this,"es sityva ar vici, xelaxla tqvi: " + text); //TODO
            Util.log("Unknown order");
        }
    }

    private static boolean checkWord(String words[], String text){
        for(String keyWord : words){
            if(keyWord.equals(text)){
                return true;
            }
        }
        return false;
    }

}
