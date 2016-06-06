package com.nodrex.connectedworld.asynctasks;

import android.os.AsyncTask;

import com.nodrex.android.tools.Util;
import com.nodrex.connectedworld.helper.Constants;
import com.nodrex.connectedworld.protocol.AsyncTaskParam;
import com.nodrex.connectedworld.protocol.LedOn;
import com.nodrex.connectedworld.protocol.Protocol;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainAsyncTask extends AsyncTask<AsyncTaskParam,Void,Void> {

    public static final int MAIN_ASYNC_TASK_TRY_COUNTER = 2;

    private AsyncTaskParam asyncTaskParam;
    private int protocol;

    @Override
    protected Void doInBackground(AsyncTaskParam... params) {
        if(params == null || params.length < 1)return null;
        asyncTaskParam = params[0];
        protocol = asyncTaskParam.getProtocol();

        switch(protocol){
            case Protocol.LED_ON: return ledOn();
            case Protocol.LED_OFF: break;
            //some more protocols
            default: //do something
        }

        return null;
    }

    private Void ledOn(){
        LedOn ledOn = (LedOn) asyncTaskParam;
        String value =  ledOn.getValue();
        return null;
    }

    public static final String pingESP(String data) throws Exception {
        //String data = /*"/?" +*/ /*URLEncoder.encode("pin", "UTF-8") + "=" +*/ URLEncoder.encode("6", "UTF-8");
        //String data = "/?pin=4";
        Util.log("trying to send data: " + data);

        String text = null;
        BufferedReader reader = null;

        // Send data
        try {

            URL url = new URL("http://192.168.2.103:80"+ data);
            // Send POST data request 192.168.2.107

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);

            //conn.setChunkedStreamingMode(0);

            //conn.setConnectTimeout(timeOut);


            Util.log("data sent: " + data);

            String line = null;
            StringBuilder sb = new StringBuilder();
            // Get the server response
            int tryCounter = 1;
            for (int i = 0; i < tryCounter; i++) {
                try {
                    Util.log("GetStringFromUrl try: " + (i + 1));
                    reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    break;
                } catch (Exception e) {
                    Util.log("GetStringFromUrl -> Exception: " + e.toString());
                }
            }

            // Read Server Response
            while ((line = reader.readLine()) != null) {
                // Append server response in string
                sb.append(line + "\n");
            }

            text = sb.toString();
        } catch (Exception ex) {
            throw new Exception("GetStringFromUrl: " + ex.toString());
        } finally {
            try {
                reader.close();
            } catch (Exception ex) {}
        }

        return text;

    }

    public static void ping(AsyncTaskParam asyncTaskParam){
        try{
            for(int i = 0; i< MAIN_ASYNC_TASK_TRY_COUNTER ; i++){
                MainAsyncTask mainAsyncTask = new MainAsyncTask();
                mainAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,asyncTaskParam);
                break;
            }
        }catch (Exception e){
            //TODO handle exception and provide apropriately massage if fails after several try
        }
    }

}
