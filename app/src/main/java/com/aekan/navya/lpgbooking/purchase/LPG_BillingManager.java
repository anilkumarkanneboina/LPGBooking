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
import com.android.billingclient.api.PurchaseHistoryResponseListener;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by arunramamurthy on 01/01/18.
 */

public class LPG_BillingManager implements BillingManager,SkuDetailsResponseListener,BillingClientStateListener,PurchaseHistoryResponseListener{
    private BillingClient mBillingClient;

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
                } else {
                    //query for purchases
                    if (LPG_Purchase_Utility.cachePurchaseList == null){
                        //create SKU Details param list
                        mBillingClient.queryPurchaseHistoryAsync(mSKUID, new PurchaseHistoryResponseListener() {
                            @Override
                            public void onPurchaseHistoryResponse(int responseCode, List<Purchase> purchasesList) {
                                switch (responseCode){
                                    case BillingClient.BillingResponse.OK:
                                        LPG_Purchase_Utility.cachePurchaseList = purchasesList;
                                        break;
                                    default:
                                        break;
                                }

                            }
                        });
                    }
                }

        }

    }

    @Override
    public int isSKUPurchased(String SKUID){
        //Update whether user had already purchased the SKU item
        //this is preferably to be done only once when the app loads
        int purchaseAvailable =LPG_Purchase_Utility.PREMIUM_USER;
        if(mBillingClient.isReady()){
            Purchase.PurchasesResult purchasesResult = mBillingClient.queryPurchases(BillingClient.SkuType.INAPP);
            LPG_Purchase_Utility.isPurchaseChecked = true;
            switch (purchasesResult.getResponseCode()){
                case BillingClient.BillingResponse.FEATURE_NOT_SUPPORTED:
                    Toast.makeText(mActivity.getApplicationContext(),mActivity.getResources().getString(R.string.billing_featurenotsupported),Toast.LENGTH_LONG);
                    return LPG_Purchase_Utility.REGULAR_USER;

                case BillingClient.BillingResponse.ERROR:
                default:
                    Toast.makeText(mActivity.getApplicationContext(),mActivity.getResources().getString(R.string.billing_error_gathering_purchasedetails),Toast.LENGTH_LONG);
                    return LPG_Purchase_Utility.REGULAR_USER;

                case BillingClient.BillingResponse.OK:
                    for(Purchase counter:purchasesResult.getPurchasesList()){
                        if(counter.getSku().equals(mSKUID)){
                            purchaseAvailable = LPG_Purchase_Utility.PREMIUM_USER;
                            break;
                        }
                    }
                    break;
            }

        } else {
            mRunnable = new Runnable() {
                @Override
                public void run() {
                    Purchase.PurchasesResult purchasesResult = mBillingClient.queryPurchases(BillingClient.SkuType.INAPP);
                    LPG_Purchase_Utility.isPurchaseChecked = true;
                    switch (purchasesResult.getResponseCode()) {
                        default:
                            Toast.makeText(mActivity.getApplicationContext(), mActivity.getResources().getString(R.string.billing_error_gathering_purchasedetails), Toast.LENGTH_LONG);
                            break;
                        case BillingClient.BillingResponse.OK:

                            for (Purchase counter : purchasesResult.getPurchasesList()) {

                                if (counter.getSku().equals(mSKUID)) {
                                    if (mBillingConsumer != null) {
                                        mBillingConsumer.updatePurchaseInfo(true);
                                    }break;
                                }
                            }
                            break;
                    }
                }
            };
            mBillingClient.startConnection(this);
            purchaseAvailable = LPG_Purchase_Utility.USER_DETAILS_FETCHED_ASYNCHRONOUS;

        }
        return purchaseAvailable;
    }

    @Override
    public boolean isReady(){ return mBillingClient.isReady(); }

    @Override
    public void closeConnection(){
        mBillingClient.endConnection();
    }

    @Override
    public void onPurchaseHistoryResponse(int responseCode, List<Purchase> purchasesList){
        switch (responseCode){
            case BillingClient.BillingResponse.OK:
                LPG_Purchase_Utility.cachePurchaseList = purchasesList;
                break;
            default:
                break;

        }
    }

}
