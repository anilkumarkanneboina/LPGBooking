package com.aekan.navya.lpgbooking.utilities;

import android.database.Cursor;
import android.os.Handler;
import android.os.Message;

/**
 * Created by aruramam on 4/8/2017.
 *
 * This handler is the "catcher" - ie., it catches the service response message, and appropriately takes action.
 * Since the service response, as thrown by "worker" , could be one of two activities, the catcher will need methods to take appropriate action
 * for each of the activity.
 * Thuus, the two methods would be - update Activity with LPG details , (or), update primary key of LPG record.
 *
 * This catcher object actually use an interface - which we would call as catcher-helper interface. The interface just contains the two methods
 * corresponding to activities. The catcher-helper interface is LPGServiceResponseCallBack
 */

public class LPGServiceCallBackHandler implements Handler.Callback {
    //Wrapper class for implemenation logic of callback
    private LPGServiceResponseCallBack mServiceResponseCallBack;

    public LPGServiceCallBackHandler(LPGServiceResponseCallBack callBack ){
        mServiceResponseCallBack = callBack;
    }

    @Override
    public boolean handleMessage(Message msg) {
        //select appropriate methods based on message value
        switch(msg.what){
            case LPG_Utility.MSG_GETALLCYLINDERS:
                //call appropriate call back method
                mServiceResponseCallBack.updateActivityWithLPGDetailsCursor((Cursor) msg.obj);
                break;
            case LPG_Utility.MSG_INCREMENTPRIMARYKEY:
                //call appropriate call back method
                mServiceResponseCallBack.updatePrimaryKeyIncrement((String) msg.obj);
            default:
                break;
        }

        return false;
    }
}
