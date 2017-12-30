package com.aekan.navya.lpgbooking;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Messenger;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.aekan.navya.lpgbooking.utilities.LPGDataAPI;
import com.aekan.navya.lpgbooking.utilities.LPGServiceAPI;
import com.aekan.navya.lpgbooking.utilities.LPGServiceCallBackHandler;
import com.aekan.navya.lpgbooking.utilities.LPGServiceResponseCallBack;
import com.aekan.navya.lpgbooking.utilities.LPG_AlertBoxClass;
import com.aekan.navya.lpgbooking.utilities.LPG_SQLOpenHelperClass;
import com.aekan.navya.lpgbooking.utilities.LPG_SQL_ContractClass;
import com.aekan.navya.lpgbooking.utilities.LPG_Utility;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static com.aekan.navya.lpgbooking.utilities.LPG_Utility.Alert;
import static com.aekan.navya.lpgbooking.utilities.LPG_Utility.ifWeCanShowInterstitialAdNow;

public class AddLPGConnection extends AppCompatActivity implements LPGServiceResponseCallBack {

    public GregorianCalendar test_alarm_midway;
    public GregorianCalendar test_alarm_final_expiry;
    private InterstitialAd mInterstitialAd;

    // Set REGEX strings for data validation
    private String mRegexNumber = "^[1-9][0-9]{9}$";
    private String mRegexSMS = "^[1-9][0-9]{9}$";
    private String mRegexExpiryDays = "[0-9]+";
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private String finalIDCount ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Activity creation using the saved bundle

        //Call onCreate function of super class
        super.onCreate(savedInstanceState);
        //set content view for the activity
        setContentView(R.layout.activity_add_lpgconnection);
        //Get database to do CRUD operations


        // Set REGEX strings for data validation
        final String regexNumber = "[0-9]{5,15}+";
        final String regexExpiryDays = "[0-9]+";

        //Instantiate the tool bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.addlpg_toolbar);

