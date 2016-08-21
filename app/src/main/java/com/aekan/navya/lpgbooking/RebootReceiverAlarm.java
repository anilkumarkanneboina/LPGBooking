package com.aekan.navya.lpgbooking;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.security.AllPermission;
import java.util.Calendar;
import java.util.GregorianCalendar;

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
        SQLiteDatabase sqLiteDatabase = (new LPG_SQLOpenHelperClass(context)).getWritableDatabase();

        //Query the database with all the information that is needed
       String[] columnnames = {LPG_SQL_ContractClass.LPG_CONNECTION_ROW.CONNECTION_NAME, LPG_SQL_ContractClass.LPG_CONNECTION_ROW.LAST_BOOKED_DATE, LPG_SQL_ContractClass.LPG_CONNECTION_ROW.CONNECTION_EXPIRY_DAYS};
        Cursor cursor = sqLiteDatabase.query(LPG_SQL_ContractClass.LPG_CONNECTION_ROW.TABLE_NAME,columnnames,null,null,null,null,null);
        //iterate through the cursor to set alarm for every connection made.
        //check if the cursor has any rows before iterating through the cursor
        if (cursor.getCount() > 0){
            cursor.moveToFirst();
            int cursorRecordSetCount = cursor.getCount();
            for ( int i = 0;i<cursorRecordSetCount;++i){
                String[] cursorDateFields = cursor.getString(cursor.getColumnIndex(LPG_SQL_ContractClass.LPG_CONNECTION_ROW.LAST_BOOKED_DATE)).split("/");
                int cylinderExpiryDays = Integer.parseInt( cursor.getString(cursor.getColumnIndex(LPG_SQL_ContractClass.LPG_CONNECTION_ROW.CONNECTION_EXPIRY_DAYS)));
                String cursorConnectionName = cursor.getString(cursor.getColumnIndex(LPG_SQL_ContractClass.LPG_CONNECTION_ROW.CONNECTION_NAME));
                int lastBookedDateField = Integer.parseInt( cursorDateFields[1]);
                int lastBookedYearField = Integer.parseInt(  cursorDateFields[2]);
                int   lastBookedMonthField = Integer.parseInt( cursorDateFields[0]);
                GregorianCalendar lastBookedDateInCursor = new GregorianCalendar(lastBookedYearField,lastBookedMonthField - 1,lastBookedDateField,0,0,0);
                Calendar systemDate = Calendar.getInstance();
              //  int cylinderExpiryDays = Integer.parseInt( cursor.getString(cursor.getColumnIndex(LPG_SQL_ContractClass.LPG_CONNECTION_ROW.CONNECTION_EXPIRY_DAYS)));
                GregorianCalendar midExpiryDate = lastBookedDateInCursor;
                midExpiryDate.add(Calendar.DAY_OF_MONTH,Math.round( cylinderExpiryDays/2) );

                if ( systemDate.compareTo(lastBookedDateInCursor) <= 0  ) {
                    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    Intent intentCursorAlarm = new Intent(context,LPG_AlarmReceiver.class);
                    intentCursorAlarm.putExtra("NotificationTitle","Cylinder is half done");
                    intentCursorAlarm.putExtra("NotificationContent", cursorConnectionName + " is half empty now. Please click on Book icon to book the cylinder now!!");
                    PendingIntent cursorPendingIntent = PendingIntent.getBroadcast(context,0,intentCursorAlarm,0);

                    alarmManager.set(AlarmManager.ELAPSED_REALTIME,midExpiryDate.getTimeInMillis(),cursorPendingIntent);
                    cursor.moveToNext();


                }



            }

        }

    // Close the DB and cursor
        sqLiteDatabase.close();
        cursor.close();
    }
}
