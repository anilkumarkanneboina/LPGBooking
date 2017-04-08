package com.aekan.navya.lpgbooking.utilities;

import android.database.Cursor;

/**
 * Created by aruramam on 4/8/2017.
 * This is a wrapper on implementation logic for handling response messages
 * in Service request's responses.
 * The Handler class will call API methods in this class
 * as callback on receiving response messages.
 */

public interface LPGServiceResponseCallBack {
    public void updateActivityWithLPGDetailsCursor(Cursor c);
}
