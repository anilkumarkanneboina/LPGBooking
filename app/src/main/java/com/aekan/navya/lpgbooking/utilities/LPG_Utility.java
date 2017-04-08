package com.aekan.navya.lpgbooking.utilities;

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

    public final static String PERMISSION_INTIMATION_MESSAGE = "This string identifies the message to be pasted in permission check activity, to display SMS initimation message";
    public final static String PERMISSION_SMS_INTIMATION = "SHOW SMS MESSAGE";
    public final static String PERMISSION_CALL_INTIMATION = "SHOW CALL MESSAGE";
    public final static String PERMISSION_STATUS = "This string defines permission status";
    public final static String PERMISSION_DENIED = "PERMISSION DENIED";
    public final static String PERMISSION_ACCEPTED = "PERMISSION PROVIDED";

    public final static int HASH_CAPACITY = 4;
    public final static float HASH_LOAD_FACTOR = 0.8f;

    public final static int MSG_GETALLCYLINDERS = 0;
    public final static int MSG_INCREMENTPRIMARYKEY = 1243;
}
