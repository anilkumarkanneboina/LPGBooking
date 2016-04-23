package com.aekan.navya.lpgbooking;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.annotation.StringDef;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddLPGConnection extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lpgconnection);
        Toolbar toolbar = (Toolbar) findViewById(R.id.addlpg_toolbar);
        //setSupportActionBar(toolbar);
        //Set Navigation icon for the toolbar
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intentMainActivity = new Intent(getBaseContext(), MainActivity.class);
                //check if intent would resolve to an activity and then start the activity
                if (intentMainActivity.resolveActivity(getPackageManager()) != null) {
                    startActivity(intentMainActivity);
                }

            }


        });

        //Create values for edit text
        final EditText lpgConnection = (EditText) findViewById(R.id.add_lpgconnectionnameedittext);
        final EditText lpgProvider = (EditText) findViewById(R.id.add_provideredittext);
        final EditText lpgAgency = (EditText) findViewById(R.id.add_agencyedittext);

        //Set listener events for Save button and Cancel button.
        // To set listener events, initialize counter value for primary key ID;

        //create DB query to get counter value
        int iDCount=1;

       /* String[] sqlIDMax = {"MAX(" + LPG_SQL_ContractClass.LPG_CONNECTION_ROW._ID +") AS MAXID"};
        try {
        SQLiteCursor sqLiteCursorID = (SQLiteCursor) ((LPGApplication)getApplication()).LPGDB.query(LPG_SQL_ContractClass.LPG_CONNECTION_ROW.TABLE_NAME,sqlIDMax,null,null,null,null,null);

        if (sqLiteCursorID.getCount() == 1) {
            iDCount = 1;
        } else {
            try {
                String strIDCount = sqLiteCursorID.getString(0);
                iDCount = Integer.parseInt(strIDCount) + 1;
            } catch (SQLiteException se) {
                Log.v("SQLiteException", "SQL Invalid");

            }
        } }catch(SQLiteException se){
                if (iDCount == 0 ){
                    iDCount = 1;
                }

            }


*/
        final String finalIDCount = Integer.toString(iDCount);
        //Get the database
        final SQLiteDatabase sqLiteDatabase =  ((LPGApplication) getApplication()).LPGDB;

        //Set onclick listener for Save button ;

        Button buttonSave = (Button) findViewById(R.id.save_button);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues contentValuesDB = new ContentValues();
                contentValuesDB.put(LPG_SQL_ContractClass.LPG_CONNECTION_ROW._ID,finalIDCount);
                contentValuesDB.put(LPG_SQL_ContractClass.LPG_CONNECTION_ROW.CONNECTION_NAME,lpgConnection.getText().toString());
                contentValuesDB.put(LPG_SQL_ContractClass.LPG_CONNECTION_ROW.PROVIDER,lpgProvider.getText().toString());
                contentValuesDB.put(LPG_SQL_ContractClass.LPG_CONNECTION_ROW.AGENCY,lpgAgency.getText().toString());

                long insertRow = sqLiteDatabase.insert(LPG_SQL_ContractClass.LPG_CONNECTION_ROW.TABLE_NAME,null,contentValuesDB);
                if (insertRow != -1){
                    ((LPGApplication) getApplication()).LPG_Alert.showDialogHelper("LPG Connection Created ", "OK", null, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    },null);
                }
                ((LPGApplication) getApplication()).LPG_Alert.show(getFragmentManager(),"DB");
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}