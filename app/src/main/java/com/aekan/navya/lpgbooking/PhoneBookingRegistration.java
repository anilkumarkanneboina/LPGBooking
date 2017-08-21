package com.aekan.navya.lpgbooking;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Messenger;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.aekan.navya.lpgbooking.utilities.LPGDataAPI;
import com.aekan.navya.lpgbooking.utilities.LPGServiceCallBackHandler;
import com.aekan.navya.lpgbooking.utilities.LPGServiceResponseCallBack;
import com.aekan.navya.lpgbooking.utilities.LPG_PhoneListener;
import com.aekan.navya.lpgbooking.utilities.LPG_SQL_ContractClass;
import com.aekan.navya.lpgbooking.utilities.LPG_Utility;

import org.w3c.dom.Text;

import java.util.HashMap;

/**
 * Created by arunramamurthy on 20/06/17.
 */

public class PhoneBookingRegistration extends AppCompatActivity implements LPGServiceResponseCallBack {

    private final int LPG_BOOKING_REQUEST_PERMISSION_CALL_PHONE = 123;
    private final int LPG_BOOKING_REQUEST_PERMISSION_SMS = 234;
    //Array adapter for spinner
    private ArrayAdapter<CharSequence> mSpinnerAdapter;
    private HashMap<String, String> mMapConnectionNameAndId;
    private String mConnection;
    private Cursor mCursor;
    private Button mbuttonRegister;
    private int activityPurpose;
    private TelephonyManager telephonyManager;
    private PhoneStateListener phonelistener;
    private String phoneNumber;
    private String provider;

