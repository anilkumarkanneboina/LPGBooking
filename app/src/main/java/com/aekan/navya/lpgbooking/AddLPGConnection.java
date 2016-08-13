package com.aekan.navya.lpgbooking;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.StringDef;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Pattern;

public class AddLPGConnection extends AppCompatActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Activity creation using the saved bundle

        //Call onCreate function of super class
        super.onCreate(savedInstanceState);
        //set content view for the activity
        setContentView(R.layout.activity_add_lpgconnection);
        //Get database to do CRUD operations
        SQLiteDatabase db = ((LPGApplication) getApplication()).LPGDB ;
        if (db == null ){
            Log.v("DB","DB Is null");
        }
        // Set the holder to have connection id string
        String connectionPrimaryKey = null;
        // Set REGEX strings for data validation
        final String regexNumber = "[0-9]{5,15}+";
        final String regexExpiryDays = "[0-9]+";
        final String regexMandatory = "[]+";
        //Instantiate the tool bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.addlpg_toolbar);
        //setSupportActionBar(toolbar);
        //Set Navigation icon for the toolbar
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentMainActivity = new Intent(getApplicationContext(), MainActivity.class);
                //check if intent would resolve to an activity and then start the activity
                if (intentMainActivity.resolveActivity(getPackageManager()) != null) {
                    startActivity(intentMainActivity);
                }

            }


        });
        toolbar.setTitle("Add Connection");

        //set keyboard behaviour
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        //Create values for edit text
        final EditText lpgConnection = (EditText) findViewById(R.id.add_lpgconnectionnameedittext);
        final EditText lpgProvider = (EditText) findViewById(R.id.add_provideredittext);
        final EditText lpgAgency = (EditText) findViewById(R.id.add_agencyedittext);
        final EditText lpgAgencyPhoneNo = (EditText) findViewById(R.id.add_agencyphoneedittext);
        final EditText lpgConnectionId = (EditText) findViewById(R.id.add_connectionid);
        final EditText lpglastdatelabel = (EditText) findViewById(R.id.add_lastbookeddate);
        final EditText lpgconnnectionexpiry = (EditText) findViewById(R.id.add_connectionexpiry);
        lpglastdatelabel.setEnabled(false);
        final ScrollView scrollView = (ScrollView) findViewById(R.id.form_scroll_view);
        //Set listener events for Save button and Cancel button.
        // To set listener events, initialize counter value for primary key ID;

        //Set validators for phone no and expiry dates
        lpgAgencyPhoneNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String phoneNo = lpgAgencyPhoneNo.getText().toString();

                if (!(phoneNo.matches(regexNumber))){
                    lpgAgencyPhoneNo.setError(" Please enter a numeral only, without any special characters");
                }
            }
        });


        lpgconnnectionexpiry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String phoneNo = lpgconnnectionexpiry.getText().toString();

                if (!(phoneNo.matches(regexExpiryDays))){
                    lpgconnnectionexpiry.setError(" Please enter a numeral only, without any special characters");
                }
            }
        });



        //Load the page with data, and initialise id counters based on
        // intent filter being passed to the activity
        final Bundle connectionBundle = getIntent().getExtras();
        final String connectionIdString =  getIntent().getStringExtra(LPG_SQL_ContractClass.LPG_CONNECTION_ROW.FIELD_CONNECTION_ID_EDIT);
