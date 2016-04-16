package com.aekan.navya.lpgbooking;

import android.provider.BaseColumns;

/**
 * Created by arunramamurthy on 14/04/16.
 */
public final class LPG_SQL_ContractClass {
    //prevent anybody from automatically instantiating the class
    public LPG_SQL_ContractClass(){}

    //create constants for the database
    public static abstract class LPG_CONNECTION_ROW implements BaseColumns{
        public static final String TABLE_NAME = "connection";
        public static final String CONNECTION_NAME ="connection_name";
        public static final String PROVIDER = "provider";
        public static final String AGENCY ="agency";
        public static final String AGENCY_PHONE_NUMBER ="agency_phone_no";


    }

}
