package com.aekan.navya.lpgbooking;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Messenger;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneStateListener;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.aekan.navya.lpgbooking.utilities.LPGDataAPI;
import com.aekan.navya.lpgbooking.utilities.LPGServiceCallBackHandler;
import com.aekan.navya.lpgbooking.utilities.LPGServiceResponseCallBack;
import com.aekan.navya.lpgbooking.utilities.LPG_PhoneListener;
import com.aekan.navya.lpgbooking.utilities.LPG_SQL_ContractClass;
import com.aekan.navya.lpgbooking.utilities.LPG_Utility;
import com.aekan.navya.lpgbooking.utilities.lpgconnectionparcel;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by arunramamurthy on 16/10/16.
 */

public class LPGBooking extends AppCompatActivity implements LPGServiceResponseCallBack {

    //constants to identify permission requests
    private final static int LPG_BOOKING_REQUEST_PERMISSION_CALL_PHONE = 1;
    private final static int LPG_BOOKING_REQUEST_PERMISSION_SMS = 2;
    String lpgparcelConnectionProvider;
    String lpgparcelConnectionPhoneNumber;
    Intent lpgBookingCallIntent;
    // use a field within this class to store phone no
    // this field would be initialised during onCreate and would
    // be used subsequently during permission response handling
    private String LPG_CONNECTION_PHONE_NO;
    private String LPG_SMS_REFILL_NO;
    private TelephonyManager telephonyManager;
    private LPG_PhoneListener phoneStateListener;
    private String lpgparcelConnectionId; // connection id of record being booked
    //String lpgparcelLastBookedDate;
    //String lpgparcelExpectedExpiryDays;

    @Override
    @RequiresPermission(allOf = {Manifest.permission.SEND_SMS, Manifest.permission.CALL_PHONE})

