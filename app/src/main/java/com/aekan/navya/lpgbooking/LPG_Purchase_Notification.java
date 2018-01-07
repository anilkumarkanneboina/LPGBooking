package com.aekan.navya.lpgbooking;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.aekan.navya.lpgbooking.purchase.BillingManager;
import com.aekan.navya.lpgbooking.purchase.LPG_BillingManager;
import com.aekan.navya.lpgbooking.purchase.LPG_Purchase_Utility;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;

import java.util.List;


/**
 * Created by arunramamurthy on 31/12/17.
 */

public class LPG_Purchase_Notification extends AppCompatActivity implements BillingClientStateListener,PurchasesUpdatedListener {

    private BillingClient mBillingClient;
    private boolean mIsServiceConnected;
    private BillingManager mBillingManager;


    @Override
    protected void onCreate(Bundle bundle){
        //call super class method and inflate activity layout
        super.onCreate(bundle);
        setContentView(R.layout.activity_purchase);

        final Intent intent = new Intent(getApplicationContext(),MainActivity.class);

        //start service connection
        mIsServiceConnected=false;
        mBillingClient = BillingClient.newBuilder(this).setListener(this).build();
        mBillingClient.startConnection(this);

        //set tool bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.purchase_toolbar);
        setSupportActionBar(toolbar);
        setTitle(R.string.purchase_activity_title);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
            }
        });

        //set cancel button
        Button cancelPurchase = (Button) findViewById(R.id.purchase_not_ok);
        cancelPurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
            }
        });




    }
    @Override
    public void onBillingServiceDisconnected(){
        //re-initiate connection
        mIsServiceConnected=false;
        mBillingClient.startConnection(this);
    }
    @Override
    public void onBillingSetupFinished(int responseCode){
        switch (responseCode) {
            case BillingClient.BillingResponse.ERROR:
                mIsServiceConnected=false;
                Toast.makeText(this,R.string.billing_conn_error,Toast.LENGTH_LONG);
                break;
            case BillingClient.BillingResponse.SERVICE_DISCONNECTED:
                mIsServiceConnected=false;
                Toast.makeText(this,R.string.billing_conn_service_disconnected,Toast.LENGTH_LONG);
                break;
            case BillingClient.BillingResponse.SERVICE_UNAVAILABLE:
                mIsServiceConnected=false;
                Toast.makeText(this,R.string.billing_conn_network_down,Toast.LENGTH_LONG);
                break;
            case BillingClient.BillingResponse.OK:
                mIsServiceConnected=true;
                mBillingManager = new LPG_BillingManager(mBillingClient, LPG_Purchase_Utility.PREMIUM_USER_SKU,this,this);

        }
    }
    @Override
    public void onPurchasesUpdated (int responseCode,
                                    List<Purchase> purchases){
        String SKU_ID = LPG_Purchase_Utility.PREMIUM_USER_SKU;
        switch (responseCode){
            case BillingClient.BillingResponse.OK:
                //check if purchase has been made for specified SKU.
                boolean isSpecificSKUPurchased = false;
                for(Purchase purchase : purchases){
                    isSpecificSKUPurchased = (purchase.getSku() == SKU_ID);
                }
                if (isSpecificSKUPurchased) { LPG_Purchase_Utility.setPremiumUserSku(this,SKU_ID); }
                break;

        }

    }
    @Override
    protected void onDestroy(){
        //call super method first
        super.onDestroy();
        //disconnect billing connection if still valid
        if(mIsServiceConnected){ mBillingClient.endConnection(); }
    }

    private void updateSKUDetails(String SKUID){


    }



}