    TextView providerTextView;
    TextView  agencyTextView;
    TextView phonenumberTextView;
    TextView registrationNotification;
    Button regButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //call super class
        super.onCreate(savedInstanceState);
        //inflate
        setContentView(R.layout.phone_registration);
        //Configure tool bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.phoneregistration_toolbar);


        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homeActivity = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(homeActivity);
            }
        });
        setSupportActionBar(toolbar);

        //verify if the activity is being used for phone booking registration or SMS booking registration.
        activityPurpose = getIntent().getIntExtra(LPG_Utility.REGISTRATION_TYPE,LPG_Utility.PHONE_BOOKING_REGISTRATION);

        phoneNumber = ((TextView)findViewById(R.id.reg_no_textfield)).getText().toString();
        provider = ((TextView) findViewById(R.id.reg_provider)).getText().toString();

        providerTextView = (TextView) findViewById(R.id.reg_provider);
        agencyTextView = (TextView) findViewById(R.id.reg_agency);
        phonenumberTextView = (TextView) findViewById(R.id.reg_no_textfield);
        registrationNotification = (TextView) findViewById(R.id.registration_notification_message) ;
        regButton = (Button) findViewById(R.id.reg_button);

        //Disable registration button
        mbuttonRegister = (Button) findViewById(R.id.reg_button);
        mbuttonRegister.setEnabled(false);
        //set onclicklistener for booking button
        mbuttonRegister.setOnClickListener(new View.OnClickListener() {
            //get mobile no from form


            @Override
            public void onClick(View v) {
                //get phone no from text view
                phoneNumber = ((TextView) findViewById(R.id.reg_no_textfield)).getText().toString();

                switch (activityPurpose){
                    case (LPG_Utility.PHONE_BOOKING_REGISTRATION):
                        //check for permission to call phone
                        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            //permission not available
                            //check if permission needs to be requested
                            if (ActivityCompat.shouldShowRequestPermissionRationale(PhoneBookingRegistration.this, android.Manifest.permission.CALL_PHONE)) {
                                //need to request permission
                                Intent permissionIntent = new Intent(getApplicationContext(),PermissionCheckForFeature.class);
                                permissionIntent.putExtra(LPG_Utility.PERMISSION_INTIMATION_MESSAGE,LPG_Utility.PERMISSION_CALL_INTIMATION);
                                startActivityForResult(permissionIntent, LPG_BOOKING_REQUEST_PERMISSION_CALL_PHONE);

                            } else {
                                //show permission for phonebooking
                                ActivityCompat.requestPermissions(PhoneBookingRegistration.this, new String[]{android.Manifest.permission.CALL_PHONE, android.Manifest.permission.SEND_SMS}, LPG_BOOKING_REQUEST_PERMISSION_CALL_PHONE);
                            }
                        } else {
                            //call the provider no for registration
                            LPG_Utility.callOrTextUtility(getApplicationContext(), phoneNumber, null, provider, LPG_Utility.COMMUNICATE_PHONE);
                        }
                        break;
                    case (LPG_Utility.SMS_BOOKING_REGISTRATIION):
                        //check permission for sending sms
                        if ((ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.SEND_SMS)) != PackageManager.PERMISSION_GRANTED) {
                            // check if user needs tobe intimated about the permission request
                            if (ActivityCompat.shouldShowRequestPermissionRationale(PhoneBookingRegistration.this, android.Manifest.permission.SEND_SMS)) {
                                //start permission check activity with SMS Booking
                                Intent intentSMSPermission = new Intent(getApplicationContext(), PermissionCheckForFeature.class);
                                intentSMSPermission.putExtra(LPG_Utility.PERMISSION_INTIMATION_MESSAGE, LPG_Utility.PERMISSION_SMS_INTIMATION);
                                startActivityForResult(intentSMSPermission, LPG_BOOKING_REQUEST_PERMISSION_SMS);
                            } else {
                                //request permission from user
                                ActivityCompat.requestPermissions(PhoneBookingRegistration.this, new String[]{android.Manifest.permission.SEND_SMS, android.Manifest.permission.CALL_PHONE}, LPG_BOOKING_REQUEST_PERMISSION_SMS);
                            }
                        } else {
                            // send sms
                            LPG_Utility.callOrTextUtility(getApplicationContext(), phoneNumber, null, provider, LPG_Utility.COMMUNICATE_SMS);
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.smsbooking_smssent), Toast.LENGTH_SHORT).show();
                        }

                        break;
                    default:
                        break;

                }
            }
        });

        //set activity title and hint text
        TextInputLayout numberToRegister = (TextInputLayout) findViewById(R.id.reg_no_textinputlayout) ;
        switch (activityPurpose){
            case (LPG_Utility.PHONE_BOOKING_REGISTRATION):
                numberToRegister.setHint(getResources().getString(R.string.registration_phonebooking_hint_regno));
                toolbar.setTitle(getResources().getString(R.string.phonebooking_activity_title));
                //initialise telephone state listener
                telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                phonelistener = new LPG_PhoneListener(this,LPG_Utility.PHONELISTENER_FROM_REGISTRATION);
                telephonyManager.listen(phonelistener,PhoneStateListener.LISTEN_CALL_STATE);

                break;
            case (LPG_Utility.SMS_BOOKING_REGISTRATIION):
                numberToRegister.setHint(getResources().getString(R.string.registration_smsbooking_hint_regno));
                toolbar.setTitle(getResources().getString(R.string.smsbooking_activity_title));
                ((TextView) findViewById(R.id.reg_button)).setText(getResources().getString(R.string.smsbooking_buttonname));
                break;
            default:
                numberToRegister.setHint(getResources().getString(R.string.registration_phonebooking_hint_regno));
                toolbar.setTitle(getResources().getString(R.string.phonebooking_activity_title));
                break;
        }

        //Populate spinner with connection names
        LPGDataAPI lpgDataAPI = new LPGDataAPI((LPGApplication) getApplication(), "Call from Navigation Drawer");
        Messenger serviceResponseCallbackMessenger = new Messenger(new Handler(new LPGServiceCallBackHandler(this)));

        lpgDataAPI.populateAllConnections(serviceResponseCallbackMessenger);


    }

    public void updateAllConnectionData(Cursor c) {
        if (c == null) {
            return;
        }
        //get list of row ids into an array
        // and create array adapter from this array
        mCursor = c;
        String[] connectionNames = new String[c.getCount()];
        if (!c.moveToFirst()) {
            findViewById(R.id.registration_spinner).setVisibility(View.GONE);
            findViewById(R.id.reg_provider_textinputlayout).setVisibility(View.GONE);
            findViewById(R.id.registration_spinner).setVisibility(View.GONE);
            findViewById(R.id.reg_agency_textinputlayout).setVisibility(View.GONE);
            findViewById(R.id.reg_no_textinputlayout).setVisibility(View.GONE);
            findViewById(R.id.registration_notification_message).setVisibility(View.GONE);
            findViewById(R.id.reg_button).setVisibility(View.GONE);
            ((TextView)findViewById(R.id.registration_description)).setText(R.string.registration_noconnection);
            return;
        }


        //get list of connection names in a array
        for (int i = 0; i < (c.getCount() - 1); ++i) {
            connectionNames[i] = (c.getString(c.getColumnIndex(LPG_SQL_ContractClass.LPG_CONNECTION_ROW.CONNECTION_NAME)));
            c.moveToNext();

        }
        connectionNames[c.getCount() - 1] = c.getString(c.getColumnIndex(LPG_SQL_ContractClass.LPG_CONNECTION_ROW.CONNECTION_NAME));
        //Initialise the spinner now
        Spinner connectionSpinner = (Spinner) findViewById(R.id.registration_spinner);

        ArrayAdapter<String> connectionAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, connectionNames);
        connectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        connectionSpinner.setAdapter(connectionAdapter);

        //set item click listener for spinner
        connectionSpinner.setOnItemSelectedListener(new SpinnerListener(mCursor));

        //enable button
        mbuttonRegister.setEnabled(true);


    }

    public void updateActivityWithLPGDetailsCursor(Cursor c) {
    }

    public void updatePrimaryKeyIncrement(String incrementedPrimaryKey) {
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent resultIntent) {
        //Provide response based on activity request code
        switch (requestCode) {
            case (LPG_BOOKING_REQUEST_PERMISSION_CALL_PHONE):
                //check if user is ok to provide phone permission
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        //provide user with permission dialog for call permission
                        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.SEND_SMS, android.Manifest.permission.CALL_PHONE}, LPG_BOOKING_REQUEST_PERMISSION_CALL_PHONE);
                        break;
                    case Activity.RESULT_CANCELED:
                        //inform user to check again
                        Toast.makeText(this, R.string.permission_toast_callpermission_denied, Toast.LENGTH_SHORT).show();
                }

                break;
            case (LPG_BOOKING_REQUEST_PERMISSION_SMS):
                //decide if user has given permission
                switch (resultCode) {
                    //user did not give permission to send SMS
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.permission_toast_sms_denied), Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_OK:
                        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.SEND_SMS, android.Manifest.permission.CALL_PHONE}, LPG_BOOKING_REQUEST_PERMISSION_SMS);
                }
        }
    }

    public void onRequestPermissionsResult(int RequestCode, String permissions[], int[] grantResults) {
        //check permission status for different actions
        switch (RequestCode) {
            case (LPG_BOOKING_REQUEST_PERMISSION_CALL_PHONE):
                if ((grantResults.length != 0) && (grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
                    //check for permission and call registration number
                    if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                        //call lpg booking
                        LPG_Utility.callOrTextUtility(getApplicationContext(), phoneNumber, null, provider, LPG_Utility.COMMUNICATE_PHONE);
                    }

                } else {
                    Toast.makeText(this, R.string.permission_toast_callpermission_denied, Toast.LENGTH_SHORT).show();

                }

                break;
            case (LPG_BOOKING_REQUEST_PERMISSION_SMS):
                if ((grantResults.length != 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                        LPG_Utility.callOrTextUtility(getApplicationContext(), phoneNumber, null, provider, LPG_Utility.COMMUNICATE_SMS);
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.smsbooking_smssent), Toast.LENGTH_SHORT).show();
                    }
                }

            default:
                break;

        }

    }

    protected void onDestroy() {
        super.onDestroy();
        //unregister telephone listener
        if (phonelistener != null) {
            telephonyManager.listen(phonelistener, PhoneStateListener.LISTEN_NONE);
        }
    }

    public class SpinnerListener implements AdapterView.OnItemSelectedListener {

        //cursor to hold variables for cursor
        private Cursor mCursor;

        public SpinnerListener(Cursor c) {
            mCursor = c;
            mCursor.moveToFirst();

        }


        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            //Get selected item
            String connectionName,providerName=LPG_Utility.PROVIDER_NAME_UNDEFINED;
            //get views from layout which need to be initialised

            parent.setSelection(position);


            // get adapter
            ArrayAdapter<String> parentAdapter = (ArrayAdapter<String>) parent.getAdapter();
            connectionName = parentAdapter.getItem(position);
            //update text fields based on the connection name selected
            // by iterating connection name within the cursor and getting connection details
            // from cursor
            for (int i = 0; i < mCursor.getCount(); ++i) {
                if (mCursor.getString(mCursor.getColumnIndex(LPG_SQL_ContractClass.LPG_CONNECTION_ROW.CONNECTION_NAME)).equals(connectionName)) {
                    providerTextView.setText(mCursor.getString(mCursor.getColumnIndex(LPG_SQL_ContractClass.LPG_CONNECTION_ROW.PROVIDER)));
                    agencyTextView.setText(mCursor.getString(mCursor.getColumnIndex(LPG_SQL_ContractClass.LPG_CONNECTION_ROW.AGENCY)));
                    phonenumberTextView.setText(mCursor.getString(mCursor.getColumnIndex(LPG_SQL_ContractClass.LPG_CONNECTION_ROW.AGENCY_SMS_NUMBER)));

                    String phoneNumberText = phonenumberTextView.getText().toString();
                    providerName = providerTextView.getText().toString();
                    phoneNumber = phoneNumberText;
                    if(phoneNumberText.length() == 0){
                        // inform user to enter a valid number
                        TextView notificationText = (TextView) findViewById(R.id.registration_notification_message);
                        notificationText.setVisibility(View.VISIBLE);
                        //give notification based on intent type
                        switch (activityPurpose){
                            case LPG_Utility.PHONE_BOOKING_REGISTRATION:
                                notificationText.setText(getResources().getString(R.string.registation_phonebooking_null_number));
                                break;
                            case LPG_Utility.SMS_BOOKING_REGISTRATIION:
                                notificationText.setText(getResources().getString(R.string.registation_smsbooking_null_number));
                                break;
                        }

                    } else  {
                        //enable call booking button
                        (findViewById(R.id.reg_button)).setEnabled(true);
                        //disable notification message
                        findViewById(R.id.registration_notification_message).setVisibility(View.GONE);
                    }






                }
            mCursor.moveToNext();

        }
            //disable SMS notification if provider is not one of major three
            if (providerName.equals(LPG_Utility.PROVIDER_NAME_UNDEFINED)){
                registrationNotification.setVisibility(View.VISIBLE);
                registrationNotification.setText(  getResources().getString(R.string.reg_notification_pristine) );
                registrationNotification.setTextColor(Color.parseColor(getResources().getString(R.string.reg_notification_text_color)));
                regButton.setEnabled(true);


            } else {
                registrationNotification.setVisibility(View.VISIBLE);
                registrationNotification.setText(  getResources().getString(R.string.reg_notification_sms_error) );
                registrationNotification.setTextColor(Color.parseColor(getResources().getString(R.string.reg_notificiation_text_color_red)));
                regButton.setEnabled(false);

            }


            //reset cursor
            mCursor.moveToFirst();

        }

        public void onNothingSelected(AdapterView<?> parent) {

            Log.v("Spinner","nothing selected");
        }

    }


}
