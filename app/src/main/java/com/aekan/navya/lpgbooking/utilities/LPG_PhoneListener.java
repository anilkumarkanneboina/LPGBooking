package com.aekan.navya.lpgbooking.utilities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.RequiresPermission;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.aekan.navya.lpgbooking.ConfirmLPGBookingCompletion;
import com.aekan.navya.lpgbooking.MainActivity;

/**
 * Created by arunramamurthy on 15/01/17.
 * Phone Listener - will start activity to request booking confirmation from user
 * when the phone connection is disconnnected.
 */

public class LPG_PhoneListener extends PhoneStateListener {
    //class to create phone listener override
    //Set flag string for Phone state listener
    private static final String PHONELISTENERFLAGOFFHOOK = "PHONE OFF HOOK";
    private static final String PHONELISTENERFLAGONHOOK = "PHONE ON HOOK";

    //field for tracking phone listener
    private String setPhoneFlag;

    //fields for application context
    private Context applicationContext;
    private String LPGConnectionId;
    private String LPGConnectionName;

    //constructor to set application context
    public LPG_PhoneListener(Context context, String connectionid, String connectionName) {
        super();
        applicationContext = context;
        LPGConnectionId = connectionid;
        LPGConnectionName = connectionName;
        setPhoneFlag = PHONELISTENERFLAGONHOOK;
        Log.v("In PhoneListerenr", LPGConnectionId);

    }

    @Override
    @RequiresPermission(Manifest.permission.READ_PHONE_STATE)
    public void onCallStateChanged(int state, String incomingNumber) {

        //handle phone state
        switch (state) {
            case (TelephonyManager.CALL_STATE_IDLE):
                //start the next activity if the user had initiated the call
                //well, we safely believe that the call was to book the phone
                if (setPhoneFlag.equals(LPG_PhoneListener.PHONELISTENERFLAGOFFHOOK)) {
                    // change the flag;
                    setPhoneFlag = PHONELISTENERFLAGONHOOK;

                    //start the intent
                    if( LPGConnectionId.equals(LPG_Utility.PHONELISTENER_FROM_REGISTRATION)){
                        Intent intentHome = new Intent(applicationContext, MainActivity.class);
                        applicationContext.startActivity(intentHome);
                    }

                    else  {
                    Intent intentConfirmLPGBooking = new Intent(applicationContext, ConfirmLPGBookingCompletion.class);
                    intentConfirmLPGBooking.putExtra(LPG_Utility.LPG_CONNECTION_ID, LPGConnectionId);
                        intentConfirmLPGBooking.putExtra(LPG_Utility.LPG_CONNECTION_NAME, LPGConnectionName);
                    intentConfirmLPGBooking.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    applicationContext.startActivity(intentConfirmLPGBooking);
                    }

                }
                break;

            case (TelephonyManager.CALL_STATE_OFFHOOK):
                Log.v("Inside Phone Listener", "Phone off hook");
                //user has called the phone from within the application
                // time to set the flag that the user has attempted to book his cylinder
                setPhoneFlag = LPG_PhoneListener.PHONELISTENERFLAGOFFHOOK;
                break;

            default:
                break;
        }

    }


}
