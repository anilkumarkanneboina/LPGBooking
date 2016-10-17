package com.aekan.navya.lpgbooking.utilities;

import android.os.Parcel;
import android.os.Parcelable;

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
    private String connectionname;
    private String provider;
    private String agency;
    private String phonenumber;
    private String connectionid;
    private String lastbookeddate;
    private Parcel recordParcel;

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
        this.connectionname =   connectionname;
        this.provider       =   provider;
        this.agency         =   agency;
        this.phonenumber    =   phonenumber;
        this.connectionid   =   connectionid;
        this.lastbookeddate =   lastbookeddate;
        this.recordParcel   =   null;


    }

    //constructor to set the parcel for the Parcelable object
    public lpgconnectionparcel(Parcel parcel){
        this.recordParcel = parcel;

        //set other values to be null
        this.id = null;
        this.connectionname = null;
        this.provider=null;
        this.agency = null;
        this.phonenumber=null;
        this.connectionid=null;
        this.lastbookeddate=null;


    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        //assign value to destination parcel
        if (this.recordParcel == null){
            //if the current object stores information in
            //individual fields rather than in parcel object
            //then write them to the destination parcel
            dest.writeString(this.id);
            dest.writeString(this.connectionname);
            dest.writeString(this.provider);
            dest.writeString(this.agency);
            dest.writeString(this.phonenumber);
            dest.writeString(this.connectionid);
            dest.writeString(this.lastbookeddate);

        } else {
            //store the current parcel in destination parcel
            dest = this.recordParcel;
        }

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
    public Parcel getRecordParcel(){ return this.recordParcel; }
    public String getId(){return this.id;}
    public String getConnectionname(){ return this.connectionname; }
    public String getProvider(){return this.provider;}
    public String getAgency(){return this.agency;}
    public String getPhonenumber() {return this.phonenumber;}
    public String getConnectionid(){return this.connectionid;}
    public String getLastbookeddate(){return this.lastbookeddate;}
    public Parcel getLpgConnectionrecordParcel(){return this.recordParcel;}
}
