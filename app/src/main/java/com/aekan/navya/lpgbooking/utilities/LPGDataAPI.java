package com.aekan.navya.lpgbooking.utilities;

import android.app.Application;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.HandlerThread;

import com.aekan.navya.lpgbooking.LPGApplication;

import java.util.HashMap;
import android.os.Handler;

/**
 * Created by aruramam on 3/28/2017.
 */

class LPGDataAPIThread extends HandlerThread implements ServiceClientAPIInterface {

    //Instance variable to hold application context
    private LPGApplication mApplication;
    //Handler to process information
    private Handler mServiceClientHandler;

    //constructor to instantiate application
    public LPGDataAPIThread(LPGApplication application,String name){
        super(name);
        mApplication = application;
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
}
