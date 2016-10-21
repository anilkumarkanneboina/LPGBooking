package com.aekan.navya.lpgbooking;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.aekan.navya.lpgbooking.utilities.lpgconnectionparcel;

/**
 * Created by arunramamurthy on 16/10/16.
 */

public class LPGBooking extends AppCompatActivity {

    //constants to identify permission requests
    private final static int LPG_BOOKING_REQUEST_PERMISSION_CALL_PHONE = 1;
    // use a field within this class to store phone no
    // this field would be initialised during onCreate and would
    // be used subsequently during permission response handling
    private String LPG_CONNECTION_PHONE_NO;

    @Override
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

        //Local variables to get connection details from parcel;
        String lpgparcelConnectionId;
        String lpgparcelConnectionName;
        String lpgparcelConnectionProvider;
        String lpgparcelConnectionAgency;
        String lpgparcelConnectionPhoneNumber;
        String lpgparcelConnectionIdentifier;
        String lpgparcelLastBookedDate;

        //get the parcealable content from the intent
        // Connection details could have been put in the Parcealable object in two way
        // either in the Parcel type field, or in individual fields
        // We will get connection details by checking if the Parcel field is null
        lpgconnectionparcel lpgconnectioninfo = getIntent().getParcelableExtra(lpgconnectionparcel.LPG_CONNECTIONRECORD_PARCEL);
        Parcel lpgConnectionParcel = lpgconnectioninfo.getLpgConnectionrecordParcel();
        if (lpgconnectioninfo.getLpgConnectionrecordParcel() != null) {
            //get the connection details from Parcel field of the Parcealable object
            lpgparcelConnectionId = lpgConnectionParcel.readString();
            lpgparcelConnectionName = lpgConnectionParcel.readString();
            lpgparcelConnectionProvider = lpgConnectionParcel.readString();
            lpgparcelConnectionAgency = lpgConnectionParcel.readString();
            lpgparcelConnectionPhoneNumber = lpgConnectionParcel.readString();
            //initialise LPG Connection phone no
            LPG_CONNECTION_PHONE_NO = lpgparcelConnectionPhoneNumber;
            lpgparcelConnectionIdentifier = lpgConnectionParcel.readString();
            lpgparcelLastBookedDate = lpgConnectionParcel.readString();

        } else {
            //get the data from the individual fields using getter methods in the parcel
            lpgparcelConnectionName = lpgconnectioninfo.getConnectionname();
            lpgparcelConnectionProvider = lpgconnectioninfo.getProvider();
            lpgparcelConnectionPhoneNumber = lpgconnectioninfo.getPhonenumber();
            //initialise LPG connetion phone no
            LPG_CONNECTION_PHONE_NO = lpgparcelConnectionPhoneNumber;
            lpgparcelConnectionIdentifier = lpgconnectioninfo.getConnectionid();
            lpgparcelLastBookedDate = lpgconnectioninfo.getLastbookeddate();
        }

        //set the edit text accordingly
        lpgConnectionName.setText(lpgparcelConnectionName);
        lpgProvider.setText(lpgparcelConnectionProvider);
        lpgExpiryDate.setText(lpgparcelLastBookedDate);

        //Set telephone call intent for call image
        final Intent lpgBookingCallIntent = new Intent(Intent.ACTION_CALL);
        lpgBookingCallIntent.setData(Uri.parse("tel:" + lpgparcelConnectionPhoneNumber));
        // set onclick listener on image
        lpgBookingCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //startActivity(lpgBookingCallIntent);
                //check if the app has been given permissions to make a call
                if (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    //get permission from user to use the phone again
                    //before we inform the user, we need to check if we need user permission

                    if (ActivityCompat.shouldShowRequestPermissionRationale(getParent(), Manifest.permission.CALL_PHONE)) {
                        // if user permission is needed, inform a message to user through a dialogbox
                        // show the dialog box in a seperate thread so as not to stop the current execution
                        new AsyncTask<Void, Void, Boolean>() {
                            // DialogBox is being shown in worker thread of AsyncTask, through the doInBackground function
                            @Override
                            protected Boolean doInBackground(Void... voids) {

                                ((LPGApplication) getApplication())
                                        .LPG_Alert
                                        .showDialogHelper(
                                                getResources().getString(R.string.lpgbooking_callpermissiondialog_title),
                                                getResources().getString(R.string.DIALOG_OK),
                                                null,
                                                new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {

                                                        dialog.dismiss();

                                                    }
                                                },
                                                null
                                        );
                                ((LPGApplication) getApplication())
                                        .LPG_Alert.show(getSupportFragmentManager(), "Request Call Permission");

                                return true;

                            }

                            @Override
                            protected void onPostExecute(Boolean result) {
                                //show the user the permission dialog box to request for his permission.

                                ActivityCompat.requestPermissions(getParent(), new String[]{Manifest.permission.CALL_PHONE}, LPG_BOOKING_REQUEST_PERMISSION_CALL_PHONE);
                            }

                        }.execute();
                    }

                } else {
                    // call the phone application
                    startActivity(lpgBookingCallIntent);
                }

            }
        });

        //set sms option to the app
        final Intent lpgBookThroughSMSIntent = new Intent(Intent.ACTION_VIEW);


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
                        Intent lpgBookingCallIntent = new Intent(Intent.ACTION_CALL);
                        lpgBookingCallIntent.setData(Uri.parse("tel:" + LPG_CONNECTION_PHONE_NO));
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



                }

            }
        }

    }
}