    protected void onCreate(@Nullable Bundle savedInstanceState) {


        //call superclass oncreate function
        super.onCreate(savedInstanceState);

        //set content view for the activity
        setContentView(R.layout.lpg_booking);
        EditText lpgConnectionName = (EditText) findViewById(R.id.lpgbooking_connectionname_edittext);
        EditText lpgProvider = (EditText) findViewById(R.id.lpgbooking_provider_edittext);
        EditText lpgExpiryDate = (EditText) findViewById(R.id.lpgbooking_expected_expiry_date_edittext);
        ImageView lpgBookingCall = (ImageView) findViewById(R.id.lpgbooking_call_img);
        ImageView lpgBookingSMS = (ImageView) findViewById(R.id.lpgbooking_sms_img);
        //make the edittext as non clickable
        lpgConnectionName.setClickable(false);
        lpgExpiryDate.setClickable(false);
        lpgProvider.setClickable(false);
        lpgBookingCall.setEnabled(false);
        lpgBookingSMS.setEnabled(false);
        //set back stack for the activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.lpgbooking_toolbar);
        //set support action bar
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homeIntent = new Intent(getApplicationContext(),MainActivity.class);
                if (homeIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(homeIntent);
                }
            }
        });
        //set null values for Phone and SMS - before binding lPG Connection details
        LPG_CONNECTION_PHONE_NO = null;
        LPG_SMS_REFILL_NO = null;

        //get the connection id from the parcel, which has been added to the intent
        lpgconnectionparcel lpgconnectioninfo = getIntent().getParcelableExtra(lpgconnectionparcel.LPG_CONNECTIONRECORD_PARCEL);
        lpgparcelConnectionId = lpgconnectioninfo.getId();
        Log.v("ClickLPGBooking", lpgparcelConnectionId);

        //register phone listener
        telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        phoneStateListener = new LPG_PhoneListener(getApplication(), lpgparcelConnectionId);
        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);



        ConcurrentHashMap<String, Cursor> concurrentHash = ((LPGApplication) getApplication()).cacheLocalData;
        if (!(concurrentHash.containsKey(lpgparcelConnectionId))) {
            LPGDataAPI addBookingAPI = new LPGDataAPI((LPGApplication) getApplication(), "Service Call from Add Booking");
            addBookingAPI.populateCylinderInfoThroughCursorWithRowID(lpgparcelConnectionId, new Messenger(new Handler(new LPGServiceCallBackHandler(this))));
        } else {
            updateActivityWithLPGDetailsCursor(concurrentHash.get(lpgparcelConnectionId));
        }



        // set onclick listener on image
        lpgBookingCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("LPGBooking","Inside On Click of Call Booking");
                //startActivity(lpgBookingCallIntent);
                //check if the app has been given permissions to make a call
                if (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    //get permission from user to use the phone again
                    //before we inform the user, we need to check if we need user permission
                    Log.v("LPGBooking","Inside No permission available");
                    if ((android.support.v4.app.ActivityCompat.shouldShowRequestPermissionRationale(LPGBooking.this, Manifest.permission.CALL_PHONE)) == false ){
                        Log.v("LPGBooking","Inside Show request permission");
                        // if user permission is needed, inform a message to user through a dialogbox
                        //android.support.v4.app.ActivityCompat.shouldShowRequestPermissionRationale(getParent(), Manifest.permission.CALL_PHONE)
                        // show the dialog box in a seperate thread so as not to stop the current execution

                        Intent permissionRequestor = new Intent(getApplicationContext(),PermissionCheckForFeature.class);
                        permissionRequestor.putExtra(LPG_Utility.PERMISSION_INTIMATION_MESSAGE,LPG_Utility.PERMISSION_CALL_INTIMATION);
                        startActivityForResult(permissionRequestor,LPG_BOOKING_REQUEST_PERMISSION_CALL_PHONE);


                    }
                    else{
                        //need not show intimation to user
                        ActivityCompat.requestPermissions(getParent(),new String[]{Manifest.permission.CALL_PHONE},LPG_BOOKING_REQUEST_PERMISSION_CALL_PHONE);
                    }

                } else {
                    // call the phone application
                    Log.v("LPGBooking"," About to call Booking Call Intent");
                    startActivity(lpgBookingCallIntent);
                }

            }
        });

        //set sms option to the app
        final Intent lpgBookThroughSMSIntent = new Intent(Intent.ACTION_VIEW);

        lpgBookingSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check if application has permission to send sms
                if ((ContextCompat.checkSelfPermission(v.getContext(),Manifest.permission.SEND_SMS)) != PackageManager.PERMISSION_GRANTED) {
                    // no permission to send sms
                    // request permission for sending SMS

                    Intent SMSPermissonRequestor = new Intent(getApplicationContext(),PermissionCheckForFeature.class);
                    SMSPermissonRequestor.putExtra(LPG_Utility.PERMISSION_INTIMATION_MESSAGE,LPG_Utility.PERMISSION_SMS_INTIMATION);
                    startActivityForResult(SMSPermissonRequestor,LPG_BOOKING_REQUEST_PERMISSION_SMS);

                } else{
                    //the app has permission to send sms
                    SmsManager smsManager = SmsManager.getDefault();
                    //Get details to send SMS to

                }
            }
        });
        //

    }

    @Override
    public void onRequestPermissionsResult(int RequestCode, String permissions[], int[] grantResults) {
        // handle cases for permissions result for call and sms based on RequestCode

        switch (RequestCode) {
            case LPG_BOOKING_REQUEST_PERMISSION_CALL_PHONE: {
                if ((grantResults.length != 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // Permission request was successful with the user
                    // start the intent to call the user
                    if (LPG_CONNECTION_PHONE_NO != null) {

                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        startActivity(lpgBookingCallIntent);
                    }



                } else {
                    ((LPGApplication) getApplication()).LPG_Alert.showDialogHelper(getResources().getString(R.string.lpgbooking_callpermissiondialog_title),"OK",null,new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Intent homeIntent = new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(homeIntent);
                        }
                    } ,null).show(getSupportFragmentManager(),"Cancel permission");
                }
                break;
            }

            case LPG_BOOKING_REQUEST_PERMISSION_SMS:{
                // check if permission has been granted

                if (grantResults[0] == PackageManager.PERMISSION_GRANTED ){
                    // send sms

                } else {
                    ((LPGApplication) getApplication()).LPG_Alert.showDialogHelper(getResources().getString(R.string.lpgbooking_callpermissiondialog_title),"OK",null,new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Intent homeIntent = new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(homeIntent);
                        }
                    } ,null).show(getSupportFragmentManager(),"Cancel permission");
                }
                break;
            }

        }

    }


    @Override
    protected void onResume() {
        super.onResume();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);
    }

    protected void onActivityResult(int requestCode, int resultCode , Intent resultIntent){
        // check if the result is for Call or SMS
        switch (requestCode) {
            case LPG_BOOKING_REQUEST_PERMISSION_CALL_PHONE:
                //check if access was granted :
                switch (resultCode){
                    case Activity.RESULT_CANCELED:
                        //User has not given permission to use call feature of the phone
                        // Present a dialog box and go back to Home screen
                        ((LPGApplication) getApplication()).LPG_Alert.showDialogHelper(getResources().getString(R.string.lpgbooking_permissioncancellation_request), "OK", null, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Intent homeIntent = new Intent(getApplicationContext(),MainActivity.class);
                                startActivity(homeIntent);
                            }
                        },null).show(getSupportFragmentManager(),"Permission not granted");
                        break;
                    case Activity.RESULT_OK:
                        //Request for permissions
                        ActivityCompat.requestPermissions(LPGBooking.this, new String[]{Manifest.permission.CALL_PHONE}, LPG_BOOKING_REQUEST_PERMISSION_CALL_PHONE);
                }

            case LPG_BOOKING_REQUEST_PERMISSION_SMS:
                    switch (resultCode){
                        case Activity.RESULT_CANCELED:
                            ((LPGApplication) getApplication()).LPG_Alert.showDialogHelper(getResources().getString(R.string.lpgbooking_permissioncancellation_request),"OK",null,new DialogInterface.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    Intent homeIntent = new Intent(getApplicationContext(),MainActivity.class);
                                    startActivity(homeIntent);
                                }
                            } ,null).show(getSupportFragmentManager(),"Permission not granted for SMS");
                            break;
                        case Activity.RESULT_OK:
                            //Request for SMS permission
                            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS},LPG_BOOKING_REQUEST_PERMISSION_SMS);
                    }
        }


    }

    @Override
    public void updateActivityWithLPGDetailsCursor(Cursor c) {
        EditText lpgConnectionName = (EditText) findViewById(R.id.lpgbooking_connectionname_edittext);
        EditText lpgProvider = (EditText) findViewById(R.id.lpgbooking_provider_edittext);
        EditText lpgExpiryDate = (EditText) findViewById(R.id.lpgbooking_expected_expiry_date_edittext);
        ImageView ClickToCall = (ImageView) findViewById(R.id.lpgbooking_call_img);
        ImageView ClickToSMS = (ImageView) findViewById(R.id.lpgbooking_sms_img);
        if (c.moveToFirst()) {
            lpgConnectionName.setText(c.getString(c.getColumnIndex(LPG_SQL_ContractClass.LPG_CONNECTION_ROW.CONNECTION_NAME)));
            lpgProvider.setText(c.getString(c.getColumnIndex(LPG_SQL_ContractClass.LPG_CONNECTION_ROW.PROVIDER)));
            LPG_CONNECTION_PHONE_NO = c.getString(c.getColumnIndex(LPG_SQL_ContractClass.LPG_CONNECTION_ROW.AGENCY_PHONE_NUMBER));
            LPG_SMS_REFILL_NO = c.getString(c.getColumnIndex(LPG_SQL_ContractClass.LPG_CONNECTION_ROW.AGENCY_SMS_NUMBER));
            lpgparcelConnectionProvider = c.getString(c.getColumnIndex(LPG_SQL_ContractClass.LPG_CONNECTION_ROW.PROVIDER));
            // Set expected expiry date
            String lpgparcelLastBookedDate = c.getString(c.getColumnIndex(LPG_SQL_ContractClass.LPG_CONNECTION_ROW.LAST_BOOKED_DATE));
            String lpgparcelExpectedExpiryDays = c.getString(c.getColumnIndex(LPG_SQL_ContractClass.LPG_CONNECTION_ROW.CONNECTION_EXPIRY_DAYS));

            String[] lpgStringArrayForLastBookedDate = lpgparcelLastBookedDate.split("/");
            int lpgLastExpiryDateDay = Integer.valueOf(lpgStringArrayForLastBookedDate[0]);
            int lpgLastExpiryDateMonth = Integer.valueOf(lpgStringArrayForLastBookedDate[1]);
            int lpgLastExpiryDateYear = Integer.valueOf(lpgStringArrayForLastBookedDate[2]);

            GregorianCalendar lpgCalendarLastBookedDate = new GregorianCalendar();
            lpgCalendarLastBookedDate.set(Calendar.DATE, lpgLastExpiryDateDay);
            lpgCalendarLastBookedDate.set(Calendar.MONTH, lpgLastExpiryDateMonth - 1);
            lpgCalendarLastBookedDate.set(Calendar.YEAR, lpgLastExpiryDateYear);
            lpgCalendarLastBookedDate.add(Calendar.DATE, Integer.valueOf(lpgparcelExpectedExpiryDays));
            //set this value to expected expiry date field
            // Get the String value of the Date
            String lpgStringExpiryDate = Integer.toString(lpgCalendarLastBookedDate.get(Calendar.DATE)) + "/" +
                    Integer.toString(lpgCalendarLastBookedDate.get(Calendar.MONTH) + 1) + "/" +
                    Integer.toString(lpgCalendarLastBookedDate.get(Calendar.YEAR));
            lpgExpiryDate.setText(lpgStringExpiryDate);

            //Enable command buttons
            ClickToCall.setEnabled(true);
            ClickToSMS.setEnabled(true);

            //Set telephone call intent for call image
            lpgBookingCallIntent = new Intent(Intent.ACTION_CALL);
            lpgBookingCallIntent.setData(Uri.parse("tel:" + LPG_CONNECTION_PHONE_NO));

        }


    }

    @Override
    public void updatePrimaryKeyIncrement(String incrementedPrimaryKey) {

    }

    public static class mSMSBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

        }

    }
}
