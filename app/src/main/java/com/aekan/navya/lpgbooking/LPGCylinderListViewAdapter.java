package com.aekan.navya.lpgbooking;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.BoolRes;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by arunramamurthy on 11/03/16.
 * Class to create LPGCylinderList.
 * This class will have the classes for RecyclerView viz. the ViewHolder and the adapter
 */
public class LPGCylinderListViewAdapter extends RecyclerView.Adapter<LPGCylinderListViewAdapter.LPGViewHolder> {

    //create a static class for the list of lpg cylinder
    private static class LPGCylinderListInfo {
        protected String LPGCylinderName;
        protected String LPGCylinderCompany;
        protected String LPGCylinderExpiry;
        protected String LPG_ROW_ID;

        //Constructor for LPG Cylinder list
        public LPGCylinderListInfo (String LPGCylinderNameInit, String LPGCylinderCompanyInit,String LPGCylinderExpiryInit, String LPGrowid) {
            LPGCylinderName = LPGCylinderNameInit;
            LPGCylinderCompany = LPGCylinderCompanyInit;
            LPGCylinderExpiry = LPGCylinderExpiryInit;
            LPG_ROW_ID = LPGrowid;
        }
    }

    //Create view holder class
    public static class LPGViewHolder extends RecyclerView.ViewHolder{
        //Create the view holder test
        protected TextView mARLPGName;
        protected TextView marLPGCompanyName;
        protected TextView marLPGExpiry;
        protected ImageButton marEditConnection;
        protected ImageButton mARDeleteConnection;

        public LPGViewHolder(View v){
            super(v);
            //assign the view holder elements in constructor
            mARLPGName = (TextView) v.findViewById(R.id.lpg_cylinder_name);
            marLPGCompanyName = (TextView) v.findViewById(R.id.lpg_cylinder_company);
            marLPGExpiry = (TextView) v.findViewById(R.id.lpg_expiry);
            marEditConnection = (ImageButton) v.findViewById(R.id.edit_connection_btn);
            mARDeleteConnection = (ImageButton) v.findViewById(R.id.delete_connection_btn);
        }

    }
    //have a fixed list of cylinders for now
    private final int LPG_CYLINDER_LIST_LENGTH = 4;

    //create a private enumeration for lpg cylinder
    private ArrayList<LPGCylinderListInfo> LPGCylinderList;

    //Create constructor for the adapter to initialise with
    public LPGCylinderListViewAdapter(){
        LPGCylinderList = new ArrayList<LPGCylinderListInfo>();
        //Initialise the list of values for the array
        for (int i=0;i<LPG_CYLINDER_LIST_LENGTH;i++)
        {
            //Initialise the Arraylist
            LPGCylinderList.add(new LPGCylinderListInfo("Cylinder no " + i,
                    "Cylinder Company is Cylinder " + i,
                    "Cylinder will expiry in " + i + " days", "NA"
            ));
        }
    }

    //Constructor using SQLiteCursor as input
    public LPGCylinderListViewAdapter(SQLiteCursor sqLiteCursor){
        //get count in cursor ;
        int cursorCount = sqLiteCursor.getCount();
        LPGCylinderList = new ArrayList<LPGCylinderListInfo>();
        Log.v("Initialisation","Cursor " + cursorCount);

        if (cursorCount == 0) {
//            case 0 :
            //create the Array list with notification messages to user to create new connections
            LPGCylinderList.add(new LPGCylinderListInfo("No Connections Found", "You can add your LPG connection now!!", "Just click on Add button below","NA"));
            Log.v("Initialisation",Integer.toString( LPGCylinderList.size()));
        }else{
//            default:
                //create the Array list from SQLiteCursor
                sqLiteCursor.moveToFirst();

                //get column count
                int columnCount = sqLiteCursor.getColumnCount();
                String lpgConnection, lpgAgency, lpgProvider,lpgRowId;
                while (sqLiteCursor.isAfterLast() != true   ){
                    {
                        lpgConnection = sqLiteCursor.getString(0);
                        lpgProvider = sqLiteCursor.getString(1);
                        lpgAgency = sqLiteCursor.getString(2);
                        lpgRowId = sqLiteCursor.getString(3);

                        LPGCylinderList.add( new LPGCylinderListInfo(lpgConnection,lpgProvider,lpgAgency,lpgRowId));

                        sqLiteCursor.moveToNext();
                    }


                }

        }

    }

