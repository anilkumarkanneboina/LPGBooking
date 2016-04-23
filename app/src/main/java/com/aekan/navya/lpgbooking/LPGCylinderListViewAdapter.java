package com.aekan.navya.lpgbooking;

import android.database.sqlite.SQLiteCursor;
import android.support.annotation.BoolRes;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

        //Constructor for LPG Cylinder list
        public LPGCylinderListInfo (String LPGCylinderNameInit, String LPGCylinderCompanyInit,String LPGCylinderExpiryInit) {
            LPGCylinderName = LPGCylinderNameInit;
            LPGCylinderCompany = LPGCylinderCompanyInit;
            LPGCylinderExpiry = LPGCylinderExpiryInit;

        }
    }

    //Create view holder class
    public static class LPGViewHolder extends RecyclerView.ViewHolder{
        //Create the view holder test
        protected TextView mARLPGName;
        protected TextView marLPGCompanyName;
        protected TextView marLPGExpiry;

        public LPGViewHolder(View v){
            super(v);
            //assign the view holder elements in constructor
            mARLPGName = (TextView) v.findViewById(R.id.lpg_cylinder_name);
            marLPGCompanyName = (TextView) v.findViewById(R.id.lpg_cylinder_company);
            marLPGExpiry = (TextView) v.findViewById(R.id.lpg_expiry);
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
                    "Cylinder will expiry in " + i + " days"
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
            LPGCylinderList.add(new LPGCylinderListInfo("No Connections Found", "You can add your LPG connection now!!", "Just click on Add button below"));
            Log.v("Initialisation",Integer.toString( LPGCylinderList.size()));
        }else{
//            default:
                //create the Array list from SQLiteCursor
                sqLiteCursor.moveToFirst();

                //get column count
                int columnCount = sqLiteCursor.getColumnCount();
                String lpgConnection, lpgAgency, lpgProvider;
                while (sqLiteCursor.isAfterLast() != true   ){
                    {
                        lpgConnection = sqLiteCursor.getString(0);
                        lpgProvider = sqLiteCursor.getString(1);
                        lpgAgency = sqLiteCursor.getString(2);

                        LPGCylinderList.add( new LPGCylinderListInfo(lpgConnection,lpgProvider,lpgAgency));

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
        LPGCylinderListInfo CurrentRow = LPGCylinderList.get(i);
        Log.v("Adapter on Bind",CurrentRow.LPGCylinderName);
        Log.v("Adapter on Bind",CurrentRow.LPGCylinderCompany);
        Log.v("Adapter on Bind",CurrentRow.LPGCylinderExpiry);

        //Assign the text value for each of the item
        LVH.mARLPGName.setText(CurrentRow.LPGCylinderName);
        LVH.marLPGCompanyName.setText(CurrentRow.LPGCylinderCompany);
        LVH.marLPGExpiry.setText(CurrentRow.LPGCylinderExpiry);
    }

    @Override
    public int getItemCount(){
        return LPGCylinderList.size();
    }
}
