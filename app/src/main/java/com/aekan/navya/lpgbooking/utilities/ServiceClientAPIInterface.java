package com.aekan.navya.lpgbooking.utilities;

import android.database.Cursor;

/**
 * Created by aruramam on 3/29/2017.
 * Promise interface for handler thread
 * Contains method to service client requests.
 * This interface helps seperate API implementation logic
 * from handler mechanism.
 */

public interface ServiceClientAPIInterface {
    public Cursor getCylinderRecordForID(String rowID);
    public String getIncrementedPrimaryKey();
}