//        Log.v("String Value ", connectionIdString);


        if (connectionIdString != null) {
            //get the connection id associated with the bundle
            //display the contents of the retrieved connection record in the fields

            //CharSequence connectionId = connectionBundle.getCharSequence(LPG_SQL_ContractClass.LPG_CONNECTION_ROW.FIELD_CONNECTION_ID_EDIT, LPG_SQL_ContractClass.LPG_CONNECTION_ROW.VALUE_CONNECTION_ID_NULL);
            //prepare to query the database
            String[] columnList = {LPG_SQL_ContractClass.LPG_CONNECTION_ROW.CONNECTION_NAME,
                    LPG_SQL_ContractClass.LPG_CONNECTION_ROW.PROVIDER,
                    LPG_SQL_ContractClass.LPG_CONNECTION_ROW.AGENCY,
                    LPG_SQL_ContractClass.LPG_CONNECTION_ROW.AGENCY_PHONE_NUMBER,
                    LPG_SQL_ContractClass.LPG_CONNECTION_ROW.CONNECTION_ID,
                    LPG_SQL_ContractClass.LPG_CONNECTION_ROW.LAST_BOOKED_DATE,
                    LPG_SQL_ContractClass.LPG_CONNECTION_ROW.CONNECTION_EXPIRY_DAYS};

            // String[] selectionArgs = {connectionId.toString()};
            //query the database
            assert db != null;
            Cursor cursor = db.query(LPG_SQL_ContractClass.LPG_CONNECTION_ROW.TABLE_NAME, columnList, LPG_SQL_ContractClass.LPG_CONNECTION_ROW._ID + " = " + connectionIdString, null, null, null, null, null);

            if (cursor.getCount() > 1) {
                Log.v("EditConnection", "More than one connection id");
            }
            //Set Values for edit text fields
            cursor.moveToFirst();
            Log.v("EditConnnection",Integer.toString(cursor.getColumnIndex(LPG_SQL_ContractClass.LPG_CONNECTION_ROW.CONNECTION_NAME)));
            lpgConnection.setText(cursor.getString(cursor.getColumnIndex(LPG_SQL_ContractClass.LPG_CONNECTION_ROW.CONNECTION_NAME)));
            lpgProvider.setText(cursor.getString(cursor.getColumnIndex(LPG_SQL_ContractClass.LPG_CONNECTION_ROW.PROVIDER)));
            lpgAgency.setText(cursor.getString(cursor.getColumnIndex(LPG_SQL_ContractClass.LPG_CONNECTION_ROW.AGENCY)));
            lpgAgencyPhoneNo.setText(cursor.getString(cursor.getColumnIndex(LPG_SQL_ContractClass.LPG_CONNECTION_ROW.AGENCY_PHONE_NUMBER)));
            lpgConnectionId.setText(cursor.getString(cursor.getColumnIndex(LPG_SQL_ContractClass.LPG_CONNECTION_ROW.CONNECTION_ID)));
            lpglastdatelabel.setText(cursor.getString(cursor.getColumnIndex(LPG_SQL_ContractClass.LPG_CONNECTION_ROW.LAST_BOOKED_DATE)));
            lpgconnnectionexpiry.setText(cursor.getString(cursor.getColumnIndex(LPG_SQL_ContractClass.LPG_CONNECTION_ROW.CONNECTION_EXPIRY_DAYS)));
            //create DB query to get counter value
            connectionPrimaryKey = connectionIdString;
        } else {
            // set primary key vallue for connection id
            String[] connectionID = {LPG_SQL_ContractClass.LPG_CONNECTION_ROW._ID};
            Cursor cursorID = db.query(LPG_SQL_ContractClass.LPG_CONNECTION_ROW.TABLE_NAME,connectionID,null,null,null,null,null);
            Log.v("AddConnnection ", "Cursor count before records creation " + Integer.toString(cursorID.getCount()));
           // Log.v("AddConnection Cursor "," getColumnIndex " + Integer.toString(cursorID.getColumnIndex(LPG_SQL_ContractClass.LPG_CONNECTION_ROW._ID)));
            //Log.v("AddConnection Cursor "," getString " + cursorID.getString(0));

            if (cursorID.getCount() == 0){
                Log.v("AddConnection ", "Initial cursor count");
                connectionPrimaryKey = "1";

            }else {
                //get max value for connection primary key
                try {
                    int counterPrimaryKey = 1;
                    cursorID.moveToFirst();
                    Log.v("AddConnection ",Integer.toString(cursorID.getCount()));
                    for (int i = 0; i < cursorID.getCount(); ++i) {
                        Log.v("AddConnection ","Inside cursor max iteration - counter value " + Integer.toString(i) + " Cursor length " + Integer.toString(cursorID.getCount())+ " Row id value " + cursorID.getString(cursorID.getColumnIndex(LPG_SQL_ContractClass.LPG_CONNECTION_ROW._ID)) );
                        int valueOfPrimaryKeyFromCounter = Integer.parseInt(cursorID.getString(cursorID.getColumnIndex(LPG_SQL_ContractClass.LPG_CONNECTION_ROW._ID)));
                        if (valueOfPrimaryKeyFromCounter > counterPrimaryKey) {
                            counterPrimaryKey = valueOfPrimaryKeyFromCounter;
                        }
                        cursorID.moveToNext();
                    }
                    connectionPrimaryKey = Integer.toString(counterPrimaryKey+1);
                }
                    catch (android.database.CursorIndexOutOfBoundsException cursorIndex ){
                        Log.v("AddConnection ", "Inside cursor bounds exception");
                        connectionPrimaryKey = "1";
                    }
            }
    }


        final String finalIDCount = connectionPrimaryKey; // Integer.toString(iDCount);
        //Get the database
        final SQLiteDatabase sqLiteDatabase = ((LPGApplication) getApplication()).LPGDB;

        //Set onclick listener for Save button ;

        FloatingActionButton buttonSave = (FloatingActionButton) findViewById(R.id.fab_save_connection);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get validations for all fields done before submission;
                boolean dataEnteredRight = true;
                int yScrollPosition = 0;

                //Validations for connection expiry to be mandatory field
                String connectionExpiry = lpgconnnectionexpiry.getText().toString();
                if ( connectionExpiry.length() == 0 ){
                    lpgconnnectionexpiry.setError(" Expiry Days is mandatory ");
                    dataEnteredRight = false;
                    yScrollPosition = 350;
                } else {

                    if ( !(connectionExpiry.matches(regexExpiryDays)) ){
                        lpgconnnectionexpiry.setError(" Please enter a numeral ");
                        dataEnteredRight = false;
                        yScrollPosition = 350;
                    }
                }
                //Validations for last booked date to be entered
                String lastBookedDate = lpglastdatelabel.getText().toString();
                if(lastBookedDate.length() == 0){
                    lpglastdatelabel.setError(" Please select a date by clicking on the calendar icon ");
                    dataEnteredRight = false;
                    yScrollPosition = 280;
                }
                //Validation for Date to be entered in future


                //Validations for Agency to be mandatory field
                String connectionName = lpgConnection.getText().toString();
                if (connectionName.length() == 0 ){
                    lpgConnection.setError("Connection Name is Mandatory");
                    dataEnteredRight = false;
                    yScrollPosition = 0;
                }

                if (dataEnteredRight == true)
                {
                    ContentValues contentValuesDB = new ContentValues();
                    contentValuesDB.put(LPG_SQL_ContractClass.LPG_CONNECTION_ROW._ID, finalIDCount);
                    contentValuesDB.put(LPG_SQL_ContractClass.LPG_CONNECTION_ROW.CONNECTION_NAME, lpgConnection.getText().toString());
                    contentValuesDB.put(LPG_SQL_ContractClass.LPG_CONNECTION_ROW.PROVIDER, lpgProvider.getText().toString());
                    contentValuesDB.put(LPG_SQL_ContractClass.LPG_CONNECTION_ROW.AGENCY, lpgAgency.getText().toString());
                    contentValuesDB.put(LPG_SQL_ContractClass.LPG_CONNECTION_ROW.AGENCY_PHONE_NUMBER, lpgAgencyPhoneNo.getText().toString());
                    contentValuesDB.put(LPG_SQL_ContractClass.LPG_CONNECTION_ROW.CONNECTION_ID, lpgConnectionId.getText().toString());
                    contentValuesDB.put(LPG_SQL_ContractClass.LPG_CONNECTION_ROW.LAST_BOOKED_DATE, lpglastdatelabel.getText().toString());
                    contentValuesDB.put(LPG_SQL_ContractClass.LPG_CONNECTION_ROW.CONNECTION_EXPIRY_DAYS, lpgconnnectionexpiry.getText().toString());
                    //Insert to database if it is a new database
                    if (connectionIdString == null) {

                        long insertRow = sqLiteDatabase.insert(LPG_SQL_ContractClass.LPG_CONNECTION_ROW.TABLE_NAME, null, contentValuesDB);
                        if (insertRow != -1) {
                            ((LPGApplication) getApplication()).LPG_Alert.showDialogHelper("LPG Connection Created ", "OK", null, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }, null);
                        }
                        ((LPGApplication) getApplication()).LPG_Alert.show(getFragmentManager(), "DB");
                    } else {
                        int updateDBCount = sqLiteDatabase.update(LPG_SQL_ContractClass.LPG_CONNECTION_ROW.TABLE_NAME, contentValuesDB, LPG_SQL_ContractClass.LPG_CONNECTION_ROW._ID + " = " + finalIDCount, null);
                        Log.v("EditConnection "," Update DB Count " + Integer.toString(updateDBCount));
                        if (updateDBCount > 0) {
                            Log.v("EditConnection"," Inside udpate count dialog caller");
                            ((LPGApplication) getApplication()).LPG_Alert.showDialogHelper("LPG Connection updated", "Ok", null, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }, null);
                            ((LPGApplication)getApplication()).LPG_Alert.show(getFragmentManager(),"DB");
                        }

                    }
                    // set alarms based on last confirmed date and expiry time
                    // get the last booked date and get the date which would be mid-way till expiry
                    // if the mid-way date is not in the past, then set an alarm on that day

                    //Get the date object from the lastdatelabel text field, by splitting the string and creating a date
                    String[] strDateFields = lpglastdatelabel.getText().toString().split("/");
                    int lpglastbookeddate, lpglastbookedmonth, lpglastbookedyear;
                    Calendar sysDate = Calendar.getInstance();
                    Date lpgLastBooked;
                    GregorianCalendar lpgLastBookedGregCalendar;
                    int lpgExpiryDays = Integer.getInteger(lpgconnnectionexpiry.getText().toString());


                    if (strDateFields.length != 0 ){

                        // From the split string, get the integer values for date, month and year fields
                        // and create a date object with these values
                        lpglastbookeddate= Integer.getInteger(strDateFields[1]);
                        lpglastbookedmonth = Integer.getInteger(strDateFields[0]);
                        lpglastbookedyear = Integer.getInteger(strDateFields[2]);
                        //Create the AlarmManager object to set alarms
                        AlarmManager AlarmManager = (android.app.AlarmManager)getSystemService(Context.ALARM_SERVICE);
//                        String selectedDate = Integer.toString(setMonth + 1) + "/" + Integer.toString(setDay) + "/" + Integer.toString(setYear);
                        // Create the gregorian calendar based on the last booked date
                        // add half of expected expiry time to this date, to arrive at mid-way expiry date
                        lpgLastBookedGregCalendar = new GregorianCalendar(lpglastbookedyear,lpglastbookedmonth - 1, lpglastbookeddate);
                        GregorianCalendar midwayExpiryDate = lpgLastBookedGregCalendar;
                        midwayExpiryDate.add(Calendar.DATE, Math.round( lpgExpiryDays/2));
                        Date lpgMidWayExpiryDate = midwayExpiryDate.getTime();

                        //compare this midway expiry time with sys date and
//                        create an alarm only if this date is in future

                        if ( sysDate.compareTo(midwayExpiryDate) <= 0  ) {
                            //set the alarm for this date with the notification class
                            Intent notificationIntent = new Intent(getApplicationContext(),LPG_AlarmReceiver.class);
                            PendingIntent notificationPendingIntent = PendingIntent.getBroadcast(getApplicationContext(),0,notificationIntent,0);
                            midwayExpiryDate.set(Calendar.HOUR_OF_DAY,12);
                            midwayExpiryDate.set(Calendar.MINUTE,1);

                            AlarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,2000,notificationPendingIntent);
                            Log.v("Alarm", "Alarm Set for  " + midwayExpiryDate.toString());





                        }

                    }


                }
                else {
                   scrollView.scrollTo(0,yScrollPosition);
                }
            }
        });
      /*  FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.

//        set onclick listener for image button
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

    @Override
    public boolean onTouchEvent(MotionEvent event){
        //Close the key board if being shown;
        Log.v("Touch","Within OnTouchEvent" );
        InputMethodManager inputMethodManager =(InputMethodManager) getApplicationContext().getSystemService(INPUT_METHOD_SERVICE);

        if (inputMethodManager.isActive()) {
            Log.v("Touch","Keypad is active");
            View view = this.getCurrentFocus();
            //* if (view != null)*//* {
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

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
            Date date = setDateG.getTime();

//                Get system date
            Calendar calendar = Calendar.getInstance();

            if ((date.compareTo(calendar.getTime())) <= 0 ) {
                LPG_secAlertBox lpg_secAlertBox = new LPG_secAlertBox();
                lpg_secAlertBox.showDialogHelper("Invalid Last Booking Date","Ok",null, new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                },null);
                lpg_secAlertBox.show(getFragmentManager(),"DB");
            } else {


                EditText lastBookedDateView = (EditText) getActivity().findViewById(R.id.add_lastbookeddate);
                String selectedDate = Integer.toString(setMonth + 1) + "/" + Integer.toString(setDay) + "/" + Integer.toString(setYear);
                Log.v("DateSelected", selectedDate);
                lastBookedDateView.setText(selectedDate);



            }
        }
    }

    public static class LPG_secAlertBox extends DialogFragment {
        //create members to support Dialog options;
        public String ALERTDIALOGTITLE;
        public String SETPOSITIVETEXT;
        public String SETNEGATIVETEXT;
        public DialogInterface.OnClickListener SETPOSITIVECLICKLISTENER;
        public DialogInterface.OnClickListener SETNEGATIVECLICKLISTENER;

        public Dialog onCreateDialog(Bundle SavedInstance){
            //Create the dialog box
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            //set dialog Title, positive and negative strings;
            if (SETNEGATIVECLICKLISTENER != null) {
                builder.setTitle(ALERTDIALOGTITLE).setPositiveButton(SETPOSITIVETEXT, SETPOSITIVECLICKLISTENER).setNegativeButton(SETNEGATIVETEXT, SETNEGATIVECLICKLISTENER);
            }
            else {
                builder.setTitle(ALERTDIALOGTITLE).setNeutralButton(SETPOSITIVETEXT,SETPOSITIVECLICKLISTENER);
            }
            //return the Dialog box;
            return builder.create();
        }

        public void showDialogHelper(String Title, String PositiveText, String NegativeText, DialogInterface.OnClickListener PositiveClickListener,DialogInterface.OnClickListener NegativeClickListener){
            //Set the positive and negative texts
            ALERTDIALOGTITLE = Title;
            SETPOSITIVETEXT = PositiveText;
            SETNEGATIVETEXT = NegativeText;
            SETPOSITIVECLICKLISTENER = PositiveClickListener;
            SETNEGATIVECLICKLISTENER = NegativeClickListener;

            //Show the dialog;
            // super.show(getFragmentManager(),"Dialog");


        }

    }

}
