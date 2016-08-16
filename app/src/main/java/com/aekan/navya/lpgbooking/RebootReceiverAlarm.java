package com.aekan.navya.lpgbooking;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by arunramamurthy on 15/08/16.
 */
public class RebootReceiverAlarm extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        /*Use this receiver to create alarms on system reboot
        On receiving this intent, query the database and
       get all saved cylinder information. With this information
       create the necessary alarm by checking the business logic
       as given in AddLPGConnection*/

        //Get the database instance
        SQLiteDatabase sqLiteDatabase = ((LPGApplication) context.getApplicationContext()).LPGDB;

        //Query the database with all the information that is needed


    }
}
