package com.aekan.navya.lpgbooking.utilities;

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
    }

    //constructor to set the parcel for the Parcelable object
    public lpgconnectionparcel(Parcel parcel){
        id = parcel.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(id);

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

    /**
     * Created by arunramamurthy on 14/04/16.
     */

}
