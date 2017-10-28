package com.aekan.navya.lpgbooking.utilities;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.BaseColumns;

/**
 * Created by arunramamurthy on 16/10/16.
 * This class is used to morph an individual
 * record of local data base into a parcealable
 * object, so that the records can be passed between
 * activities in the application.
 */

public class lpgconnectionparcel implements Parcelable {

    /*declare local variables for the parcel*/
    private String id;
    private boolean isFromNotification ;
    private Context mContext;
    //constant for identifying the parcel
    public final static String  LPG_CONNECTIONRECORD_PARCEL = "LPG Record parcel";

    //constructor to create values for the parcel
    public lpgconnectionparcel(String id,
                               String connectionname,
                               String provider,
                               String agency,
                               String phonenumber,
                               String connectionid,
                               String lastbookeddate
    ){
        this.id=id;


    }

    public lpgconnectionparcel(String id){
        this.id = id;
        this.isFromNotification = false;
    }

    public lpgconnectionparcel(String id, boolean isFromNotification){
        this.id = id;
        this.isFromNotification = isFromNotification;
    }

    public lpgconnectionparcel(String id,boolean isFromNotification,Context context){
        this.id = id;
        this.isFromNotification = isFromNotification;
        this.mContext = context;


    }

    //constructor to set the parcel for the Parcelable object
    public lpgconnectionparcel(Parcel parcel){
        boolean[] notificationFlag = new boolean[1];

        id = parcel.readString();
        parcel.readBooleanArray(notificationFlag);
        isFromNotification = notificationFlag[0];
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(id);
        dest.writeBooleanArray(new boolean[]{isFromNotification});


    }

    public static final Parcelable.Creator<lpgconnectionparcel> CREATOR = new Parcelable.Creator<lpgconnectionparcel>(){
        @Override
        public lpgconnectionparcel createFromParcel(Parcel source){
            return new lpgconnectionparcel(source);
        }

        @Override
        public lpgconnectionparcel[] newArray(int size){
            return new lpgconnectionparcel[size];
        }
    };


    //getter methods for the object

    public String getId(){return this.id;}

    public boolean getNotificationFlag() { return this.isFromNotification ;}

    public Context getmContext() {return  this.mContext;}

    /**
     * Created by arunramamurthy on 14/04/16.
     */

}
