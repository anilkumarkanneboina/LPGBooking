package com.aekan.navya.lpgbooking;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ButtonBarLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.aekan.navya.lpgbooking.utilities.LPG_SQL_ContractClass;
import com.aekan.navya.lpgbooking.utilities.LPG_Utility;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by aruramam on 1/8/2017.
 */

public class ConfirmLPGBookingCompletion extends AppCompatActivity {

    /*Create the activity which would request confirmation from
    user about LPG Booking attempt - the activity will reset the corresponding
    LPG Cylinder's last booked date to current date, and create alarma.
    If the attempt was a failure, the app would redirect the user to try boooking one more time. */
    @Override
    protected void onCreate(Bundle savedInstanceState){
        // Logic to be completed during Activity creation

        //Call onCreate from super class
        super.onCreate(savedInstanceState);

        // Define the local variables for onCreate method
        //get connection id for the cylinder
        final String LPG_CONNECTION_ID = getIntent().getStringExtra(LPG_Utility.LPG_CONNECTION_ID) ;
        final String LPGCONNECTIONEXPIRYDAYS = "FAILURE TO FETCH CONNECTION EXPIRY DAYS FROM DB";
        final AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

        //set content for the activity
        setContentView(R.layout.confirm_lpg_submission);

        //set on click listeners for Yes and no buttons
        //get the button controls
        Button buttonConfirmLPGBookingYes = (Button) findViewById(R.id.confirmbooking_yes);
        Button buttonConfirmLPGBookingNo = (Button) findViewById(R.id.confirmbooking_no);

        /*set on-click listener for the yes buttonConfirmLPGBookingYes
        Now this has many parts to it.
        We need to update the Database with the last booked date as current date
        have started tracking as per new booking.
        When this is all done, we will give a notification to user that we
        And , set alarms for notification for new expired expiry date*/
        buttonConfirmLPGBookingYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Set the last booked date for the cylinder to be current date

                //Get current date
                GregorianCalendar currentCalendar = (GregorianCalendar) Calendar.getInstance();
                int currentDate = currentCalendar.get(Calendar.DAY_OF_MONTH);
                int currentMonth = currentCalendar.get(Calendar.MONTH);
                int currentYear = currentCalendar.get(Calendar.YEAR);
                String currentDateString = Integer.toString(currentDate) + Integer.toString(currentMonth + 1) + Integer.toString(currentYear);

                //Update the current date string in database as the last booked date
                // for the cyliner in quesion
                SQLiteDatabase db = ((LPGApplication) getApplication()).LPGDB ;
                ContentValues updateFieldsList = new ContentValues();
                updateFieldsList.put(LPG_SQL_ContractClass.LPG_CONNECTION_ROW.LAST_BOOKED_DATE,currentDateString);
                String whereClause = LPG_SQL_ContractClass.LPG_CONNECTION_ROW.CONNECTION_ID + " LIKE ?";
                String[] whereClauseFilter = { LPG_CONNECTION_ID };
                try {
                    db.update(LPG_SQL_ContractClass.LPG_CONNECTION_ROW.TABLE_NAME,
                            updateFieldsList,
                            whereClause,
                            whereClauseFilter
                    );
                } catch (Exception e){
                    //DB update has failed. Print the exception to log, and give an error message to user
                    ((LPGApplication) getApplication()).LPG_Alert.showDialogHelper(getResources().getString(R.string.confirmbooking_updatefailure), "Ok", null, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    },null).show(getSupportFragmentManager(),"Failure to Update DB");
                    Log.v("Update failure in DB",e.toString());

                }


                //Set the alarms for the new last booked date.

                //get expected expiry days for the connection

                String[] fieldListExpectedExpiryDate = {LPG_SQL_ContractClass.LPG_CONNECTION_ROW.CONNECTION_EXPIRY_DAYS};
                String ConnectionExpiryDays = new String();
                try {

                    Cursor c = db.query(LPG_SQL_ContractClass.LPG_CONNECTION_ROW.TABLE_NAME,
                            fieldListExpectedExpiryDate,
                            whereClause,
                            whereClauseFilter,
                            null,
                            null,
                            LPG_SQL_ContractClass.LPG_CONNECTION_ROW.CONNECTION_EXPIRY_DAYS + "DESC");

                    if (c.moveToFirst()){
                        ConnectionExpiryDays = c.getString(0);
                    }
                    c.close();


                } catch (Exception e){
                    ConnectionExpiryDays = LPGCONNECTIONEXPIRYDAYS;

                    ((LPGApplication) getApplication()).LPG_Alert.showDialogHelper(getResources().getString(R.string.confirmbooking_cursorfetchfailure),
                            "Ok",
                            null,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            },
                            null
                    ).show(getSupportFragmentManager(),"Failure to get Connection Expiry Days");
                }

                //Create an Alarm with updated Connection Expiry String
                //create the intent
                Intent intentAlarmForSuccessfulBooking  = new Intent(getApplicationContext(),LPGBooking.class);
                intentAlarmForSuccessfulBooking.putExtra(LPG_Utility.LPG_ALARMINTENT_NOTIFICATIONTITLE,getResources().getString(R.string.confirmbooking_alarm_notificationtitle_yes));
                intentAlarmForSuccessfulBooking.putExtra(LPG_Utility.LPG_ALARMINTENT_NOTIFICATIONID,LPG_CONNECTION_ID);
                intentAlarmForSuccessfulBooking.putExtra(LPG_Utility.LPG_ALARMINTENT_NOTIFICATIONCONTENT, LPG_CONNECTION_ID + getResources().getString(R.string.Notification_MidWayAlarm) );

                Calendar midWayAlarm = Calendar.getInstance();
                int intConnectionExpiryDays = Integer.parseInt(ConnectionExpiryDays);
                int pendingIntentRequestCode = Integer.parseInt(LPG_CONNECTION_ID) * 10 + 1;

                midWayAlarm.add(Calendar.DAY_OF_MONTH,Math.round(intConnectionExpiryDays));
                midWayAlarm.set(Calendar.HOUR_OF_DAY,12);
                midWayAlarm.set(Calendar.MINUTE,1);

                PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(getApplicationContext(),pendingIntentRequestCode,intentAlarmForSuccessfulBooking,PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.set(AlarmManager.RTC,midWayAlarm.getTimeInMillis(),alarmPendingIntent);

                //close db
                db.close();
            }
        });

    }



}
