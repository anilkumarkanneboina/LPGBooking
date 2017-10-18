package com.aekan.navya.lpgbooking;

import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.aekan.navya.lpgbooking.utilities.LPG_SQLOpenHelperClass;
import com.aekan.navya.lpgbooking.utilities.LPG_SQL_ContractClass;
import com.aekan.navya.lpgbooking.utilities.LPG_Utility;

import java.util.Calendar;

/**
 * Created by arunramamurthy on 15/08/16.
 * Reboot Alarm set up
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
        SQLiteDatabase sqLiteDatabase = (new LPG_SQLOpenHelperClass(context)).getWritableDatabase();

        //Query the database with all the information that is needed
        String[] columnnames = {
                LPG_SQL_ContractClass.LPG_CONNECTION_ROW.CONNECTION_NAME,
                LPG_SQL_ContractClass.LPG_CONNECTION_ROW.LAST_BOOKED_DATE,
                LPG_SQL_ContractClass.LPG_CONNECTION_ROW.CONNECTION_EXPIRY_DAYS,
                LPG_SQL_ContractClass.LPG_CONNECTION_ROW._ID
        };
        Cursor cursor = sqLiteDatabase.query(LPG_SQL_ContractClass.LPG_CONNECTION_ROW.TABLE_NAME,columnnames,null,null,null,null,null);
        //iterate through the cursor to set alarm for every connection made.
        //check if the cursor has any rows before iterating through the cursor
        if (cursor.getCount() > 0){
            cursor.moveToFirst();
            int cursorRecordSetCount = cursor.getCount();
            for ( int i = 0;i<cursorRecordSetCount;++i){
                String cursorDateFields = cursor.getString(cursor.getColumnIndex(LPG_SQL_ContractClass.LPG_CONNECTION_ROW.LAST_BOOKED_DATE));
                String cylinderExpiryDays = (cursor.getString(cursor.getColumnIndex(LPG_SQL_ContractClass.LPG_CONNECTION_ROW.CONNECTION_EXPIRY_DAYS)));
                String cursorConnectionName = cursor.getString(cursor.getColumnIndex(LPG_SQL_ContractClass.LPG_CONNECTION_ROW.CONNECTION_NAME));
                String LPG_Row_ID = cursor.getString(cursor.getColumnIndex(LPG_SQL_ContractClass.LPG_CONNECTION_ROW._ID));
                Calendar systemDate = Calendar.getInstance();


                LPG_Utility.RefillAlarmNotification[] alarmNotification = LPG_Utility.getRefillRemainder(context, cursorDateFields, cylinderExpiryDays, LPG_Row_ID, cursorConnectionName, LPG_Utility.LPG_GET_REGULAR_ALARM_NOTIFICATION_DATES);
                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

                //set alarms
                for (int counter = 0; counter < alarmNotification.length; ++counter) {
                    alarmManager.set(AlarmManager.RTC_WAKEUP, alarmNotification[counter].getGregorialCalendar().getTimeInMillis(), alarmNotification[counter].getRefillCylinder());
                }

                }



            }
        cursor.close();

        }


}

