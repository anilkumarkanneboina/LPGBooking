package com.aekan.navya.lpgbooking;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.aekan.navya.lpgbooking.utilities.LPG_SQL_ContractClass;
import com.aekan.navya.lpgbooking.utilities.LPG_Utility;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements NavigationAdapter.ListenerAdapter {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
   private GoogleApiClient client;
    private HashMap<Integer, AdapterView.OnItemClickListener> mListenerAdapter;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle navDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Add details for LPG Connection", Snackbar.LENGTH_LONG)
                        .setAction("+", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                //create an intent for add lpg connection activity
                                Intent intentLPGAdd = new Intent(getApplicationContext(), AddLPGConnection.class);
                                intentLPGAdd.putExtra(LPG_SQL_ContractClass.LPG_CONNECTION_ROW.FIELD_CONNECTION_ID_EDIT, LPG_Utility.LPG_CONNECTION_ID);
                                startActivity(intentLPGAdd);
                            }


                        }).show();
            }

            //Test commit - dummy comment
        });

        //Set Drawer layout
        mDrawerLayout = (DrawerLayout) findViewById(R.id.navdrawer);
        Log.v("Main", "Drawer layout bar set");

        //Set recycler view, by initialising the adapter
        RecyclerView recyclerViewNavigation = (RecyclerView) findViewById(R.id.nav_recyclerview);
        recyclerViewNavigation.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewNavigation.setLayoutManager(linearLayoutManager);
        NavigationAdapter navigationAdapter = new NavigationAdapter(getApplicationContext(), this);
        recyclerViewNavigation.setAdapter(navigationAdapter);


        //Enable toggle action button for drawer layout
        //toolbar.setNavigationIcon(R.drawable.ic_menu_48);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        //set Drawer shadow
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        //set drawer toggle action listerener
        navDrawerToggle = new ActionBarDrawerToggle(this,//current activity
                mDrawerLayout, //drawer layout
                toolbar,
                R.string.navdrawer_open,
                R.string.navdrawer_close
        );
        mDrawerLayout.setDrawerListener(navDrawerToggle);

        ///////////////////////////////////////
        //Create recycler view and initialize it
        //Create the query for database and set up recycler view
        ///////////////////////////////////////

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.lpg_recycler_view);
        //set recycler view to have same size
        recyclerView.setHasFixedSize(true);
        //set layout manager
        LinearLayoutManager LPGLinearLayoutMgr = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(LPGLinearLayoutMgr);
        //Set view holder adapter
      //  recyclerView.setAdapter(new LPGCylinderListViewAdapter());

        if (!(((LPGApplication) getApplication()).isSingletonRun )) {
            ((LPGApplication) getApplication()).LPG_AlertBoxInstantiate();
        }
        //Columns for database ;
        String[] sqLiteColumns = {LPG_SQL_ContractClass.LPG_CONNECTION_ROW.CONNECTION_NAME, LPG_SQL_ContractClass.LPG_CONNECTION_ROW.PROVIDER, LPG_SQL_ContractClass.LPG_CONNECTION_ROW.AGENCY, LPG_SQL_ContractClass.LPG_CONNECTION_ROW._ID};
        SQLiteCursor sqLiteCursor;

        SQLiteDatabase sqLiteDatabase = ((LPGApplication) getApplication()).LPGDB;

        if (sqLiteDatabase == null) {

            Log.v("MainActivity", "SQL DB does not exist");
        }

        sqLiteCursor = (SQLiteCursor) ((LPGApplication) getApplication()).LPGDB.query(LPG_SQL_ContractClass.LPG_CONNECTION_ROW.TABLE_NAME,
                sqLiteColumns,
                null,
                null,
                null,
                null,
                null
                );


        recyclerView.setAdapter(new LPGCylinderListViewAdapter(sqLiteCursor));
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }*/

    /*@Override*/
   /* public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
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
                "Main Page", // TODO: Define a title for the content shown.
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

    public HashMap<Integer, View.OnClickListener> getmListenerAdapter() {
        HashMap<Integer, View.OnClickListener> listenerHashMap = new HashMap<Integer, View.OnClickListener>();
        //Listener adapter for Header ;
        listenerHashMap.put(new Integer(0), null);

        //Listener adapter for Home screen
        listenerHashMap.put(new Integer(1), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
            }
        });

        //listener adapter for add lpg connection
        listenerHashMap.put(new Integer(2), new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent addLPGIntent = new Intent(getApplicationContext(), AddLPGConnection.class);
                addLPGIntent.putExtra(LPG_SQL_ContractClass.LPG_CONNECTION_ROW.FIELD_CONNECTION_ID_EDIT, LPG_Utility.LPG_CONNECTION_ID);
                startActivity(addLPGIntent);

            }
        });

        //listerner adapter for Phone booking registration
        listenerHashMap.put(new Integer(3), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addLPGIntent = new Intent(getApplicationContext(), PhoneBookingRegistration.class);
                startActivity(addLPGIntent);

            }
        });

        //listerner adapter for SMS booking registration
        listenerHashMap.put(new Integer(4), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addLPGIntent = new Intent(getApplicationContext(), SMSBookingRegistration.class);
                startActivity(addLPGIntent);

            }
        });
        //listener adapter for FAQs booking registratino
        listenerHashMap.put(new Integer(5), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addLPGIntent = new Intent(getApplicationContext(), FAQs.class);
                startActivity(addLPGIntent);

            }
        });

        return listenerHashMap;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //handle necessary actions for navigation drawer
        if (navDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        //call action bar toggle object with new configuration
        super.onConfigurationChanged(newConfig);
        //call appropriate method from navigation drawer
        Log.v("Drawer", "Inside Configuration changed");
        navDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        Log.v("Drawer", "Inside Post Create");
        navDrawerToggle.syncState();
    }


}
