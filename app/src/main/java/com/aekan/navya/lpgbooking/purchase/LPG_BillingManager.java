package com.aekan.navya.lpgbooking.purchase;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.aekan.navya.lpgbooking.R;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
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

public class LPG_BillingManager implements BillingManager,SkuDetailsResponseListener,BillingClientStateListener{
    private BillingClient mBillingClient;
    private ArrayList<Purchase> mPurchases;
    private String mSKUID;
    private Activity mActivity;
    private boolean mIsServiceActive;
    private Runnable mRunnable;
    private BillingConsumer mBillingConsumer;

    public LPG_BillingManager( String SKUID, Activity activity,BillingConsumer billingConsumer,PurchasesUpdatedListener purchasesUpdatedListener){
        // prepare Billing Client

        mIsServiceActive = false;
        mSKUID=SKUID;
        mActivity = activity;
        mBillingConsumer = billingConsumer;
        //create new Billing client for furthur processing
        mBillingClient = BillingClient.newBuilder(mActivity.getApplicationContext()).setListener(purchasesUpdatedListener).build();
        mBillingClient.startConnection(this);
    }
    @Override
    public void getSKUDetails( String SKUID){
        //provide details about a SKU
        //check if billing client is active
        if (mBillingClient.isReady()){
            //query billing client for SKU details if active
            List<String> skuList = new ArrayList<>();
            skuList.add(mSKUID);
            SkuDetailsParams.Builder skuDetailsParams = SkuDetailsParams.newBuilder();
            skuDetailsParams.setSkusList(skuList);
            skuDetailsParams.setType(BillingClient.SkuType.INAPP);
            mBillingClient.querySkuDetailsAsync(skuDetailsParams.build(),this);
        } else {
            mRunnable = new Runnable() {
                @Override
                public void run() {
                    List<String> skuList = new ArrayList<>();
                    skuList.add(mSKUID);
                    SkuDetailsParams.Builder skuDetailsParams = SkuDetailsParams.newBuilder();
                    skuDetailsParams.setSkusList(skuList);
                    skuDetailsParams.setType(BillingClient.SkuType.INAPP);
                    mBillingClient.querySkuDetailsAsync(skuDetailsParams.build(),LPG_BillingManager.this);
                }
            };
            mBillingClient.startConnection(this);
        }
    }

    @Override
    public void onSkuDetailsResponse(int responseCode, List<SkuDetails> skuDetailsList){
        if(mBillingConsumer!=null){
        switch(responseCode){
            default:
                    mBillingConsumer.updateSKUInfo(null);
                    break;
            case BillingClient.BillingResponse.OK:
                    mBillingConsumer.updateSKUInfo(skuDetailsList);
                    break;




        }
        }



    }

    @Override
    public void onBillingServiceDisconnected(){

    }

    @Override
    public void onBillingSetupFinished(int ResponseCode){
        //set flag for active connection
        switch (ResponseCode){
            case BillingClient.BillingResponse.DEVELOPER_ERROR:
                Toast.makeText(mActivity.getApplicationContext(),mActivity.getResources().getString(R.string.billing_conn_developer_error),Toast.LENGTH_LONG);
                break;
            case BillingClient.BillingResponse.ERROR:
                mIsServiceActive=false;
                Toast.makeText(mActivity.getApplicationContext(),mActivity.getResources().getString(R.string.billing_conn_error),Toast.LENGTH_LONG);
                break;
            case BillingClient.BillingResponse.SERVICE_DISCONNECTED:
                mIsServiceActive=false;
                Toast.makeText(mActivity.getApplicationContext(),mActivity.getResources().getString(R.string.billing_conn_service_disconnected),Toast.LENGTH_LONG);
                break;
            case BillingClient.BillingResponse.SERVICE_UNAVAILABLE:
                mIsServiceActive=false;
                Toast.makeText(mActivity.getApplicationContext(),mActivity.getResources().getString(R.string.billing_conn_network_down),Toast.LENGTH_LONG);
            case BillingClient.BillingResponse.OK:
                if(mRunnable != null){
                    mRunnable.run();
                }
        }

    }


}
