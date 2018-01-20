package com.aekan.navya.lpgbooking.purchase;

import android.content.Context;
import android.content.SharedPreferences;

import com.aekan.navya.lpgbooking.R;
import com.android.billingclient.api.Purchase;

import java.util.List;

/**
 * Created by arunramamurthy on 01/01/18.
 */

public class LPG_Purchase_Utility {
    public static final String PREMIUM_USER_SKU = "lpg_booking_premium_user";
    public static final String USER_STATUS_UNDEFINED = "User status has not been defined yet";
    public static final String USER_PURCHASE_ASYNCHRONOUS = "User purchase is not in cache and is being retrieved asynchronously";
    public static final int PREMIUM_USER = 2348;
    public static final int REGULAR_USER = 4634;
    public static final int USER_DETAILS_FETCHED_ASYNCHRONOUS= 43598;
    public static final String USER_STATUS_REGULAR  ="User needs to buy In-App feature";
    public static final String USER_STATUS_PREMIUM = "User is a premium user" ;
    public static final String SKU_STATUS_DEFAULT="Default status for SKU";
    public static boolean isPurchaseChecked;
    public static List<Purchase> cachePurchaseList;
    private static String userStatus = USER_STATUS_UNDEFINED;

    private static final int CAP_FREEMIUM_CONNECTIONS = 1;
    static {
        isPurchaseChecked = false;
        cachePurchaseList = null;
    }

    public static void setPremiumUserSku(Context context,String skuID){
        userStatus = USER_STATUS_PREMIUM;
        updateUserStatus(context,skuID);


    }

    private  static void updateUserStatus(Context context,String skuID){
        //update share preference

        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getResources().getString(R.string.billing_sharedpref_filename),Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getResources().getString(R.string.billing_sharedpref_key),skuID);
        editor.commit();
    }

    public static boolean getSKUStatus(Context context,String skuID){
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getResources().getString(R.string.billing_sharedpref_filename),Context.MODE_PRIVATE);
        String SKUStatus = sharedPreferences.getString(context.getResources().getString(R.string.billing_sharedpref_key),SKU_STATUS_DEFAULT);

        if(SKUStatus.equals(SKU_STATUS_DEFAULT)){
            return false;
        } else {
            return true;
        }
    }

    public static boolean returnPremiumUserStatus(boolean userStatus, int noOfConnections){
        boolean status = false;
        if(userStatus){
            if(noOfConnections < CAP_FREEMIUM_CONNECTIONS){
                status = true;
            } else {status = false;}
        } else{status = false;}
       return status;
    }

}
