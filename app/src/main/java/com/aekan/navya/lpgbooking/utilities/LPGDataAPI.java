package com.aekan.navya.lpgbooking.utilities;

import android.app.Application;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.HandlerThread;

import com.aekan.navya.lpgbooking.LPGApplication;

import java.util.HashMap;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;

/**
 * Created by aruramam on 3/28/2017.
 * This is an implementation for LPGServiceAPI interface,
 * which hides the handler thread mechanism within itself.
 *
 * This class makes  the implementation logic and wrapper interfaces connect,
 * and holds majority of functionality of service calls.
 *
 * This class  implements ServiceClientAPRInterface,
 * so that the handler can use an instance of this class, to call API
 * methods needed to service client requests.
 *
 * This class implements LPGServiceAPI, so that clients can make a simple
 * API method call for getting a specific service. The implementation logic for how
 * client  service calls  will be processed, is provided in method definitions.
 *
 *
 *
 *
 */

public class LPGDataAPI extends HandlerThread implements ServiceClientAPIInterface,LPGServiceAPI {

    //Instance variable to hold application context
    private LPGApplication mApplication;
    //Handler to process information
    private Handler mServiceClientHandler;

    //constructor to instantiate application
    public LPGDataAPI(LPGApplication application,String name){
        super(name);
        mApplication = application;
        //start the handler thread
        this.start();
    }



    //service call to get cursor for a specific row id
    public Cursor getCylinderRecordForID(String rowID){

        HashMap<String,Cursor> hashCursor = mApplication.cacheLocalData;
        SQLiteDatabase lpgDB = mApplication.LPGDB;
        Cursor c;

        if (!(hashCursor.containsKey(rowID))) {
            //local cache does not have details for this row id
            //update this cursor record in local cache HashMap
            //query from the database for all columns and put them in a cursor
            String selection = LPG_SQL_ContractClass.LPG_CONNECTION_ROW._ID + "= ?";
            String[] selectionArgs = { rowID };

            c=lpgDB.query(LPG_SQL_ContractClass.LPG_CONNECTION_ROW.TABLE_NAME,
                    null,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null);
            //update the value in HashMap - this acts as a local caching mechanism
            hashCursor.put(rowID,c);
            c.close();

        }

        return hashCursor.get(rowID);


    }

    @Override
    protected void onLooperPrepared(){
        //initiate handler
        mServiceClientHandler = new ServiceClientHandler(getLooper(),this);
    }

    @Override
    public void populateCylinderInfoThroughCursorWithRowID(String rowID, Messenger messenger) {
        //Create a message with row id and Messenger objects
        Message populateCylinderInfo = Message.obtain(null,LPG_Utility.MSG_GETALLCYLINDERS,rowID);
        populateCylinderInfo.replyTo = messenger;
        //ensure that Looper has been started before sending messages to ServiceClientHandler
        if (mServiceClientHandler == null){
            mServiceClientHandler = new ServiceClientHandler(getLooper(),this);
        }
        //send message to Handler
        mServiceClientHandler.sendMessage(populateCylinderInfo);


    }
}
