package com.aekan.navya.lpgbooking.utilities;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.aekan.navya.lpgbooking.LPG_AlarmReceiver;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by aruramam on 1/9/2017.
 */

public class LPG_Utility {
    //Define constants to be used in the project
    public final static String LPG_CONNECTION_ID="This is used to transfer connectionid across activities";
    public final static String LPG_ALARMINTENT_NOTIFICATIONTITLE="NotificationTitle";
    public final static String LPG_ALARMINTENT_NOTIFICATIONID="NotificationID";
    public final static String LPG_ALARMINTENT_NOTIFICATIONCONTENT = "Notification content";
    /*Constants to be defined for usage in permission checks*/
    public final static int PERMISSION_SMS_ALLOWED = 0;
    public final static int PERMISSION_SMS_NOT_ALLOWED = 1;
    public final static int PERMISSION_CALL_ALLOWED = 2;
    public final static int PERMISSION_CALL_NOT_ALLOWED = 3;
    //constants for lpg booking activity
    public final static String PERMISSION_INTIMATION_MESSAGE = "This string identifies the message to be pasted in permission check activity, to display SMS initimation message";
    public final static String PERMISSION_SMS_INTIMATION = "SHOW SMS MESSAGE";
    public final static String PERMISSION_CALL_INTIMATION = "SHOW CALL MESSAGE";
    public final static String PERMISSION_STATUS = "This string defines permission status";
    public final static String PERMISSION_DENIED = "PERMISSION DENIED";
    public final static String PERMISSION_ACCEPTED = "PERMISSION PROVIDED";
    public final static String LPG_PROVIDER_INDANE = "INDANE";
    public final static String LPG_PROVIDER_BHARATH = "BHARATH GAS";
    public final static String LPG_PROVIDER_HP = "HP GAS";
    public final static String LPG_REFILL_TEXT_BHARATH = "LPG";
    public final static String LPG_REFILL_TEXT_INDANE = "IOC";
    public final static String LPG_REFILL_TEXT_HP = "HPGAS";
    public final static String[] LPG_PROVIDERS = {LPG_PROVIDER_INDANE, LPG_PROVIDER_BHARATH, LPG_PROVIDER_HP};
    public final static String[] LPG_PROVIDERS_EXTENDEDLIST = {"Indane", "Indian Oil Corp", "IOC", "Hindustan Petroleum", "HPCL", "HP Gas", "Bharath Gas"};
    public final static int LPG_PROVIDER_NOT_FOUND = 100;
    public final static int SENT_REFILL_SMS = 12;
    public final static int DELIVERED_REFILL_SMS = 14;
    public final static String SMS_DELIVERY_MILESTONE = "This will represent present state of SMS Delivery";
    public final static String PROGRESS_SMS_SENT = "SMS Sent...";
    public final static String PROGRESS_SMS_DELIVERED = "SMS Delivered ...";
    public final static String PROGRESS_START = "Sending SMS....";

    public final static int HASH_CAPACITY = 4;
    public final static float HASH_LOAD_FACTOR = 0.8f;

    public final static int MSG_GETALLCYLINDERS = 0;
    public final static int MSG_INCREMENTPRIMARYKEY = 1243;

    public final static int FINAL_NOTIFICATION_BUFFER = 8;



    public static int getIndexOfProvider(String Provider) {
        for (int i = 0; i < LPG_PROVIDERS.length; ++i) {
            if (LPG_PROVIDERS[i].equals(Provider)) {
                return i;
            }

        }
        return LPG_PROVIDER_NOT_FOUND;
    }

    public static String getLPGProvider(String input) {
        if (input.equals("Indane") || input.equals("Indian Oil Corp") || input.equals("IOC")) {
            return LPG_PROVIDER_INDANE;
        } else {
            if (input.equals("Hindustan Petroleum") || input.equals("HPCL") || input.equals("HP Gas")) {
                return LPG_PROVIDER_HP;
            } else {
                if (input.equals("Bharath Gas")) {
                    return LPG_PROVIDER_BHARATH;
                }

            }   //"Hindustan Petroleum","HPCL","HP Gas"
        }
        return input;
    }

