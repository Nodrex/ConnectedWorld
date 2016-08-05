package com.nodrex.connectedworld.communication;

import android.os.AsyncTask;

import com.nodrex.android.tools.Util;
import com.nodrex.connectedworld.helper.Constants;
import com.nodrex.connectedworld.order.LedOff;
import com.nodrex.connectedworld.order.LedOn;
import com.nodrex.generic.server.protocol.Param;
import com.nodrex.generic.server.protocol.Protocol;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;

public class WifiC extends AsyncTask<Param,Param,Param> {

    public static final int MAIN_ASYNC_TASK_TRY_COUNTER = 3;
    public static final int CONNECTION_TIME_OUT = 10000;
    public static final int CONNECTION_TIME_OUT_ITERATION = 50000;
    public static final int SLEEP_INTERVAL = 1000;

    private Param param;
    private Protocol protocol;

    @Override
    protected Param doInBackground(Param... params) {
        if(params == null || params.length < 1)return null;
        param = params[0];
        protocol = param.getProtocol();

        switch(protocol){
            case LED_ON:
                ledOn();
                break;
            case LED_OFF:
                ledOff();
                break;
            //some more protocols
            default: //do something
        }

        return param;
    }

    private boolean ledOn(){
        Util.log("Trying led on");
        LedOn ledOn = (LedOn) param;
        String ip =  ledOn.getIp();
        //String answer = sendDataToESP(value);
        String answer = sendDataToESPSocket(ip,LedOn.DATA);
        Util.log(answer);
        /*if("-1\n".equals(answer) || "-1".equals(answer)){
            Util.log("recall of ledOn");
            try {
                Thread.sleep(SLEEP_INTERVAL);
            } catch (InterruptedException e) {
                for(int j=0; j<CONNECTION_TIME_OUT_ITERATION; j++);//Just trying to spend time.
            }
            Util.log("ping ledOn again");
            return false;
        }*/
        return true;
    }

    private boolean ledOff(){
        Util.log("Trying led off");
        LedOff ledOff = (LedOff) param;
        String ip =  ledOff.getIp();
        //String answer = sendDataToESP(value);
        String answer = sendDataToESPSocket(ip,LedOff.DATA);
        Util.log(answer);
        /*if("-1\n".equals(answer) || "-1".equals(answer)){
            Util.log("recall of ledOff");
            try {
                Thread.sleep(SLEEP_INTERVAL);
            } catch (InterruptedException e) {
                for(int j=0; j<CONNECTION_TIME_OUT_ITERATION; j++);//Just trying to spend time.
            }
            Util.log("ping ledOff again");
            return false;
        }*/
        return true;
    }

    public String sendDataToESPSocket(String ip,String data) {
        Util.log("Trying to send data socket: " + ip);

        Socket socket = new Socket();
        try {
            InetSocketAddress socketAddr = new InetSocketAddress(ip, Constants.PORT);
            socket.connect(socketAddr, CONNECTION_TIME_OUT);//TODO porti shevcvali: gavzardo 2000 is zevit
        } catch (IOException e) {
            Util.log("problem when connecting with socket: " + e.toString());
        }

        try {
            DataOutputStream DataOut = new DataOutputStream(socket.getOutputStream());
            Util.log("Sending data: " + data);
            DataOut.writeBytes(data);
            DataOut.flush();
        }catch (Exception e){
            Util.log("problem in sendDataToESPSocket: " + e.toString());
        }

/*
        try {
            DataInputStream datain = new DataInputStream(socket.getInputStream());
            Util.log("Reading data: " + data);
            String result = datain.readUTF();
            Util.log("Result: " + result);
        }catch (Exception e){
            Util.log("problem in sendDataToESPSocket: " + e.toString());
        }
*/

        try {
            Util.log("closing socket");
            socket.close();
            Util.log("socket is closed");
        } catch (IOException e) {
            Util.log("problem when closing oscket: " + e.toString());
        }

        try {
            Util.log("Thread sleep");
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            for (int i=0; i<500000; i++);
        }
        Util.log("Thread is up");

        return "S";
    }

    public String sendDataToESP(String ipPortAndData) {
        Util.log("Trying to send data: " + ipPortAndData);
        //String text = null;
        BufferedReader reader = null;
        InputStreamReader inputStreamReader = null;
        HttpURLConnection conn = null;
        URL url = null;
        try {
            url = new URL(ipPortAndData);
            conn = (HttpURLConnection) url.openConnection();
            //conn.setDoInput(true);
            conn.setConnectTimeout(CONNECTION_TIME_OUT);
            //conn.setReadTimeout(CONNECTION_TIME_OUT);//TODO
            //Util.log("Disable read timeOut");
            Util.log("Connection was established");
            String line = null;
            //StringBuilder sb = new StringBuilder();
            //try {
            //Util.log("inputStreamReader");
            inputStreamReader = new InputStreamReader(conn.getInputStream());
            //Util.log("BufferedReader");
            reader = new BufferedReader(inputStreamReader);
            //Util.log("already read...");
            /*} catch (Exception e) {
                Util.log("Problem in main read: " + e.toString());
            }*/
            /*while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
                Util.log("in while______");
            }
            text = sb.toString();*/
        }catch (NullPointerException npe){
            /*try{
                Thread.sleep(CONNECTION_TIME_OUT);
            }catch (Exception e){
                for(int j=0; j<CONNECTION_TIME_OUT_ITERATION; j++);//Just trying to spend time.
            }*/
            return "d";
        }catch (Exception e) {
            Util.log("problem in sendDataToESP: " + e.toString());
            return "d";
            //return "-1\n";
        } finally {
            /*try {
                Util.log("closing reader");
                reader.close();
                Util.log("reader closed");
            } catch (Exception ex) {
                Util.log("problem in closing reader: " + ex.toString());
            }
            try {
                Util.log("closing inputStreamReader");
                inputStreamReader.close();
                Util.log("inputStreamReader closed");
            } catch (Exception ex) {
                Util.log("problem in closing inputStreamReader: " + ex.toString());
            }*/
            try {
                Util.log("closing connection");
                conn.disconnect();
                Util.log("connection closed");
            } catch (Exception ex) {
                Util.log("problem in closing connections: " + ex.toString());
            }
        }
        return "d";
    }

    @Override
    protected void onPostExecute(Param result) {
        super.onPostExecute(result);
        if(result == null) return;
        protocol = result.getProtocol();
        switch(protocol){
            case LED_ON:
                LedOn ledOn = (LedOn) result;
                if (ledOn.isFailed()) ledOn.failed();
                else ledOn.done();
                break;
            case LED_OFF:
                LedOff ledoff = (LedOff) result;
                if(ledoff.isFailed()) ledoff.failed();
                else ledoff.done();
                break;
            //some more protocols
            default: //do something
        }
    }

    public static void ping(Param asyncTaskParam){
        for(int i = 0; i< MAIN_ASYNC_TASK_TRY_COUNTER ; i++){
            try{
                WifiC mainAsyncTask = new WifiC();
                mainAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,asyncTaskParam);
                break;
            }catch (Exception e){
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ie) {
                    for(int j=0; j<CONNECTION_TIME_OUT_ITERATION; j++);//Just trying to spend time.
                }
            }
        }
    }

}
