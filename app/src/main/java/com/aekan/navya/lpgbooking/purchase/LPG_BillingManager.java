package com.aekan.navya.lpgbooking.purchase;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.aekan.navya.lpgbooking.R;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by arunramamurthy on 01/01/18.
 */

public class LPG_BillingManager implements BillingManager,SkuDetailsResponseListener,PurchasesUpdatedListener{
    private BillingClient mBillingClient;
    private ArrayList<Purchase> mPurchases;
    private String mSKUID;
    private ArrayList<SkuDetails> mSkuDetailsList;
    private Activity mActivity;
    private PurchasesUpdatedListener mPurchaseUpdatedListener;

    public LPG_BillingManager(BillingClient billingClient, String SKUID, Activity activity,PurchasesUpdatedListener purchasesUpdatedListener){
        // prepare Billing Client
        mBillingClient = billingClient;
        mSKUID=SKUID;
        mActivity = activity;
        mPurchaseUpdatedListener = purchasesUpdatedListener;
    }
    @Override
    public String getSKUDescription( String SKUID){
        return "STring";
    }

    private void getAllSKUDetails(){
        //create SKU Params
        List<String> skuList = new ArrayList<>();
        skuList.add(mSKUID);

        final SkuDetailsParams skuQueryParams = SkuDetailsParams.newBuilder().setSkusList(skuList).setType(BillingClient.SkuType.INAPP).build();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                mBillingClient.querySkuDetailsAsync(skuQueryParams,LPG_BillingManager.this);
            }
        };
        executeServiceRequest(runnable);
    }


    @Override
    public void onSkuDetailsResponse(int responseCode, List<SkuDetails> skuDetailsList){


    }

    private void executeServiceRequest(Runnable r){
        if (mBillingClient.isReady()){
            r.run();
        }
        else {
            startGooglePlayConnection(r);
        }
    }

    private void startGooglePlayConnection(Runnable runnable){
        if (!(mBillingClient.isReady())) {

        }

    }

    @Override
    public void onPurchasesUpdated(int responseCode, List<Purchase> purchases){
        switch (responseCode){
            case BillingClient.BillingResponse.OK:
                //check if purchase has been made for specified SKU.
                boolean isSpecificSKUPurchased = false;
                for(Purchase purchase : purchases){
                    mPurchases.add(purchase);
                    isSpecificSKUPurchased = (purchase.getSku() == mSKUID);
                }
                if (isSpecificSKUPurchased) { updateUserStatus(mSKUID); }
                break;

        }

    }

    private void updateUserStatus(String skuID){
        //update share preference
        Context context = mActivity.getApplicationContext();
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getResources().getString(R.string.billing_sharedpref_filename),Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getResources().getString(R.string.billing_sharedpref_key),mSKUID);
        editor.commit();

        //update user status in app utility

    }

}
