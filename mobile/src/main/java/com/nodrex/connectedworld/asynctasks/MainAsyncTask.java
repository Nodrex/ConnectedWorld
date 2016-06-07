package com.nodrex.connectedworld.asynctasks;

import android.os.AsyncTask;

import com.nodrex.connectedworld.protocol.AsyncTaskParam;
import com.nodrex.connectedworld.protocol.LedOff;
import com.nodrex.connectedworld.protocol.LedOn;
import com.nodrex.connectedworld.protocol.Protocol;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainAsyncTask extends AsyncTask<AsyncTaskParam,Void,Void> {

    public static final int MAIN_ASYNC_TASK_TRY_COUNTER = 3;
    public static final int CONNECTION_TIME_OUT = 10000;

    private AsyncTaskParam asyncTaskParam;
    private int protocol;

    @Override
    protected Void doInBackground(AsyncTaskParam... params) {
        if(params == null || params.length < 1)return null;
        asyncTaskParam = params[0];
        protocol = asyncTaskParam.getProtocol();

        switch(protocol){
            case Protocol.LED_ON: return ledOn();
            case Protocol.LED_OFF: return ledOff();
            //some more protocols
            default: //do something
        }

        return null;
    }

    private Void ledOn(){
        LedOn ledOn = (LedOn) asyncTaskParam;
        String value =  ledOn.getValue();
        sendDataToESP(value);
        return null;
    }

    private Void ledOff(){
        LedOff ledOff = (LedOff) asyncTaskParam;
        String value =  ledOff.getValue();
        sendDataToESP(value);
        return null;
    }

    public static final String sendDataToESP(String ipPortAndData) {
        //String data = /*"/?" +*/ /*URLEncoder.encode("pin", "UTF-8") + "=" +*/ URLEncoder.encode("6", "UTF-8");
        String text = null;
        BufferedReader reader = null;
        try {
            //URL url = new URL("http://192.168.2.103:80"+ data);
            URL url = new URL(ipPortAndData);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setConnectTimeout(CONNECTION_TIME_OUT);

            String line = null;
            StringBuilder sb = new StringBuilder();
            int tryCounter = 3;
            for (int i = 0; i < tryCounter; i++) {
                try {
                    reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    break;
                } catch (Exception e) {}
            }
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            text = sb.toString();
        } catch (Exception ex) {
            //throw new Exception("GetStringFromUrl: " + ex.toString());
        } finally {
            try {
                reader.close();
            } catch (Exception ex) {}
        }
        return text;
    }

    public static void ping(AsyncTaskParam asyncTaskParam){
        for(int i = 0; i< MAIN_ASYNC_TASK_TRY_COUNTER ; i++){
            try{
                MainAsyncTask mainAsyncTask = new MainAsyncTask();
                mainAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,asyncTaskParam);
                break;
            }catch (Exception e){
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ie) {
                    for(int j=0; j<5000; j++);//Just trying to spend time.
                }
            }
        }
    }

}
