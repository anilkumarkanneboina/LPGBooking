package com.aekan.navya.lpgbooking.utilities;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;

import com.aekan.navya.lpgbooking.LPGApplication;

import java.util.HashMap;

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
            mApplication.cacheLocalData.put(rowID, c);
//            c.close();

        }
        //mApplication.cacheLocalData.put(rowID,c);
        return hashCursor.get(rowID);


    }

    @Override
    public String getIncrementedPrimaryKey() {
        String connectionPrimaryKey;

        // set primary key value for connection id
        String[] connectionID = {LPG_SQL_ContractClass.LPG_CONNECTION_ROW._ID};
        Cursor cursorID = mApplication.LPGDB.query(LPG_SQL_ContractClass.LPG_CONNECTION_ROW.TABLE_NAME,connectionID,null,null,null,null,null);
        Log.v("AddConnnection ", "Cursor count before records creation " + Integer.toString(cursorID.getCount()));
        // Log.v("AddConnection Cursor "," getColumnIndex " + Integer.toString(cursorID.getColumnIndex(LPG_SQL_ContractClass.LPG_CONNECTION_ROW._ID)));
        //Log.v("AddConnection Cursor "," getString " + cursorID.getString(0));

        if (cursorID.getCount() == 0){
            Log.v("AddConnection ", "Initial cursor count");
            connectionPrimaryKey = "1";

        }else {
            //get max value for connection primary key
            try {
                int counterPrimaryKey = 1;
                cursorID.moveToFirst();
                Log.v("AddConnection ",Integer.toString(cursorID.getCount()));
                for (int i = 0; i < cursorID.getCount(); ++i) {
                    Log.v("AddConnection ","Inside cursor max iteration - counter value " + Integer.toString(i) + " Cursor length " + Integer.toString(cursorID.getCount())+ " Row id value " + cursorID.getString(cursorID.getColumnIndex(LPG_SQL_ContractClass.LPG_CONNECTION_ROW._ID)) );
                    int valueOfPrimaryKeyFromCounter = Integer.parseInt(cursorID.getString(cursorID.getColumnIndex(LPG_SQL_ContractClass.LPG_CONNECTION_ROW._ID)));
                    if (valueOfPrimaryKeyFromCounter > counterPrimaryKey) {
                        counterPrimaryKey = valueOfPrimaryKeyFromCounter;
                    }
                    cursorID.moveToNext();
                }
                connectionPrimaryKey = Integer.toString(counterPrimaryKey+1);
            }
            catch (android.database.CursorIndexOutOfBoundsException cursorIndex ){
                Log.v("AddConnection ", "Inside cursor bounds exception");
                connectionPrimaryKey = "1";
            }
        }
        //close and release the cursor
        cursorID.close();

        return connectionPrimaryKey;
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

    @Override
    public void updateCylinderIDCursor(Messenger messenger) {
        //send message to handler to update primary key value
        Message requestPrimaryKey = Message.obtain(null,LPG_Utility.MSG_INCREMENTPRIMARYKEY);
        requestPrimaryKey.replyTo = messenger;
        //ensure that Looper has been started before sending messages to ServiceClientHandler
        if (mServiceClientHandler == null){
            mServiceClientHandler = new ServiceClientHandler(getLooper(),this);
        }
        mServiceClientHandler.sendMessage(requestPrimaryKey);


    }
}