        //Set tool bar as action bar
        setSupportActionBar(toolbar);
        //Set Navigation icon for the toolbar
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //show interstitial ad or move to main activity
                if (!showInterStitialAd()) {
                    Intent intentMainActivity = new Intent(getApplicationContext(), MainActivity.class);
                    //check if intent would resolve to an activity and then start the activity
                    if (intentMainActivity.resolveActivity(getPackageManager()) != null) {
                        startActivity(intentMainActivity);
                    }

                }


            }


        });
        setTitle("Add Connection");

        //load interstitial ad
        loadInterstitialAd();
        //set keyboard behaviour
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        //Create values for edit text
        final EditText lpgConnection = (EditText) findViewById(R.id.add_lpgconnectionnameedittext);
        final EditText lpgProvider = (EditText) findViewById(R.id.add_provideredittext);
        final EditText lpgAgency = (EditText) findViewById(R.id.add_agencyedittext);
        final EditText lpgDistributorPhoneNo = (EditText) findViewById(R.id.add_distributorphonenumber_edittext);
        final EditText lpgAgencyPhoneNo = (EditText) findViewById(R.id.add_agencyphoneedittext);
        final EditText lpgAgencySMSNo = (EditText) findViewById(R.id.add_connectionsmsnumber);
        final EditText lpgConnectionId = (EditText) findViewById(R.id.add_connectionid);
        final EditText lpglastdatelabel = (EditText) findViewById(R.id.add_lastbookeddate);
        final EditText lpgconnnectionexpiry = (EditText) findViewById(R.id.add_connectionexpiry);
        TextView lpgUserPrompt = (TextView) findViewById(R.id.add_lpgconnection_userprompt);
        final ScrollView scrollView = (ScrollView) findViewById(R.id.form_scroll_view);
        final FloatingActionButton buttonSave = (FloatingActionButton) findViewById(R.id.fab_save_connection);

        //hide soft text pad
        hideSoftTextPad(lpgConnection);

        //Set Autocomplete values for connectino provider
        ArrayAdapter<String> lpgProviderAdapter = new ArrayAdapter<String>(this, R.layout.lpg_provider_autocomplete_layout, R.id.autocompletetextview, LPG_Utility.LPG_PROVIDERS_EXTENDEDLIST);
        AutoCompleteTextView lpgProviderAutoComplete = (AutoCompleteTextView) findViewById(R.id.add_provideredittext);
        lpgProviderAutoComplete.setAdapter(lpgProviderAdapter);


        //set hint to true in TextInputLayout - there is a jarring effect due to update from a different thread
        TextInputLayout connectionTextInput = (TextInputLayout) findViewById(R.id.add_lpgconnectionnamelabel);
        connectionTextInput.setHintAnimationEnabled(false);

        //set initial state for form
        lpglastdatelabel.setEnabled(false);
        buttonSave.setEnabled(false);


        //Load the page with data, and initialise id counters based on
        // intent filter being passed to the activity

        final String connectionIdString = getIntent().getStringExtra(LPG_SQL_ContractClass.LPG_CONNECTION_ROW.FIELD_CONNECTION_ID_EDIT);


        if (!(connectionIdString.equals(LPG_Utility.LPG_CONNECTION_ID))) {
            //get the connection id associated with the bundle
            //display the contents of the retrieved connection record in the fields

            //set text values for edit connection
            setTitle(R.string.Add_LPGBooking_EditMode_Title);
            lpgUserPrompt.setText(R.string.Add_Connection_User_Prompt_EditMode);

            //Check if we have cached details for the connection id
            boolean containsKey = LPG_Utility.hasBeenCached(connectionIdString);


            if ((containsKey)) {
                //logic to bind connection details with activity
                updateActivityWithLPGDetailsCursor(LPG_Utility.getCacheLocalData(connectionIdString));
            } else {
                //get connection details and bind with Activity elements
                //create call back messenger
                Messenger callBackMessenger = new Messenger(new Handler(new LPGServiceCallBackHandler(this)));

                //Instantiate service handler
                LPGServiceAPI serviceAPI = new LPGDataAPI(getApplicationContext(), "Service Call from Add LPG Connection to cache " + connectionIdString);

                //send message to API
                serviceAPI.populateCylinderInfoThroughCursorWithRowID(connectionIdString, callBackMessenger);

            }

            finalIDCount = connectionIdString; // Integer.toString(iDCount);
            buttonSave.setEnabled(true);


        } else {
            //set user prompts for Add connection
            setTitle(R.string.Add_LPGBooking_AddMode_Title);
            lpgUserPrompt.setText(R.string.Add_Connection_User_Prompt_AddMode);

            //this is a new connection, so increment primary key
            (new LPGDataAPI(getApplicationContext(), "Incremented Primary Key")).updateCylinderIDCursor(new Messenger(new Handler((new LPGServiceCallBackHandler(this)))));

        }

        //Set listener events for Save button and Cancel button.
        // To set listener events, initialize counter value for primary key ID;
        //Set validators for phone no and expiry dates
        lpgAgencySMSNo.addTextChangedListener(new LPG_Utility.validationListenerPhoneNumber(lpgAgencySMSNo));


        //set validator for IVRS number
        lpgAgencyPhoneNo.addTextChangedListener(new LPG_Utility.validationListenerPhoneNumber(lpgAgencyPhoneNo));


        //Get the database
        final SQLiteDatabase sqLiteDatabase = new LPG_SQLOpenHelperClass(getApplicationContext()).getWritableDatabase();

        //Set onclick listener for Save button ;
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //disable button save
                buttonSave.setEnabled(false);
                //Get validations for all fields done before submission;
                boolean dataEnteredRight = true;
                EditText scrollToError = lpgConnection;

                //hide key pad
                hideSoftTextPad(lpgConnection);


                //Validations for connection expiry to be mandatory field
                String connectionExpiry = lpgconnnectionexpiry.getText().toString();
                if ( connectionExpiry.length() == 0 ){
                    lpgconnnectionexpiry.setError(" Expiry Days is mandatory ");
                    dataEnteredRight = false;
                    scrollToError = lpgconnnectionexpiry;
                } else {

                    if (!(connectionExpiry.matches(regexExpiryDays))){
                        lpgconnnectionexpiry.setError(" Please enter a numeral ");
                        dataEnteredRight = false;
                        scrollToError = lpgconnnectionexpiry;
                    }
                }
                //Validations for last booked date to be entered
                String lastBookedDate = lpglastdatelabel.getText().toString();
                if(lastBookedDate.length() == 0){
                    lpglastdatelabel.setError(" Please select a date by clicking on the calendar icon ");
                    dataEnteredRight = false;
                    scrollToError = lpglastdatelabel ;
                }
                //Validation for Date to be entered in future


                //Validations for Agency to be mandatory field
                String connectionName = lpgConnection.getText().toString();
                if (connectionName.length() == 0 ){
                    lpgConnection.setError("Connection Name is Mandatory");
                    dataEnteredRight = false;
                    scrollToError = lpgConnection;
                }

                if (dataEnteredRight)
                {
                    ContentValues contentValuesDB = new ContentValues();
                    contentValuesDB.put(LPG_SQL_ContractClass.LPG_CONNECTION_ROW._ID, finalIDCount);
                    contentValuesDB.put(LPG_SQL_ContractClass.LPG_CONNECTION_ROW.CONNECTION_NAME, lpgConnection.getText().toString());
                    contentValuesDB.put(LPG_SQL_ContractClass.LPG_CONNECTION_ROW.PROVIDER, LPG_Utility.getLPGProvider(lpgProvider.getText().toString()));
                    contentValuesDB.put(LPG_SQL_ContractClass.LPG_CONNECTION_ROW.AGENCY, lpgAgency.getText().toString());
                    contentValuesDB.put(LPG_SQL_ContractClass.LPG_CONNECTION_ROW.AGENCY_LANDLINE_NUMBER, lpgDistributorPhoneNo.getText().toString());
                    contentValuesDB.put(LPG_SQL_ContractClass.LPG_CONNECTION_ROW.AGENCY_IVRS_NUMBER, lpgAgencyPhoneNo.getText().toString());
                    contentValuesDB.put(LPG_SQL_ContractClass.LPG_CONNECTION_ROW.AGENCY_SMS_NUMBER, lpgAgencySMSNo.getText().toString());
                    contentValuesDB.put(LPG_SQL_ContractClass.LPG_CONNECTION_ROW.CONNECTION_ID, lpgConnectionId.getText().toString());
                    contentValuesDB.put(LPG_SQL_ContractClass.LPG_CONNECTION_ROW.LAST_BOOKED_DATE, lpglastdatelabel.getText().toString());
                    contentValuesDB.put(LPG_SQL_ContractClass.LPG_CONNECTION_ROW.CONNECTION_EXPIRY_DAYS, lpgconnnectionexpiry.getText().toString());
                    //Insert to database if it is a new database
                    if (connectionIdString.equals(LPG_Utility.LPG_CONNECTION_ID) ) {

                        long insertRow = sqLiteDatabase.insert(LPG_SQL_ContractClass.LPG_CONNECTION_ROW.TABLE_NAME, null, contentValuesDB);
                        if (insertRow != -1) {

                            Alert.showDialogHelper("LPG Connection Created ", "OK", null, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    dialog.dismiss();
                                    //take user to home page
                                    Intent home = new Intent(getApplicationContext(), MainActivity.class);
                                    if (home.resolveActivity(getPackageManager()) != null) {
                                        startActivity(home);
                                    }

                                }
                            }, null);
                            Alert.show(getSupportFragmentManager(), "DB");
                        } else {
                            Alert.showDialogHelper("LPG Connection update failed. Please try later", "Ok", null, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();

                                }
                            }, null);
                            Alert.show(getSupportFragmentManager(), "DB");
                        }

                    } else {
                        int updateDBCount = sqLiteDatabase.update(LPG_SQL_ContractClass.LPG_CONNECTION_ROW.TABLE_NAME, contentValuesDB, LPG_SQL_ContractClass.LPG_CONNECTION_ROW._ID + " = " + finalIDCount, null);

                        if (updateDBCount > 0) {

                            Alert.showDialogHelper("LPG Connection updated", "Ok", null, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //clear local cache
                                    LPG_Utility.removeCacheLocalConnectionDetails(finalIDCount);
                                    buttonSave.setEnabled(true);
                                    dialog.dismiss();


                                }
                            }, null);
                            Alert.show(getSupportFragmentManager(), "DB");



                        } else {
                            Alert.showDialogHelper("LPG Connection update failed. Please try later", "Ok", null, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();

                                }
                            }, null);
                            Alert.show(getSupportFragmentManager(), "DB");
                        }

                    }


                    // This is the place where we set the alarms
                    // set alarms based on last confirmed date and expiry time
                    // get the last booked date and get the date which would be mid-way till expiry
                    // if the mid-way date is not in the past, then set an alarm on that day


                    LPG_Utility.RefillAlarmNotification[] alarmNotificationTimers = LPG_Utility.getRefillRemainder(getApplicationContext(),
                                    lpglastdatelabel.getText().toString() ,// Last booked date entered by user
                                    lpgconnnectionexpiry.getText().toString()  , //Connection expiry time
                                    finalIDCount,
                                    lpgConnection.getText().toString(),
                                    LPG_Utility.LPG_GET_REGULAR_ALARM_NOTIFICATION_DATES  // connection name
                            );
                    //for espresso test - assign values of calendars in test fields
                    test_alarm_midway = alarmNotificationTimers[0].getGregorialCalendar();
                    test_alarm_final_expiry = alarmNotificationTimers[1].getGregorialCalendar();

                            //  Set alarms based on alarms returned by getRefillRemainder method
                            AlarmManager alarmManager = (android.app.AlarmManager)getSystemService(Context.ALARM_SERVICE);
                            for ( int i = 0 ; i <2 ; i++) {

                                alarmManager.set(android.app.AlarmManager.RTC_WAKEUP, alarmNotificationTimers[i].getGregorialCalendar().getTimeInMillis(), alarmNotificationTimers[i].getRefillCylinder());


                            }

                    Log.v("Alarm","Last Booked date is " +  lpglastdatelabel.getText().toString() + " and expiry date is " +  lpgconnnectionexpiry.getText().toString() );
                    Log.v("Alarm", "Midway alarm date is " + LPG_Utility.getDateFromCalendar(alarmNotificationTimers[0].getGregorialCalendar()));
                    Log.v("Alarm", "Expiry alarm date is " + LPG_Utility.getDateFromCalendar(alarmNotificationTimers[1].getGregorialCalendar()));



                } else {
                    scrollView.scrollTo(0,scrollToError.getBottom());
                    buttonSave.setEnabled(true);
                }






            }
        });

        ImageButton lastBookedDate = (ImageButton) findViewById(R.id.btn_calendarimage);
        lastBookedDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BookedDateFragment datePicker = new BookedDateFragment();
                datePicker.show(getSupportFragmentManager(),"t");
            }
        });
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    protected void hideSoftTextPad(View v) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        //check if the key pad is visible
        if (inputMethodManager.isActive()) {
            inputMethodManager.hideSoftInputFromInputMethod(v.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        //Close the key board if being shown;

        InputMethodManager inputMethodManager =(InputMethodManager) getApplicationContext().getSystemService(INPUT_METHOD_SERVICE);

        if (inputMethodManager.isActive()) {

            View view = this.getCurrentFocus();
            if (view != null)
            {  inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
        }
    }

    return true;
}

    @Override
    public void onStart() {
        super.onStart();


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "AddLPGConnection Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.aekan.navya.lpgbooking/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "AddLPGConnection Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.aekan.navya.lpgbooking/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    @Override
    public void updateActivityWithLPGDetailsCursor(Cursor c) {

        SQLiteCursor dataCursor;
        if (c != null) {
            dataCursor = (SQLiteCursor) c;
        } else {
            Toast.makeText(getApplicationContext(), getResources().getText(R.string.connection_detail_missing), Toast.LENGTH_LONG).show();
            return;
        }
        //Get text boxes which would need to be updated
        /*lpgConnection.setText(dataCursor.getString(dataCursor.getColumnIndex(LPG_SQL_ContractClass.LPG_CONNECTION_ROW.CONNECTION_NAME)));
        lpgProvider.setText(dataCursor.getString(dataCursor.getColumnIndex(LPG_SQL_ContractClass.LPG_CONNECTION_ROW.PROVIDER)));
        lpgAgency.setText(dataCursor.getString(dataCursor.getColumnIndex(LPG_SQL_ContractClass.LPG_CONNECTION_ROW.AGENCY)));
        lpgAgencyPhoneNo.setText(dataCursor.getString(dataCursor.getColumnIndex(LPG_SQL_ContractClass.LPG_CONNECTION_ROW.AGENCY_IVRS_NUMBER)));
        lpgConnectionId.setText(dataCursor.getString(dataCursor.getColumnIndex(LPG_SQL_ContractClass.LPG_CONNECTION_ROW.CONNECTION_ID)));
        lpglastdatelabel.setText(dataCursor.getString(dataCursor.getColumnIndex(LPG_SQL_ContractClass.LPG_CONNECTION_ROW.LAST_BOOKED_DATE)));
        lpgconnnectionexpiry.setText(dataCursor.getString(cursor.getColumnIndex(LPG_SQL_ContractClass.LPG_CONNECTION_ROW.CONNECTION_EXPIRY_DAYS)));*/

        final EditText lpgConnection = (EditText) findViewById(R.id.add_lpgconnectionnameedittext);
        final EditText lpgProvider = (EditText) findViewById(R.id.add_provideredittext);
        final EditText lpgAgency = (EditText) findViewById(R.id.add_agencyedittext);
        final EditText lpgAgencyPhoneNo = (EditText) findViewById(R.id.add_agencyphoneedittext);
        final EditText lpgDistributorPhoneNo = (EditText) findViewById(R.id.add_distributorphonenumber_edittext);
        final EditText lpgAgencySMSNo = (EditText) findViewById(R.id.add_connectionsmsnumber);
        final EditText lpgConnectionId = (EditText) findViewById(R.id.add_connectionid);
        final EditText lpglastdatelabel = (EditText) findViewById(R.id.add_lastbookeddate);
        final EditText lpgconnnectionexpiry = (EditText) findViewById(R.id.add_connectionexpiry);

        //update the text boxes, if cursor is not null
        if (dataCursor.moveToFirst()){
            //Update the text boxes
            lpgConnection.setText(dataCursor.getString(dataCursor.getColumnIndex(LPG_SQL_ContractClass.LPG_CONNECTION_ROW.CONNECTION_NAME)));
            lpgProvider.setText(dataCursor.getString(dataCursor.getColumnIndex(LPG_SQL_ContractClass.LPG_CONNECTION_ROW.PROVIDER)));
            lpgAgency.setText(dataCursor.getString(dataCursor.getColumnIndex(LPG_SQL_ContractClass.LPG_CONNECTION_ROW.AGENCY)));
            lpgDistributorPhoneNo.setText(dataCursor.getString(dataCursor.getColumnIndex(LPG_SQL_ContractClass.LPG_CONNECTION_ROW.AGENCY_LANDLINE_NUMBER)));
            lpgAgencyPhoneNo.setText(dataCursor.getString(dataCursor.getColumnIndex(LPG_SQL_ContractClass.LPG_CONNECTION_ROW.AGENCY_IVRS_NUMBER)));
            lpgAgencySMSNo.setText(dataCursor.getString(dataCursor.getColumnIndex(LPG_SQL_ContractClass.LPG_CONNECTION_ROW.AGENCY_SMS_NUMBER)));
            lpgConnectionId.setText(dataCursor.getString(dataCursor.getColumnIndex(LPG_SQL_ContractClass.LPG_CONNECTION_ROW.CONNECTION_ID)));
            lpglastdatelabel.setText(dataCursor.getString(dataCursor.getColumnIndex(LPG_SQL_ContractClass.LPG_CONNECTION_ROW.LAST_BOOKED_DATE)));
            lpgconnnectionexpiry.setText(dataCursor.getString(dataCursor.getColumnIndex(LPG_SQL_ContractClass.LPG_CONNECTION_ROW.CONNECTION_EXPIRY_DAYS)));
        } else {
            Toast.makeText(getApplicationContext(),getResources().getText(R.string.connection_detail_missing),Toast.LENGTH_LONG).show();
        }
        //Update cursor values
        String connectionID = dataCursor.getString(dataCursor.getColumnIndex(LPG_SQL_ContractClass.LPG_CONNECTION_ROW._ID));

        LPG_Utility.setCacheLocalData(connectionID,dataCursor);



        //set animation enabled for the text input layout, enabling the animation after binding with activity
        //prevents jarring effect
        TextInputLayout connectionTextInput = (TextInputLayout) findViewById(R.id.add_lpgconnectionnamelabel);
        connectionTextInput.setHintAnimationEnabled(true);

    }

    @Override
    public void updatePrimaryKeyIncrement(String incrementedPrimaryKey) {
        finalIDCount = incrementedPrimaryKey;
        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.fab_save_connection);
        floatingActionButton.setEnabled(true);
    }

    public void updateAllConnectionData(Cursor c) {
    }

    public boolean showInterStitialAd() {
        boolean flagShowInterstitial = ifWeCanShowInterstitialAdNow();
        boolean showAd = mInterstitialAd.isLoaded() && flagShowInterstitial;

        if (showAd) {

            mInterstitialAd.show();

        }

        return showAd;

    }

    private void loadInterstitialAd() {
        mInterstitialAd = new InterstitialAd(getApplicationContext());
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.AdMob_InterstitialAd_AddLPGConnection));
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int errorCode) {
                Log.v("Ads", " Failed to Load Interstitial error " + errorCode);
            }

            @Override
            public void onAdLoaded() {
                Log.v("Ads", " Interstitial Ad Loaded ");
            }

            @Override
            public void onAdClosed() {
                Intent homeActivity = new Intent(getApplicationContext(), MainActivity.class);
                if (homeActivity.resolveActivity(getPackageManager()) != null) {
                    startActivity(homeActivity);
                }

            }


        });
        mInterstitialAd.loadAd(new AdRequest.Builder().addTestDevice(getResources().getString(R.string.AdMob_TestDevice)).build());
    }

    public static class BookedDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        // Instantiate an alert box class to give dialog
       // public LPG_AlertBoxClass lpg_alertBoxClass;



        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){
            //Create a date picker dialog
            final Calendar calendar = Calendar.getInstance();
            int currentDate = calendar.get(Calendar.DATE);
            int currentMonth = calendar.get(Calendar.MONTH);
            int currentYear = calendar.get(Calendar.YEAR);
//            return the DatePickerDialog instance
            return new DatePickerDialog(getContext(),this,currentYear,currentMonth,currentDate);
        }

        public void onDateSet(DatePicker datePicker,int setYear,int setMonth,int setDay){
            GregorianCalendar setDateG = new GregorianCalendar(setYear,setMonth,setDay);
            // Check if the date is in future
            // else, create an alarm to notify user mid way through booking expiry and one day before expiry

        //    SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
            Date date = setDateG.getTime();

//                Get system date
            Calendar calendar = Calendar.getInstance();

            if ((date.compareTo(calendar.getTime())) >= 0 ) {
                LPG_AlertBoxClass lpg_secAlertBox = new LPG_AlertBoxClass();
                lpg_secAlertBox.showDialogHelper("Invalid Last Booking Date","Ok",null, new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                },null);
                lpg_secAlertBox.show(getFragmentManager(),"DB");
            } else {


                EditText lastBookedDateView = (EditText) getActivity().findViewById(R.id.add_lastbookeddate);
                String selectedDate = Integer.toString(setDay) +  "/" + Integer.toString(setMonth + 1) + "/" + Integer.toString(setYear);

                lastBookedDateView.setText(selectedDate);



            }
        }
    }

}
