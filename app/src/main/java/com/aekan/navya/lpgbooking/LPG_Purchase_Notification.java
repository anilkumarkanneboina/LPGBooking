package com.aekan.navya.lpgbooking;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.aekan.navya.lpgbooking.purchase.BillingConsumer;
import com.aekan.navya.lpgbooking.purchase.BillingManager;
import com.aekan.navya.lpgbooking.purchase.LPG_BillingManager;
import com.aekan.navya.lpgbooking.purchase.LPG_Purchase_Utility;
import com.aekan.navya.lpgbooking.utilities.LPG_AlertBoxClass;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;

import java.util.List;


/**
 * Created by arunramamurthy on 31/12/17.
 */

public class LPG_Purchase_Notification extends AppCompatActivity implements BillingConsumer,PurchasesUpdatedListener {


    private boolean mIsServiceConnected;
    private BillingManager mBillingManager;


    @Override
    protected void onCreate(Bundle bundle){
        //call super class method and inflate activity layout
        super.onCreate(bundle);
        setContentView(R.layout.activity_purchase);

        final Intent intent = new Intent(getApplicationContext(),MainActivity.class);

        //set purchase button as disbled till service connection is established
        Log.v("Purchase" , " Button disabled");
        updateBillingConnectionStatus(false);

        //start service connection for purchases
        mBillingManager=new LPG_BillingManager(LPG_Purchase_Utility.PREMIUM_USER_SKU,this,this,this);
        mBillingManager.getSKUDetails(LPG_Purchase_Utility.PREMIUM_USER_SKU);

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

        Button startPurchase = (Button) findViewById(R.id.purchase_ok);
        startPurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBillingManager.purchasePremium(LPG_Purchase_Utility.PREMIUM_USER_SKU, BillingClient.SkuType.INAPP);

            }
        });





    }

    @Override
    protected void onDestroy(){
        //call super method first
        super.onDestroy();
        //disconnect billing connection if still valid
        if(mIsServiceConnected || mBillingManager.isReady()){ mBillingManager.closeConnection(); }
    }
    @Override
    public void updateSKUInfo(List<SkuDetails> skuDetails){
        //update details for the skuDetails
        if(skuDetails != null) {

            for (SkuDetails counter : skuDetails) {
                if (counter.getSku().equals(LPG_Purchase_Utility.PREMIUM_USER_SKU)) {
                    String purchaseInfo = getResources().getString(R.string.billing_SKU_cost);
                    purchaseInfo += " " + counter.getPrice();
                    TextView purchaseNotification = (TextView) findViewById(R.id.sku_details);
                    purchaseNotification.setVisibility(View.VISIBLE);
                    purchaseNotification.setText(purchaseInfo);
                    break;
                }
            }
        }


    }
    @Override
    public void onPurchasesUpdated(int responseCode, List<Purchase> purchases){
        String SKUID = LPG_Purchase_Utility.PREMIUM_USER_SKU;
        LPG_AlertBoxClass alertBoxClass = new LPG_AlertBoxClass();
        switch (responseCode){
            case BillingClient.BillingResponse.DEVELOPER_ERROR:
                Toast.makeText(getApplicationContext(),getResources().getString(R.string.billing_conn_developer_error),Toast.LENGTH_LONG);
                break;
            case BillingClient.BillingResponse.ERROR:

                Toast.makeText(getApplicationContext(),getResources().getString(R.string.billing_conn_error),Toast.LENGTH_LONG);
                break;
            case BillingClient.BillingResponse.SERVICE_DISCONNECTED:

                Toast.makeText(getApplicationContext(),getResources().getString(R.string.billing_conn_service_disconnected),Toast.LENGTH_LONG);
                break;
            case BillingClient.BillingResponse.SERVICE_UNAVAILABLE:

                Toast.makeText(getApplicationContext(),getResources().getString(R.string.billing_conn_network_down),Toast.LENGTH_LONG);
                break;
            case BillingClient.BillingResponse.USER_CANCELED:
                Toast.makeText(getApplicationContext(),getResources().getString(R.string.billing_user_cancelled),Toast.LENGTH_LONG);
                break;
            case BillingClient.BillingResponse.ITEM_UNAVAILABLE:
                Toast.makeText(getApplicationContext(),getResources().getString(R.string.billing_item_unavailable),Toast.LENGTH_LONG);
                break;
            case BillingClient.BillingResponse.ITEM_ALREADY_OWNED:
                updatePremiumStatus();
            { LPG_Purchase_Utility.setPremiumUserSku(this,SKUID); }
            alertBoxClass.showDialogHelper(getResources().getString(R.string.billing_purchase_success_dialogtitle),
                    getResources().getString(R.string.billing_purchase_alreadyowned),
                    null,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    },
                    null
            ).show(getSupportFragmentManager(),"Purchase confirmation");
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
            break;
            case BillingClient.BillingResponse.OK:
                //update status of user
                updatePremiumStatus();
                //check if purchase has been made for specified SKU.
                boolean isSpecificSKUPurchased = false;
                for(Purchase purchase : purchases){

                    isSpecificSKUPurchased = (purchase.getSku() == SKUID);
                }
                if (isSpecificSKUPurchased) { LPG_Purchase_Utility.setPremiumUserSku(this,SKUID); }

                alertBoxClass.showDialogHelper(getResources().getString(R.string.billing_purchase_success_dialogtitle),
                        getResources().getString(R.string.billing_purchase_alreadyowned),
                        null,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        },
                        null
                ).show(getSupportFragmentManager(),"Purchase confirmation");

                Intent intentMain = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intentMain);
                break;
            default:
                Toast.makeText(getApplicationContext(),getResources().getString(R.string.billing_purchase_default),Toast.LENGTH_LONG);
        }

    }
    @Override
    public void updatePurchaseInfo(boolean premium){


    }

    private void updatePremiumStatus(){
        SharedPreferences sharedPreferences = getSharedPreferences(getResources().getString(R.string.billing_sharedpref_filename),Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getResources().getString(R.string.billing_sharedpref_key),LPG_Purchase_Utility.PREMIUM_USER_SKU);
        editor.commit();

    }

    @Override
    public void updateBillingConnectionStatus(boolean connectionStatus){
        Log.v("Purchase"," Within updateBillingConnectionStatus");

        Button purchaseButton = (Button) findViewById(R.id.purchase_ok);
        if(connectionStatus){
            purchaseButton.setText(R.string.billing_purchase_yes);
            Log.v("Purchase", "Button text set as Yes");

        } else {
            Log.v("Purchase","Button text set as Connecting..");
            purchaseButton.setText(R.string.billing_purchase_yes_connecting);


        }

        purchaseButton.setEnabled(connectionStatus);

    }


}
