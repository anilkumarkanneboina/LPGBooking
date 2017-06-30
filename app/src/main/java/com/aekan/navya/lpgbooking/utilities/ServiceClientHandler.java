package com.aekan.navya.lpgbooking.utilities;

import android.database.Cursor;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

/**
 * Created by aruramam on 3/29/2017.
 * Handler, which handles messages with appropriate calls
 * to a ServiceClientAPIInterface instance.
 * This Handler object is the actual worker which does the work of getting Cylinder record for a row id , or
 * getting the incremented Primary key.
 * Thus, this worker basically needs a helper object which would basically have two methods - get cylinder, get next incremented primary key.
 * The helper class is actually designed using a interface - ServiceClientAPIInterface.
 *
 * This worker thread will provide two reply messsages, each corresponding to the activity it performs. Hence the handler which handles this
 * response should be equipped with two methods which would handle the messages appropriately.
 *
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
            break;
        case (LPG_Utility.MSG_INCREMENTPRIMARYKEY):
            //get incremented primary key value
            String incrementedPrimaryKey = mServiceClient.getIncrementedPrimaryKey();

            //Create an appropriate message
            Message response = Message.obtain(null,LPG_Utility.MSG_INCREMENTPRIMARYKEY,incrementedPrimaryKey);
            try {
                message.replyTo.send(response);
            } catch (RemoteException re){
                Log.v("Exception ", "Messenger offline");
            }
            //close the looper thread
            this.getLooper().quit();
            break;
        case (LPG_Utility.MSG_POPULATECONNECTION):
            //get all connection details in a cursor
            Cursor allConnectionDetails = mServiceClient.getAllConnectionDetails();

            Message allConnectionMessage = Message.obtain(null, LPG_Utility.MSG_POPULATECONNECTION, allConnectionDetails);
            try {
                message.replyTo.send(allConnectionMessage);
            } catch (Exception e) {
                Log.v("Messenger", "Messenger Offline");
            }
            //get the looper out
            this.getLooper().quit();

        default:
            this.getLooper().quit();
            break;
    }

    }
}