    public static RefillAlarmNotification[] getRefillRemainder(Context applicationContext, String lastBookedDate, String expiryDaysStr, String rowID, String connectionName){
        RefillAlarmNotification[] alarmTimes = new RefillAlarmNotification[2];

        String[] strDateFields = lastBookedDate.split("/");
        int lpglastbookeddate, lpglastbookedmonth, lpglastbookedyear;
        Calendar sysDate = Calendar.getInstance();
        Date lpgLastBooked;
        GregorianCalendar lpgLastBookedGregCalendar;
        int expiryDays = Integer.parseInt(expiryDaysStr);


        if (strDateFields.length != 0 ) {

            // From the split string, get the integer values for date, month and year fields
            // and create a date object with these values Integer.getInteger
            lpglastbookeddate = Integer.parseInt(strDateFields[1]);
            lpglastbookedmonth = Integer.parseInt(strDateFields[0]);
            lpglastbookedyear = Integer.parseInt(strDateFields[2]);
            //Create the AlarmManager object to set alarms


            // Create the gregorian calendar based on the last booked date
            // add half of expected expiry time to this date, to arrive at mid-way expiry date
            lpgLastBookedGregCalendar = new GregorianCalendar(lpglastbookedyear, lpglastbookedmonth - 1, lpglastbookeddate);
            GregorianCalendar midwayExpiryDate = lpgLastBookedGregCalendar;
            midwayExpiryDate.add(Calendar.DATE, Math.round(expiryDays / 2));




            //compare this midway expiry time with sys date and
            //create an alarm only if this date is in future

            if (sysDate.compareTo(midwayExpiryDate) <= 0) {
                //set the alarm for this date with the notification class
                // Create an intent and use that to create the pending intent for alarm manager
                midwayExpiryDate = (GregorianCalendar) sysDate;
                midwayExpiryDate.add(Calendar.DAY_OF_MONTH,1);

            }

            midwayExpiryDate.set(Calendar.HOUR_OF_DAY, 12);
            midwayExpiryDate.set(Calendar.MINUTE, 1);

            //Test notification creation
            GregorianCalendar newNotification =  ((GregorianCalendar) Calendar.getInstance());
            newNotification.set(Calendar.MINUTE,55);
            newNotification.set(Calendar.HOUR_OF_DAY,19);

            Log.v("Notification1",newNotification.toString() );

            // Create a pending intent request code, which will refer to the alarm being set for this lpg connnection id
            // Utilising the same pending intent request code will help to replace the existing alarm, if there is a change in
            // lpg expiry days by any chance. In this application we explicitly do not check to reset the alarm
            // if the lpg expiry days has been changed. We use inherent behaviour of alarm manager to replace alarms if we use the same pending
            // intent, which in this case would be identified with pending intent request
            // Additionally, we will identify alarms used for mid-term expiry reminder with trailing numeral one to int request
            // This is evident in pendingIntentRequestCode variable initialization below

            int pendingIntentRequestCode = Integer.parseInt(rowID) * 10 + 1;
            Intent notificationIntent = new Intent(applicationContext,LPG_AlarmReceiver.class);
            notificationIntent.putExtra(LPG_Utility.LPG_ALARMINTENT_NOTIFICATIONTITLE,"Cylinder is half done");
            notificationIntent.putExtra(LPG_Utility.LPG_ALARMINTENT_NOTIFICATIONID,rowID);
            notificationIntent.putExtra(LPG_Utility.LPG_ALARMINTENT_NOTIFICATIONCONTENT, connectionName + " is half empty now. Please click on Book icon for refill booking!!");


            PendingIntent notificationPendingIntent = PendingIntent.getBroadcast(applicationContext, pendingIntentRequestCode, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

            //Midway alarm notification record - will be put in first index of Alarm Notification
            //test notification banner for now
            alarmTimes[0] = new RefillAlarmNotification(notificationPendingIntent,newNotification);
            //alarmTimes[0] = new RefillAlarmNotification(notificationPendingIntent,midwayExpiryDate);

            // Set another alarm before final expiry
            Intent notificationExpiryUltmate = new Intent(applicationContext,LPG_AlarmReceiver.class);
            int requestCodeNotificationUltimate = Integer.parseInt(rowID) * 10 + 2;
            notificationExpiryUltmate.putExtra(LPG_ALARMINTENT_NOTIFICATIONTITLE,"You need to refill now");
            notificationExpiryUltmate.putExtra(LPG_ALARMINTENT_NOTIFICATIONID,rowID);
            notificationExpiryUltmate.putExtra(LPG_ALARMINTENT_NOTIFICATIONCONTENT,connectionName + " is about to expire. Please click on Book to refill now!!");
            PendingIntent notificationFinal = PendingIntent.getBroadcast(applicationContext,requestCodeNotificationUltimate, notificationExpiryUltmate,PendingIntent.FLAG_CANCEL_CURRENT);
            // This notification would be set 8 days before final expiry date
            // this expiry
            GregorianCalendar calendarNotificationUltimate = new GregorianCalendar(lpglastbookedyear, lpglastbookedmonth - 1, lpglastbookeddate);
            calendarNotificationUltimate.add(Calendar.DAY_OF_MONTH, expiryDays - FINAL_NOTIFICATION_BUFFER);

            // If this alarm notification is in the past,
            // provide alartm notification the next day
            if ( sysDate.compareTo(calendarNotificationUltimate) <= 0  ){
                calendarNotificationUltimate = (GregorianCalendar) sysDate;
                calendarNotificationUltimate.add(Calendar.DAY_OF_MONTH,1);
            }

            calendarNotificationUltimate.set(Calendar.HOUR_OF_DAY,13);
            calendarNotificationUltimate.set(Calendar.MINUTE,3);

            //Test notification
            GregorianCalendar newnotification2 = (GregorianCalendar) Calendar.getInstance() ;
            newnotification2.add(Calendar.MINUTE,3);

            //test notificaiton firing for now
            alarmTimes[1] = new RefillAlarmNotification(notificationFinal,newnotification2);
            //alarmTimes[1] = new RefillAlarmNotification(notificationFinal,calendarNotificationUltimate);
            }
            return alarmTimes;
        }

    public static String getSMSTextMessage(String provider){
        String textMessage = "LPG";
        if ( provider.equals(LPG_PROVIDER_BHARATH)){textMessage = LPG_REFILL_TEXT_BHARATH;}
        if ( provider.equals(LPG_PROVIDER_INDANE)) {textMessage = LPG_REFILL_TEXT_INDANE;}
        if ( provider.equals(LPG_PROVIDER_HP)) {textMessage = LPG_REFILL_TEXT_HP;}
        return textMessage;

    }






    public static class RefillAlarmNotification{
        private PendingIntent RefillCylinder;
        private GregorianCalendar AlarmTickerTime;

        public RefillAlarmNotification(PendingIntent pendingIntent, GregorianCalendar alarmTickerTime){
            RefillCylinder = pendingIntent;
            AlarmTickerTime = alarmTickerTime;
        }

        public PendingIntent getRefillCylinder ()  { return RefillCylinder;}
        public GregorianCalendar getGregorialCalendar() { return AlarmTickerTime; }
    }



}