    //Implement Create view
    @Override
    public LPGViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        //Create the view for this view
        View LPGListItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.lpg_card_view,parent, false);

        return (new LPGViewHolder(LPGListItem));


    }

    @Override
    public void onBindViewHolder(LPGViewHolder LVH, int i){
        //get the contact list for the LPG view holderc
        Log.v("Adapter on Bind", "Position " + i);
        final LPGCylinderListInfo CurrentRow = LPGCylinderList.get(i);
        Log.v("Adapter on Bind",CurrentRow.LPGCylinderName);
        Log.v("Adapter on Bind",CurrentRow.LPGCylinderCompany);
        Log.v("Adapter on Bind",CurrentRow.LPGCylinderExpiry);

        //Assign the text value for each of the item
        LVH.mARLPGName.setText(CurrentRow.LPGCylinderName);
        LVH.marLPGCompanyName.setText(CurrentRow.LPGCylinderCompany);
        LVH.marLPGExpiry.setText(CurrentRow.LPGCylinderExpiry);

        //Assign a onclick listener event for showing activity
        LVH.marEditConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("Edit Click","Clicked the view");
                Log.v("Edit Click","Value of Row id " + CurrentRow.LPG_ROW_ID);
                Intent intent = new Intent(v.getContext(),AddLPGConnection.class);
                Bundle bundle = new Bundle();
                CharSequence lpgRowId = CurrentRow.LPG_ROW_ID;
                intent.putExtra(LPG_SQL_ContractClass.LPG_CONNECTION_ROW.FIELD_CONNECTION_ID_EDIT,CurrentRow.LPG_ROW_ID);
                bundle.putCharSequence(LPG_SQL_ContractClass.LPG_CONNECTION_ROW.FIELD_CONNECTION_ID_EDIT,CurrentRow.LPG_ROW_ID);
                //intent.putExtra(LPG_SQL_ContractClass.LPG_CONNECTION_ROW.BUNDLE_NAME_EDIT_CONNECTION.toString(),bundle);
                Bundle checkb = intent.getExtras();
                Log.v("Edit Click","Bundle Value " +checkb.toString());
                v.getContext().startActivity(intent);

            }
        });

        //Assign a onClickListener to delete the connection
        // Also, when the connection is deleted, the associated alarms needs to be deleted as well
        LVH.mARDeleteConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Instruct the user to confirm if the connection needs to be deleted
                // If the user confirms as yes, cancel the connection
                // and delete the associated alarms with the connection
                final SQLiteDatabase sqLiteDatabase = new LPG_SQLOpenHelperClass(v.getContext()).getWritableDatabase();
                final Context context = v.getContext();
                final LPG_AlertBoxClass lpgDeleteConnection = new LPG_AlertBoxClass();
                final Toast toast = new Toast(v.getContext());
                final AlarmManager alarmManager = (AlarmManager) v.getContext().getSystemService(Context.ALARM_SERVICE);
                toast.setGravity(Gravity.CENTER_HORIZONTAL,0,0);
                toast.makeText(v.getContext(),"DUMMY",Toast.LENGTH_SHORT);
                lpgDeleteConnection.showDialogHelper("Do you want to delete this connection?"
                        , "Ok"
                        , "Cancel"
                        , new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // If the user says to delete the connection,
                                // delete the information from database
                                // delete the alarm
                                // give a toast to the user that the data has been deleted
                                String deleteCondition = LPG_SQL_ContractClass.LPG_CONNECTION_ROW._ID + " = ";
                                String[] deleteParameter = { CurrentRow.LPG_ROW_ID };
                                //delete the record
                                int deleteCount = sqLiteDatabase.delete(LPG_SQL_ContractClass.LPG_CONNECTION_ROW.TABLE_NAME,deleteCondition,deleteParameter);
                                // check no of deleted record
                                if ( deleteCount > 0 ) {
                                    //Delete the alarm set for this connection
                                    int pendingintentRequestCodeForAlarm = Integer.parseInt(CurrentRow.LPG_ROW_ID) * 10 + 1 ;
                                    Intent alarmCancellationIntent = new Intent(context,LPG_AlarmReceiver.class);
                                    PendingIntent alarmCancellationPendingIntent = PendingIntent.getBroadcast(context,pendingintentRequestCodeForAlarm,alarmCancellationIntent,0);
                                    alarmManager.cancel(alarmCancellationPendingIntent);
                                    // set a toast that the record has been deleted
                                    toast.setText(R.string.toast_delete_successful);
                                    toast.show();
                                } else {
                                    // set a toast that the record could not be deleted
                                    toast.setText(R.string.toast_delete_failure);
                                }

                                //close the connection for database
                                sqLiteDatabase.close();



                            }
                        }
                        , new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // just dismiss the dialog
                                lpgDeleteConnection.dismiss();
                            }
                        });

            }
        });

    }

    @Override
    public int getItemCount(){
        return LPGCylinderList.size();
    }
}
