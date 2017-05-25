package com.aekan.navya.lpgbooking.utilities;

import android.provider.BaseColumns;

/**
 * Created by arunramamurthy on 29/10/16.
 */

public  final class LPG_SQL_ContractClass {
    //prevent anybody from automatically instantiating the class
    public LPG_SQL_ContractClass(){}

    //create constants for the database
    public static abstract class LPG_CONNECTION_ROW implements BaseColumns {
        public static final String TABLE_NAME = "connection";
        public static final String CONNECTION_NAME ="connection_name";
        public static final String PROVIDER = "provider";
        public static final String AGENCY ="agency";
        public static final String AGENCY_PHONE_NUMBER ="agency_phone_no";
        public static final String AGENCY_SMS_NUMBER="sms_number";
        public static final String CONNECTION_ID = "connection_id_with_agency";
        public static final String LAST_BOOKED_DATE = "last_booked_date";
        public static final String CONNECTION_EXPIRY_DAYS = "connection_expiry_date";

        //        charsequence value associated with editing a connection
        public static final String FIELD_CONNECTION_ID_EDIT = "Connection Id for editing";
        public static final CharSequence BUNDLE_NAME_EDIT_CONNECTION = "com.aekan.navya.lpgbooking.editconnectionbundle";
        public static final CharSequence VALUE_CONNECTION_ID_NULL = "No connection to retrieve";
    }

}
