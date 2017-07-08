package com.aekan.navya.lpgbooking;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
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
import java.util.jar.Manifest;

/**
 * Created by arunramamurthy on 20/06/17.
 */

public class PhoneBookingRegistration extends AppCompatActivity implements LPGServiceResponseCallBack {

    //Array adapter for spinner
    private ArrayAdapter<CharSequence> mSpinnerAdapter;
    private HashMap<String, String> mMapConnectionNameAndId;
    private String mConnection;
    private Cursor mCursor;
    private Button mbuttonRegister;
    private int activityPurpose;
    private final int LPG_BOOKING_REQUEST_PERMISSION_CALL_PHONE = 123;
    private TelephonyManager telephonyManager;
    private PhoneStateListener phonelistener;
    private String phoneNumber;
    private String provider;
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

        //Disable registration button
        mbuttonRegister = (Button) findViewById(R.id.reg_button);
        mbuttonRegister.setEnabled(false);
        //set onclicklistener for booking button
        mbuttonRegister.setOnClickListener(new View.OnClickListener() {
            //get mobile no from form


            @Override
            public void onClick(View v) {
                switch (activityPurpose){
                    case (LPG_Utility.PHONE_BOOKING_REGISTRATION):
                        //check for permission to call phone
                        if(ContextCompat.checkSelfPermission(getParent(), android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                            //permission not available
                            //check if permission needs to be requested
                            if(ActivityCompat.shouldShowRequestPermissionRationale(getParent(), android.Manifest.permission.CALL_PHONE)){
                                //need to request permission
                                Intent permissionIntent = new Intent(getApplicationContext(),PermissionCheckForFeature.class);
                                permissionIntent.putExtra(LPG_Utility.PERMISSION_INTIMATION_MESSAGE,LPG_Utility.PERMISSION_CALL_INTIMATION);
                                getParent().startActivityForResult(permissionIntent,LPG_BOOKING_REQUEST_PERMISSION_CALL_PHONE);

                            } else {
                                //show permission for phonebooking
                                ActivityCompat.requestPermissions(getParent(),new String[]{android.Manifest.permission.CALL_PHONE, android.Manifest.permission.SEND_SMS},LPG_BOOKING_REQUEST_PERMISSION_CALL_PHONE);
                            }
                        } else {
                            //call the provider no for registration
                            LPG_Utility.callOrTextUtility(getParent(),phoneNumber,null,provider,LPG_Utility.COMMUNICATE_PHONE);
                        }
                        break;
                    case (LPG_Utility.SMS_BOOKING_REGISTRATIION):
                        //check permission for sending sms
                        if((ContextCompat.checkSelfPermission(getParent(), android.Manifest.permission.SEND_SMS)) != PackageManager.PERMISSION_GRANTED){
                            // check if user needs tobe intimated about the permission request
                            if ( ActivityCompat.shouldShowRequestPermissionRationale(getParent(), android.Manifest.permission.SEND_SMS)  ) {
                                //start permission check activity with

                            }
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
        c.moveToFirst();
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

    public class SpinnerListener implements AdapterView.OnItemSelectedListener {

        //cursor to hold variables for cursor
        private Cursor mCursor;

        public SpinnerListener(Cursor c) {
            mCursor = c;
            mCursor.moveToFirst();

        }


        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            //Get selected item
            String connectionName;
            //get views from layout which need to be initialised
            TextView provider = (TextView) findViewById(R.id.reg_provider);
            TextView agency = (TextView) findViewById(R.id.reg_agency);
            TextView phonenumber = (TextView) findViewById(R.id.reg_no_textfield);

            parent.setSelection(position);
            // get adapter
            ArrayAdapter<String> parentAdapter = (ArrayAdapter<String>) parent.getAdapter();
            connectionName = parentAdapter.getItem(position);
            //update text fields based on the connection name selected
            // by iterating connection name within the cursor and getting connection details
            // from cursor
            for (int i = 0; i < mCursor.getCount(); ++i) {
                if (mCursor.getString(mCursor.getColumnIndex(LPG_SQL_ContractClass.LPG_CONNECTION_ROW.CONNECTION_NAME)).equals(connectionName)) {
                    provider.setText(mCursor.getString(mCursor.getColumnIndex(LPG_SQL_ContractClass.LPG_CONNECTION_ROW.PROVIDER)));
                    agency.setText(mCursor.getString(mCursor.getColumnIndex(LPG_SQL_ContractClass.LPG_CONNECTION_ROW.AGENCY)));
                    phonenumber.setText(mCursor.getString(mCursor.getColumnIndex(LPG_SQL_ContractClass.LPG_CONNECTION_ROW.AGENCY_SMS_NUMBER)));
                    String phoneNumberText = phonenumber.getText().toString();
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
            //reset cursor
            mCursor.moveToFirst();

        }

        public void onNothingSelected(AdapterView<?> parent) {

            Log.v("Spinner","nothing selected");
        }

    }

    protected void onActivityResult(int requestCode, int resultCode , Intent resultIntent){
        //Provide response based on activity request code
        switch (requestCode){
            case (LPG_BOOKING_REQUEST_PERMISSION_CALL_PHONE):
                //check if user is ok to provide phone permission
                switch (resultCode){
                    case Activity.RESULT_OK:
                        //provide user with permission dialog for call permission
                        ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.SEND_SMS, android.Manifest.permission.CALL_PHONE},LPG_BOOKING_REQUEST_PERMISSION_CALL_PHONE);
                        break;
                    case Activity.RESULT_CANCELED:
                        //inform user to check again
                        Toast.makeText(this,R.string.permission_toast_callpermission_denied,Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    public void onRequestPermissionsResult(int RequestCode, String permissions[], int[] grantResults) {
        //check permission status for different actions
        switch (RequestCode){
            case (LPG_BOOKING_REQUEST_PERMISSION_CALL_PHONE):
                if((grantResults.length != 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)){
                    //check for permission and call registration number
                    if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE)== PackageManager.PERMISSION_GRANTED){
                        //call lpg booking
                        LPG_Utility.callOrTextUtility(getParent(),phoneNumber,null,provider,LPG_Utility.COMMUNICATE_PHONE);
                    }

                } else{
                    Toast.makeText(this,R.string.permission_toast_callpermission_denied,Toast.LENGTH_SHORT).show();

                }

                break;

            default:
                break;

        }

    }

    protected void onDestroy(){
        super.onDestroy();
        //unregister telephone listener
        telephonyManager.listen(phonelistener,PhoneStateListener.LISTEN_NONE);
    }


}
