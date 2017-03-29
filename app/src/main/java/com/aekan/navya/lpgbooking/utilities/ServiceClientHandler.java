package com.aekan.navya.lpgbooking.utilities;

import android.app.Service;
import android.database.Cursor;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

/**
 * Created by aruramam on 3/29/2017.
 */

public class ServiceClientHandler extends Handler {
    //api class
    private ServiceClientAPIInterface mServiceClient;

    //constructor for handler
    public ServiceClientHandler(Looper looper, ServiceClientAPIInterface serviceClientAPIInterface ){
        super(looper);
        mServiceClient = serviceClientAPIInterface;
    }

    //override message handler
    @Override
    public void handleMessage(Message message){
//        call appropriate service based on message
    switch (message.what){
        case (LPG_Utility.MSG_GETALLCYLINDERS):
            //get row id from string
            String rowId = (String) message.obj;

            //call appropriate method in service api
            Cursor c = mServiceClient.getCylinderRecordForID(rowId);

            //create another message to be sent to calling handler
            Message replyMessage = Message.obtain();
            replyMessage.what = LPG_Utility.MSG_GETALLCYLINDERS;
            replyMessage.obj = c;
            try {
                message.replyTo.send(replyMessage);
            } catch (RemoteException R){
                Log.v("Exception ", "Messenger offline");
            }

            //close the looper
            this.getLooper().quit();

    }

    }
}
