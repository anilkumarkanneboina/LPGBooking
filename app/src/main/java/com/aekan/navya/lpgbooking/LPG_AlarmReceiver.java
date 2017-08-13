package com.aekan.navya.lpgbooking;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.aekan.navya.lpgbooking.utilities.LPG_Utility;
import com.aekan.navya.lpgbooking.utilities.lpgconnectionparcel;

/**
 * Created by aruramam on 8/6/2016.
 */
public class LPG_AlarmReceiver extends BroadcastReceiver {

    public LPG_AlarmReceiver(){
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // Set a notification on receiving the broadcast
        Log.v("Alarm","On Receive");
        //Build the notification
        NotificationCompat.Builder lpgExpiryNotificationBuilder = new NotificationCompat.Builder(context);
        lpgExpiryNotificationBuilder.setSmallIcon(R.drawable.ic_feedback_black_24dp);
        lpgExpiryNotificationBuilder.setContentTitle(intent.getStringExtra(LPG_Utility.LPG_ALARMINTENT_NOTIFICATIONTITLE));
        lpgExpiryNotificationBuilder.setContentText(intent.getStringExtra(LPG_Utility.LPG_ALARMINTENT_NOTIFICATIONCONTENT));
        String lpgRowId = intent.getStringExtra(LPG_Utility.LPG_ALARMINTENT_NOTIFICATIONID);

        //Create an event to start booking activity
        Intent intentStartActivity = new Intent(context, LPGBooking.class);
        intentStartActivity.putExtra(lpgconnectionparcel.LPG_CONNECTIONRECORD_PARCEL, new lpgconnectionparcel(lpgRowId,true));

        //Create a stack builder and add the intent to the stack buildeer
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
        taskStackBuilder.addParentStack(MainActivity.class);
        taskStackBuilder.addNextIntent(intentStartActivity);
        PendingIntent pendingIntentStartActivity = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_CANCEL_CURRENT);

        //Add the pending intent to notification through builder object
        lpgExpiryNotificationBuilder.setContentIntent(pendingIntentStartActivity);
        lpgExpiryNotificationBuilder.addAction(R.drawable.ic_call_black_36dp_2, "Book", pendingIntentStartActivity);
        //lpgExpiryNotificationBuilder.setAutoCancel(true);

        Notification lpgExpiryNotification = lpgExpiryNotificationBuilder.build();
        lpgExpiryNotification.flags |= Notification.FLAG_AUTO_CANCEL;

        NotificationManager lpgNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        lpgNotificationManager.notify(Integer.parseInt(intent.getStringExtra(LPG_Utility.LPG_ALARMINTENT_NOTIFICATIONID)),lpgExpiryNotification);


    }

    /**
     * Created by arunramamurthy on 20/06/17.
     */


}
