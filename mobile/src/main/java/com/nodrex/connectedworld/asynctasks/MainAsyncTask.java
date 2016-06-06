package com.nodrex.connectedworld.asynctasks;

import android.os.AsyncTask;

import com.nodrex.connectedworld.helper.Constants;
import com.nodrex.connectedworld.protocol.AsyncTaskParam;
import com.nodrex.connectedworld.protocol.Protocol;

public class MainAsyncTask extends AsyncTask<AsyncTaskParam,Void,Void> {

    private AsyncTaskParam asyncTaskParam;
    private int protocol;

    @Override
    protected Void doInBackground(AsyncTaskParam... params) {
        if(params == null || params.length < 1)return null;
        asyncTaskParam = params[0];
        protocol = asyncTaskParam.getProtocol();

        switch(protocol){
            case Protocol.LED_ON: break;
            case Protocol.LED_OFF: break;
            //some more protocols
            default: //do something
        }

        return null;
    }

    public static void ping(AsyncTaskParam asyncTaskParam){
        try{
            for(int i = 0; i< Constants.MAIN_ASYNC_TASK_TRY_COUNTER ; i++){
                MainAsyncTask mainAsyncTask = new MainAsyncTask();
                mainAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,asyncTaskParam);
                break;
            }
        }catch (Exception e){
            //TODO handle exception and provide apropriately massage if fails after several try
        }
    }

}
