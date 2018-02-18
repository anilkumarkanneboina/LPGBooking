package com.aekan.navya.lpgbooking;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.icu.util.Currency;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aekan.navya.lpgbooking.purchase.LPG_Purchase_Utility;
import com.aekan.navya.lpgbooking.utilities.LPG_AlertBoxClass;
import com.aekan.navya.lpgbooking.utilities.LPG_SQLOpenHelperClass;
import com.aekan.navya.lpgbooking.utilities.LPG_SQL_ContractClass;
import com.aekan.navya.lpgbooking.utilities.LPG_Utility;
import com.aekan.navya.lpgbooking.utilities.lpgconnectionparcel;

import java.util.ArrayList;

import static com.aekan.navya.lpgbooking.utilities.LPG_Utility.CAP_EXPIRYSTATUS_GREEN;
import static com.aekan.navya.lpgbooking.utilities.LPG_Utility.CAP_EXPIRYSTATUS_ORANGE;

/**
 * Created by arunramamurthy on 11/03/16.
 * Class to create LPGCylinderList.
 * This class will have the classes for RecyclerView viz. the ViewHolder and the adapter
 */
public class LPGCylinderListViewAdapter extends RecyclerView.Adapter<LPGCylinderListViewAdapter.LPGViewHolder> {

    //public constant to mark no lpg cylinders have been added
    public final String NOLPGCYLINDERSADDED = "No cylinders have been added";
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
                    "Cylinder will expiry in " + i + " days", "NA","NA","NA"
            ));
        }
    }

    //Constructor using SQLiteCursor as input
    public LPGCylinderListViewAdapter(SQLiteCursor sqLiteCursor){
        //get count in cursor ;
        int cursorCount = sqLiteCursor.getCount();
        LPGCylinderList = new ArrayList<LPGCylinderListInfo>();


        if (cursorCount == 0) {
//            case 0 :
            //create the Array list with notification messages to user to create new connections
            LPGCylinderList.add(new LPGCylinderListInfo("No Connections Found", "You can add your LPG connection now!!", "Just click on Add button below", NOLPGCYLINDERSADDED,"NA","0"));

        }else{
//            default:
                //create the Array list from SQLiteCursor
                sqLiteCursor.moveToFirst();

                //get column count
                int columnCount = sqLiteCursor.getColumnCount();
                String lpgConnection, lpgAgency, lpgProvider,lpgRowId,lpgLastBookedDate,lpgExpiryDays;
                while (sqLiteCursor.isAfterLast() != true   ){
                    {
                        lpgConnection = sqLiteCursor.getString(0);
                        lpgProvider = sqLiteCursor.getString(1);
                        lpgAgency = sqLiteCursor.getString(2);
                        lpgRowId = sqLiteCursor.getString(3);
                        lpgLastBookedDate = sqLiteCursor.getString(sqLiteCursor.getColumnIndex(LPG_SQL_ContractClass.LPG_CONNECTION_ROW.LAST_BOOKED_DATE));
                        lpgExpiryDays = sqLiteCursor.getString(sqLiteCursor.getColumnIndex(LPG_SQL_ContractClass.LPG_CONNECTION_ROW.CONNECTION_EXPIRY_DAYS));

                        LPGCylinderList.add(new LPGCylinderListInfo(lpgConnection, lpgProvider, lpgAgency, lpgRowId,lpgLastBookedDate,lpgExpiryDays));

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
    public void onBindViewHolder(LPGViewHolder LVH, int i) {
        //get the contact list for the LPG view holderc

        final LPGCylinderListInfo CurrentRow = LPGCylinderList.get(i);


        //Assign the text value for each of the item
        LVH.mARLPGName.setText(CurrentRow.LPGCylinderName);
        LVH.marLPGCompanyName.setText(CurrentRow.LPGCylinderCompany);
        LVH.marLPGExpiry.setText(CurrentRow.LPGCylinderExpiry);
        Log.v("Expiry Days " , Integer.toString( i));
        Log.v("Expiry Days ", CurrentRow.getLPGExpiryDays());
        //set color for drawable
        int expiryStatusParams = LPG_Utility.getExpiryStatus(CurrentRow.getLPGLastBookedDate(),CurrentRow.getLPGExpiryDays());
        if (expiryStatusParams == LPG_Utility.EXPIRY_INVALID){
            LVH.mStatusIndicator.setVisibility(View.INVISIBLE);
        } else{
            //set percentage for progress bar
            //int progress= expiryStatusParams.getExpiryPercent();
            LVH.mStatusIndicator.setProgress(expiryStatusParams);
            LayerDrawable progressDrawable = (LayerDrawable) LVH.mStatusIndicator.getProgressDrawable();
            ClipDrawable shapeDrawable = (ClipDrawable) progressDrawable.getDrawable(2);
            shapeDrawable.setColorFilter(new PorterDuffColorFilter( getProgressColorCode(LVH.mStatusIndicator.getContext(),expiryStatusParams),PorterDuff.Mode.SRC_OVER));
            LVH.mStatusIndicator.setProgressDrawable(progressDrawable);
            //progressDrawable.setColor(expiryStatusParams.getColor());
            //LVH.mStatusIndicator.setProgressDrawable(progressDrawable);


        }



        //Paint progressPaint = ( (ShapeDrawable) progressDrawable.findDrawableByLayerId(2)).getPaint();
        //progressPaint.setColor(LPG_Utility.getExpiryStatusInfo(CurrentRow.getLPGLastBookedDate(),Integer.parseInt( CurrentRow.getLPGExpiryDays())).getColor());
       // ( (ShapeDrawable) progressDrawable.findDrawableByLayerId(2));


        //Set onclick listeners only if user has added cylinder list
        if (!(CurrentRow.LPG_ROW_ID.equals(NOLPGCYLINDERSADDED))) {
        //Assign a onclick listener event for showing activity
        LVH.marEditConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), AddLPGConnection.class);
                Bundle bundle = new Bundle();
                CharSequence lpgRowId = CurrentRow.LPG_ROW_ID;
                intent.putExtra(LPG_SQL_ContractClass.LPG_CONNECTION_ROW.FIELD_CONNECTION_ID_EDIT, CurrentRow.LPG_ROW_ID);
                bundle.putCharSequence(LPG_SQL_ContractClass.LPG_CONNECTION_ROW.FIELD_CONNECTION_ID_EDIT, CurrentRow.LPG_ROW_ID);
                //intent.putExtra(LPG_SQL_ContractClass.LPG_CONNECTION_ROW.BUNDLE_NAME_EDIT_CONNECTION.toString(),bundle);
                Bundle checkb = intent.getExtras();

                v.getContext().startActivity(intent);

            }
        });

       // LVH.mStatusIndicator.setProgress(40);
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
                final Toast toast =  Toast.makeText(v.getContext(),"Dummy", Toast.LENGTH_SHORT);
                final AlarmManager alarmManager = (AlarmManager) v.getContext().getSystemService(Context.ALARM_SERVICE);
                toast.setGravity(Gravity.BOTTOM, 0, 0);

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
                                String deleteCondition = LPG_SQL_ContractClass.LPG_CONNECTION_ROW._ID + " = ? ";
                                String[] deleteParameter = {CurrentRow.LPG_ROW_ID};

                                //delete the record
                                int deleteCount = sqLiteDatabase.delete(LPG_SQL_ContractClass.LPG_CONNECTION_ROW.TABLE_NAME, deleteCondition, deleteParameter);
                                // check no of deleted record
                                if (deleteCount > 0) {
                                    //Delete the alarm set for this connection
                                    int pendingintentRequestCodeForAlarm = Integer.parseInt(CurrentRow.LPG_ROW_ID) * 10 + 1;
                                    Intent alarmCancellationIntent = new Intent(context, LPG_AlarmReceiver.class);
                                    PendingIntent alarmCancellationPendingIntent = PendingIntent.getBroadcast(context, pendingintentRequestCodeForAlarm, alarmCancellationIntent, 0);
                                    alarmManager.cancel(alarmCancellationPendingIntent);
                                    // set a toast that the record has been deleted
                                    toast.setText(R.string.toast_delete_successful);
                                    toast.show();

                                    //start main activity again
                                    Intent intent = new Intent(context,MainActivity.class);
                                    context.startActivity(intent);
                                } else {
                                    // set a toast that the record could not be deleted
                                    toast.setText(R.string.toast_delete_failure);
                                    toast.show();
                                }

                                //close the connection for database
                                // sqLiteDatabase.close();


                            }
                        }
                        , new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // just dismiss the dialog
                                lpgDeleteConnection.dismiss();
                            }
                        });
                //Assign a on click listener to do LPG booking
                FragmentActivity thisFragmentActivity = (FragmentActivity)v.getContext();
                lpgDeleteConnection.show(thisFragmentActivity.getSupportFragmentManager(),"Delete");

            }
        });

        //Assign on click listener to do LPG Booking
        LVH.mARLPGBookingCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Start LPG booking activity with passing on the parcel
                with information about the LPG*/
                Cursor c;

                try {


                    {

                        Intent intentLPGBooking = new Intent(v.getContext(), LPGBooking.class);

                        intentLPGBooking.putExtra(lpgconnectionparcel.LPG_CONNECTIONRECORD_PARCEL, new lpgconnectionparcel(
                                CurrentRow.LPG_ROW_ID,false
                        ));
                        v.getContext().startActivity(intentLPGBooking);
                    }
                } catch (Exception e) {

                    LPG_AlertBoxClass alertDialog = new LPG_AlertBoxClass();
                    alertDialog.showDialogHelper("Error in retrieving DB",
                            "Ok",
                            null,
                            null,
                            null
                    );
                    //alertDialog.show();
                }


            }
        });
        } else {
            LVH.marEditConnection.setVisibility(View.INVISIBLE);
            LVH.mARLPGBookingCall.setVisibility(View.INVISIBLE);
            LVH.mARDeleteConnection.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount(){
        return LPGCylinderList.size();
    }

    //create a static class for the list of lpg cylinder
    private static class LPGCylinderListInfo {
        protected String LPGCylinderName;
        protected String LPGCylinderCompany;
        protected String LPGCylinderExpiry;
        protected String LPG_ROW_ID;

        public String getLPGLastBookedDate() {
            return LPGLastBookedDate;
        }

        public void setLPGLastBookedDate(String LPGLastBookedDate) {
            this.LPGLastBookedDate = LPGLastBookedDate;
        }

        public String getLPGExpiryDays() {
            return LPGExpiryDays;
        }

        public void setLPGExpiryDays(String LPGExpiryDays) {
            this.LPGExpiryDays = LPGExpiryDays;
        }

        protected String LPGLastBookedDate;
        protected String LPGExpiryDays;

        //Constructor for LPG Cylinder list
        public LPGCylinderListInfo(String LPGCylinderNameInit, String LPGCylinderCompanyInit,
                                   String LPGCylinderExpiryInit, String LPGrowid,
                                   String LPGLastBookedDateInit, String LPGExpiryDaysInit

        ) {
            LPGCylinderName = LPGCylinderNameInit;
            LPGCylinderCompany = LPGCylinderCompanyInit;
            LPGCylinderExpiry = LPGCylinderExpiryInit;
            LPG_ROW_ID = LPGrowid;
            LPGLastBookedDate= LPGLastBookedDateInit ;
            LPGExpiryDays = LPGExpiryDaysInit;
        }
    }

    //Create view holder class
    public static class LPGViewHolder extends RecyclerView.ViewHolder {
        //Create the view holder test
        protected TextView mARLPGName;
        protected TextView marLPGCompanyName;
        protected TextView marLPGExpiry;
        protected ImageButton marEditConnection;
        protected ImageButton mARDeleteConnection;
        protected ImageButton mARLPGBookingCall;
        protected ProgressBar mStatusIndicator;

        public LPGViewHolder(View v) {
            super(v);
            //assign the view holder elements in constructor
            mARLPGName = (TextView) v.findViewById(R.id.lpg_cylinder_name);
            marLPGCompanyName = (TextView) v.findViewById(R.id.lpg_cylinder_company);
            marLPGExpiry = (TextView) v.findViewById(R.id.lpg_expiry);
            marEditConnection = (ImageButton) v.findViewById(R.id.edit_connection_btn);
            mARDeleteConnection = (ImageButton) v.findViewById(R.id.delete_connection_btn);
            mARLPGBookingCall = (ImageButton) v.findViewById(R.id.call_refill_btn);
            mStatusIndicator = (ProgressBar) v.findViewById(R.id.expiry_indicator);

        }

    }

    private int getProgressColorCode (Context context, int expiryPercent){
        if(expiryPercent < (int) CAP_EXPIRYSTATUS_GREEN){
            return  context.getResources().getColor( R.color.colorPrimary);
        } else if (expiryPercent  < (int) CAP_EXPIRYSTATUS_ORANGE) {
            return  context.getResources().getColor(R.color.colorOrange);
        } else return  context.getResources().getColor(R.color.error_red);


    }
}
