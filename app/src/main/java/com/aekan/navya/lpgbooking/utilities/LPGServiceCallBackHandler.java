package com.aekan.navya.lpgbooking.utilities;

import android.database.Cursor;
import android.os.Handler;
import android.os.Message;

/**
 * Created by aruramam on 4/8/2017.
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
