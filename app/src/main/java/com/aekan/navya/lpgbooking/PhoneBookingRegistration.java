package com.aekan.navya.lpgbooking;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Messenger;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.aekan.navya.lpgbooking.utilities.LPGDataAPI;
import com.aekan.navya.lpgbooking.utilities.LPGServiceCallBackHandler;
import com.aekan.navya.lpgbooking.utilities.LPGServiceResponseCallBack;
import com.aekan.navya.lpgbooking.utilities.LPG_SQL_ContractClass;

import java.util.HashMap;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //call super class
        super.onCreate(savedInstanceState);
        //inflate
        setContentView(R.layout.phone_registration);
        //Configure tool bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.phoneregistration_toolbar);

        toolbar.setTitle(R.string.phonebooking_activity_title);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homeActivity = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(homeActivity);
            }
        });
        setSupportActionBar(toolbar);

        //Disable registration button
        mbuttonRegister = (Button) findViewById(R.id.reg_button);
        mbuttonRegister.setEnabled(false);

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

        }


        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            //Get selected item
            String connectionName;
            //get views from layout which need to be initialised
            TextView provider = (TextView) findViewById(R.id.reg_provider);
            TextView agency = (TextView) findViewById(R.id.reg_agency);
            TextView phonenumber = (TextView) findViewById(R.id.reg_no_textfield);


            // get adapter
            ArrayAdapter<String> parentAdapter = (ArrayAdapter<String>) parent.getAdapter();
            connectionName = parentAdapter.getItem(position);
            //update text fields based on the connection name selected
            // by iterating connection name within the cursor and getting connection details
            // from cursor
            for (int i = 0; i < mCursor.getCount(); ++i) {
                if (mCursor.getString(mCursor.getColumnIndex(LPG_SQL_ContractClass.LPG_CONNECTION_ROW.CONNECTION_NAME)).equals(connectionName)) {
                    provider.setText(mCursor.getString(mCursor.getColumnIndex(LPG_SQL_ContractClass.LPG_CONNECTION_ROW.PROVIDER)));
                    provider.setText(mCursor.getString(mCursor.getColumnIndex(LPG_SQL_ContractClass.LPG_CONNECTION_ROW.AGENCY)));
                    provider.setText(mCursor.getString(mCursor.getColumnIndex(LPG_SQL_ContractClass.LPG_CONNECTION_ROW.AGENCY_SMS_NUMBER)));

                }

            }


        }

        public void onNothingSelected(AdapterView<?> parent) {
        }

    }


}
